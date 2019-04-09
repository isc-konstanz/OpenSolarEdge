/* 
 * Copyright 2016-18 ISC Konstanz
 * 
 * This file is part of OpenSolarEdge.
 * For more information visit https://github.com/isc-konstanz/OpenSolarEdge
 * 
 * OpenSolarEdge is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * OpenSolarEdge is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with OpenSolarEdge.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.openmuc.solaredge;

import java.security.InvalidParameterException;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.TimeZone;

import org.openmuc.jsonpath.HttpHandler;
import org.openmuc.jsonpath.com.HttpGeneralException;
import org.openmuc.jsonpath.data.TimeValue;
import org.openmuc.jsonpath.request.HttpRequest;
import org.openmuc.jsonpath.request.HttpRequestAction;
import org.openmuc.jsonpath.request.HttpRequestMethod;
import org.openmuc.jsonpath.request.HttpRequestParameters;
import org.openmuc.jsonpath.request.json.JsonResponse;
import org.openmuc.solaredge.config.SolarEdgeConfig;
import org.openmuc.solaredge.config.SolarEdgeConst;
import org.openmuc.solaredge.data.TimeWrapper;
import org.openmuc.solaredge.parameters.DataParameters;
import org.openmuc.solaredge.parameters.EnergyDataParameters;
import org.openmuc.solaredge.parameters.EnergyDetailsParameters;
import org.openmuc.solaredge.parameters.PowerDataParameters;
import org.openmuc.solaredge.parameters.PowerDetailsParameters;
import org.openmuc.solaredge.parameters.SiteSensorsParameters;
import org.openmuc.solaredge.parameters.SolarEdgeParameters;
import org.openmuc.solaredge.parameters.StorageDataParameters;
import org.openmuc.solaredge.parameters.TimeFrameEnergyParameters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * We only support the dots (".") notation for JsonPath and also
 * you need to fully qualify the path, because we use the next 
 * path element after root as key to get for example the request path or
 * the parameters.
 * 
 * Important:
 * We do not support the Site Image request and the Installer Logo Image
 * request. We also do not support bulk requests.
 * 
 */
public class SolarEdge {
	private static final Logger logger = LoggerFactory.getLogger(SolarEdge.class);

	protected int siteId;
	protected TimeZone siteZone;
	protected HttpHandler handler;
	protected HttpRequest request;

	protected Map<ResponseMapKey, JsonResponse> responseMap = new HashMap<ResponseMapKey, JsonResponse>();
	protected Map<ResponseMapKey, TimeWrapper> requestTimeMap = new HashMap<ResponseMapKey, TimeWrapper>();

	protected SolarEdge() {
	}

	public SolarEdge(int siteId, SolarEdgeConfig config) throws Exception {
		this.siteId = siteId;
		this.handler = SolarEdgeFactory.getHttpFactory().newAuthenticatedConnection(config);
		this.handler.start();
		
		TimeValue timeValue = getTimeValuePair(SolarEdgeConst.REQUEST_VALUE_PATH_MAP.get("details timeZone"), null, "YEAR", null);
		siteZone = TimeZone.getTimeZone((String) timeValue.getValue());
	}

	public void stop() {
		handler.stop();
	}

	public HttpHandler getHttpHandler() {
		return handler;
	}

	public void setHttpHandler(HttpHandler httpHandler) {
		this.handler = httpHandler;
	}

	public int getSiteId() {
		return siteId;
	}

	public void setSiteId(int siteId) {
		this.siteId = siteId;
	}

	public TimeZone getSiteZone() {
		return siteZone;
	}

	public void setSiteZone(TimeZone siteZone) {
		this.siteZone = siteZone;
	}

	public HttpRequest getRequest() {
		return request;
	}

	public TimeValue getTimeValuePair(String valuePath, String timePath, final String timeUnit,
			final String serialNumber) throws Exception {
		valuePath = replaceSerialNumberInPath(valuePath, serialNumber);
		timePath = replaceSerialNumberInPath(timePath, serialNumber);
		TimeValue timeValuePair = null;
		String requestKey = getRequestKey(valuePath);
		ResponseMapKey responseMapKey = createResponseMapKey(requestKey, timeUnit);
		JsonResponse response = responseMap.get(responseMapKey);
		TimeWrapper time = requestTimeMap.get(responseMapKey);
		if (time == null) {
			time = new TimeWrapper(System.currentTimeMillis(), SolarEdgeConst.TIME_FORMAT, siteZone);
		}
		if (response == null || 
			isOlderThanLastTimeUnit(time.getTime(), 
					SolarEdgeConst.TIME_UNIT_MAP.get(timeUnit))) {			
			// Send Request and get record from response
			String requestPath = getRequestPath(requestKey, serialNumber);
			// Attention: Member time is refreshed in this method getParameters
			SolarEdgeParameters params = getParameters(requestKey, timeUnit, time);
			if (params.getTimeWrapperNow()!=null) {
				time = params.getTimeWrapperNow();
			}
			requestTimeMap.put(responseMapKey, time);
			HttpRequestParameters parameters = params.getParameters();
			
			try {
				HttpRequestAction action = new HttpRequestAction("");
				logger.debug("Request {}, parameters {}", requestPath, parameters);
				request = handler.getRequest(requestPath, action, 
						parameters, HttpRequestMethod.GET);
//				SolarEdgeRequestCounter.augmentCounter(System.currentTimeMillis());
				response = handler.onRequest(request);
			}
			catch (Exception ex) {
				throw ex;
//				return new TimeValue(null, time.getTime());
			}
			timeValuePair = getTimeValuePair(response, valuePath, timePath, time.getTime());
			if (timeValuePair.getValue() != null) {
				responseMap.put(responseMapKey, response);
			}
			return timeValuePair;
		}
		timeValuePair = getTimeValuePair(response, valuePath, timePath, time.getTime());
		return timeValuePair;
	}
	
	protected TimeValue getTimeValuePair(JsonResponse response, String valuePath, String timePath, Long time) 
			throws ParseException, HttpGeneralException {
		if (timePath != null) {
			return response.getTimeValueWithTimePath(valuePath, timePath, SolarEdgeConst.TIME_FORMAT, siteZone);
		}
		else {				
			return response.getTimeValueWithTime(valuePath, time);
		}
		
	}
	
	protected SolarEdgeParameters getParameters(String requestKey, String timeUnit, TimeWrapper time) throws ParseException {
		SolarEdgeParameters params;
		if (requestKey.equals("energy")) {
			params = new EnergyDataParameters(time, timeUnit);
		}
		else if (requestKey.equals("timeFrameEnergy")) {
			params = new TimeFrameEnergyParameters(time);
		}
		else if (requestKey.equals("power")) {
			params = new PowerDataParameters(time);
		}
		else if (requestKey.equals("powerDetails")) {
			params = new PowerDetailsParameters(time);
		}
		else if (requestKey.equals("energyDetails")) {
			params = new EnergyDetailsParameters(time, timeUnit);
		}
		else if (requestKey.equals("storageData")) {
			params = new StorageDataParameters(time, timeUnit);
		}
		else if (requestKey.equals("data")) {
			params = new DataParameters(time, timeUnit);
		}
		else if (requestKey.equals("siteSensors")) {
			params = new SiteSensorsParameters(time, timeUnit);
		}
		else {
			// used for Sites-, details-, dataPeriod-, overview-, 
			// siteCurrentPowerFlow-, envBenefits, ChangeLog-, accounts-, 
			// list-, Inventory-, SiteSensors-, version-, supported-requests 
			params = new SolarEdgeParameters(siteZone);
		}
		params.addParameters();
		return params;
	}
	
	protected String getRequestKey(String valuePath) {
		StringTokenizer tokenizer = new StringTokenizer(valuePath, ".");
		tokenizer.nextToken();
		return tokenizer.nextToken();
	}

	private String getRequestPath(String requestKey, String serialNumber) {
		String requestPath = replaceSiteId(SolarEdgeConst.REQUEST_PATH_MAP.get(requestKey)); 
		requestPath = replaceSerialNumber(requestPath, serialNumber);
		return requestPath;
	}

	private String replaceSiteId(String string) {
		String retval = string;
		if (string.contains(SolarEdgeConst.SITE_ID)) {
			retval = string.replaceAll(SolarEdgeConst.SITE_ID, Integer.toString(siteId));
		}
		return retval;
	}

	private String replaceSerialNumber(String string, String serialNumber) {
		String retval = string;
		if (string.contains(SolarEdgeConst.SERIAL_NUMBER)) {
			retval = string.replaceAll(SolarEdgeConst.SERIAL_NUMBER, serialNumber);
		}
		return retval;
	}
	
	private String replaceSerialNumberInPath(String path, String serialNumber) {
		String retval = path;
		if (path != null && path.contains(SolarEdgeConst.SERIAL_NUMBER)) {
			retval = path.replaceAll(SolarEdgeConst.SERIAL_NUMBER, serialNumber);
		}
		return retval;
	}

	private boolean isOlderThanLastTimeUnit(long lastTime, long timeUnit) {
		long currentTime = System.currentTimeMillis();
		return lastTime + timeUnit < currentTime;
	}
	
	protected ResponseMapKey createResponseMapKey(String requestKey, String timeUnit) {
		return new ResponseMapKey(requestKey, timeUnit);
	}
	
	class ResponseMapKey {
		
		String requestKey;
		String timeUnit;

		public ResponseMapKey(String requestKey, String timeUnit) {
			if (requestKey == null || timeUnit == null) {
				throw new InvalidParameterException();
			}
			this.requestKey = requestKey;
			this.timeUnit = timeUnit;
		}
		
		@Override
		public boolean equals(Object obj) {
			boolean retval = false;
			
			if (obj instanceof ResponseMapKey) {
				ResponseMapKey key = (ResponseMapKey)obj;
				retval = this.requestKey.equals(key.requestKey);
				if (retval) {
					retval = this.timeUnit.equals(key.timeUnit);
				}
			}						
			return retval;
		}
		@Override
		public int hashCode() {
			String str = this.requestKey + this.timeUnit;
			return str.hashCode();			
		}
	}
}
