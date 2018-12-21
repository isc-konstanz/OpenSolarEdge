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
import java.util.Iterator;
import java.util.Map;
import java.util.StringTokenizer;

import org.openmuc.solaredge.data.TimeWrapper;
import org.openmuc.solaredge.parameters.SolarEdgeDataParameters;
import org.openmuc.solaredge.parameters.SolarEdgeEnergyDetailsParameters;
import org.openmuc.solaredge.parameters.SolarEdgeEnergyParameters;
import org.openmuc.solaredge.parameters.SolarEdgeParameters;
import org.openmuc.solaredge.parameters.SolarEdgePowerDetailsParameters;
import org.openmuc.solaredge.parameters.SolarEdgePowerParameters;
import org.openmuc.solaredge.parameters.SolarEdgeSiteSonsorsParameters;
import org.openmuc.solaredge.parameters.SolarEdgeStartEndTimeParameters;
import org.openmuc.solaredge.parameters.SolarEdgeStorageDataParameters;
import org.openmuc.solaredge.parameters.SolarEdgeTimeFrameEnergyParameters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.openmuc.jsonpath.HttpHandler;
import org.openmuc.jsonpath.data.TimeValue;
import org.openmuc.jsonpath.request.HttpRequest;
import org.openmuc.jsonpath.request.HttpRequestAction;
import org.openmuc.jsonpath.request.HttpRequestMethod;
import org.openmuc.jsonpath.request.HttpRequestParameters;
import org.openmuc.jsonpath.request.json.JsonResponse;

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
public class SolarEdgeResponseHandler {
	private static final Logger logger = LoggerFactory.getLogger(SolarEdgeResponseHandler.class);
			
	private int siteId;
	private HttpHandler httpHandler;
	private HttpRequest request;
	private Map<ResponseMapKey, JsonResponse> responseMap = new HashMap<ResponseMapKey, JsonResponse>();
	protected TimeWrapper time = new TimeWrapper(System.currentTimeMillis(), SolarEdgeConst.TIME_FORMAT);
	protected TimeWrapper lastTime;
	
	protected SolarEdgeResponseHandler() {
	}
	
	public SolarEdgeResponseHandler(int siteId, HttpHandler httpHandler) {
		this.siteId = siteId;
		this.httpHandler = httpHandler;
	}
	
	public int getSiteId() {
		return siteId;
	}

	protected void setSiteId(int siteId) {
		this.siteId = siteId;
	}

	protected HttpHandler getHttpHandler() {
		return httpHandler;
	}

	protected void setHttpHandler(HttpHandler httpHandler) {
		this.httpHandler = httpHandler;
	}
	
	public HttpRequest getRequest() {
		return request;
	}

	public TimeValue getTimeValuePair(String valuePath, String timePath, String timeUnit,
			String serialNumber) throws ParseException {
		valuePath = replaceSerialNumberInPath(valuePath, serialNumber);
		timePath = replaceSerialNumberInPath(timePath, serialNumber);
		TimeValue timeValuePair = null;
		String requestKey = getRequestKey(valuePath);
		ResponseMapKey responseMapKey = new ResponseMapKey(requestKey, time, timeUnit);
		JsonResponse response = responseMap.get(responseMapKey);
		if (response == null || 
			isOlderThanLastTimeUnit(time.getTime(), 
					SolarEdgeConst.TIME_UNIT_MAP.get(timeUnit))) {			
			// Send Request and get record from response
			String requestPath = getRequestPath(requestKey, serialNumber);
			// Attention: Member time is refreshed in this method getParameters
			HttpRequestParameters parameters = 
					getParameters(requestKey, timeUnit);
			
			try {
				HttpRequestAction action = new HttpRequestAction("");
				logger.debug("Request {}, parameters {}", requestPath, parameters);
				request = httpHandler.getRequest(requestPath, action, 
						parameters, HttpRequestMethod.GET);
				response = httpHandler.onRequest(request);
			}
			catch (Exception ex) {
				logger.error(ex.toString());
				return new TimeValue(null, time.getTime());
			}
			removeLastResponseFromResponseMap(responseMapKey);
			timeValuePair = getTimeValuePair(response, valuePath, timePath);
			if (timeValuePair.getValue() != null) {
				//Attention: For time see method getParameters
				responseMapKey.endTime = time;
				responseMap.put(responseMapKey, response);
			}
			return timeValuePair;
		}
		timeValuePair = getTimeValuePair(response, valuePath, timePath);
		return timeValuePair;
	}
	
	private TimeValue getTimeValuePair(JsonResponse response, String valuePath, String timePath) throws ParseException {
		if (timePath != null) {
			return response.getTimeValueWithTimePath(valuePath, timePath, 
					SolarEdgeConst.TIME_FORMAT);
		}
		else {				
			return response.getTimeValueWithTime(valuePath, time.getTime());
		}
		
	}
	
	private void removeLastResponseFromResponseMap(ResponseMapKey responseMapKey) {
		Iterator<ResponseMapKey> it = responseMap.keySet().iterator();
		while (it.hasNext()) {
			ResponseMapKey next = it.next();
			if (responseMapKey.requestKey.equals(next.requestKey) &&
				responseMapKey.timeUnit.equals(next.timeUnit)) {
				responseMap.remove(next);
				return;
			}			
		}
	}
	
	private HttpRequestParameters getParameters(String requestKey, String timeUnit) throws ParseException {
		SolarEdgeParameters params;
		if (requestKey.equals("energy")) {
			params = new SolarEdgeEnergyParameters(time, timeUnit);
		}
		else if (requestKey.equals("timeFrameEnergy")) {
			params = new SolarEdgeTimeFrameEnergyParameters(time);
		}
		else if (requestKey.equals("power")) {
			params = new SolarEdgePowerParameters(time);
		}
		else if (requestKey.equals("powerDetails")) {
			params = new SolarEdgePowerDetailsParameters(time);
		}
		else if (requestKey.equals("energyDetails")) {
			params = new SolarEdgeEnergyDetailsParameters(time, timeUnit);
		}
		else if (requestKey.equals("storageData")) {
			params = new SolarEdgeStorageDataParameters(time, timeUnit);
		}
		else if (requestKey.equals("data")) {
			params = new SolarEdgeDataParameters(time, timeUnit);
		}
		else if (requestKey.equals("siteSensors")) {
			params = new SolarEdgeSiteSonsorsParameters(time, timeUnit);
		}
		else {
			// used for Sites-, details-, dataPeriod-, overview-, 
			// siteCurrentPowerFlow-, envBenefits, ChangeLog-, accounts-, 
			// list-, Inventory-, SiteSensors-, version-, supported-requests 
			params = new SolarEdgeParameters();
		}
		HttpRequestParameters parameters = params.getParameters();
		if (params instanceof SolarEdgeStartEndTimeParameters) {
			lastTime = ((SolarEdgeStartEndTimeParameters)params).getLastTime();
		}
		else {
			lastTime = time;
		}			
		time = params.getTimeWrapperNow();
		
		return parameters;
	}
	
	private String getRequestKey(String valuePath) {
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
	
	class ResponseMapKey {
		
		String requestKey;
		TimeWrapper endTime;
		String timeUnit;

		public ResponseMapKey(String requestKey, TimeWrapper endTime, String timeUnit) {
			if (requestKey == null || endTime == null || timeUnit == null) {
				throw new InvalidParameterException();
			}
			this.requestKey = requestKey;
			this.endTime = endTime;
			this.timeUnit = timeUnit;
		}
		
		@Override
		public boolean equals(Object obj) {
			boolean retval = false;
			
			if (obj instanceof ResponseMapKey) {
				ResponseMapKey key = (ResponseMapKey)obj;
				retval = this.requestKey.equals(key.requestKey);
				if (retval) {
					retval = this.endTime.equals(key.endTime);
					if (retval) {
						retval = this.timeUnit.equals(key.timeUnit);
					}
				}
			}						
			return retval;
		}
		@Override
		public int hashCode() {
			String str = this.requestKey + endTime.getTimeStr() + this.timeUnit;
			return str.hashCode();			
		}
	}
}
