/* 
 * Copyright 2016-19 ISC Konstanz
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

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.TimeZone;

import org.openmuc.jsonpath.HttpConnection;
import org.openmuc.jsonpath.HttpException;
import org.openmuc.jsonpath.HttpFactory;
import org.openmuc.jsonpath.data.TimeValue;
import org.openmuc.jsonpath.request.HttpQuery;
import org.openmuc.jsonpath.request.HttpResponse;
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

	public static Map<String, String> REQUEST_PATHS = new HashMap<String, String>() {
		private static final long serialVersionUID = 7565668512142972983L;
		{
			put("Sites", 			"sites/list");
			put("details", 			"site/@siteId/details");
			put("dataPeriod", 		"site/@siteId/dataPeriod");
			put("energy", 			"site/@siteId/energy");
			put("timeFrameEnergy", 	"site/@siteId/timeFrameEnergy");
			put("power", 			"site/@siteId/power");
			put("overview", 		"site/@siteId/overview");
			put("powerDetails", 	"site/@siteId/powerDetails");
			put("energyDetails", 	"site/@siteId/energyDetails");
			put("siteCurrentPowerFlow", "site/@siteId/currentPowerFlow");
			put("storageData", 		"site/@siteId/storageData");
			put("envBenefits", 		"site/@siteId/envBenefits");
			put("list", 			"equipment/@siteId/list");
			put("Inventory", 		"site/@siteId/Inventory");
			put("data", 			"equipment/@siteId/@serialNumber/data");
			put("ChangeLog", 		"equipment/@siteId/@serialNumber/changeLog");
			put("accounts", 		"accounts/list");
			put("SiteSensors", 		"equipment/@siteId/sensors");
			put("siteSensors", 		"site/@siteId/sensors");
			put("version", 			"version/current");
			put("supported", 		"version/supported");
		}
	};

	public static Map<String, String> REQUEST_PATH_VALUES = new HashMap<String, String>() {
		private static final long serialVersionUID = -5149912712569500179L;
		{
			put("details name", 					"$.details.name");
			put("details timeZone", 				"$.details.location.timeZone");
			put("energyDetails Production", 		"$.energyDetails.meters[?(@.type=='Production')].values[-1].value");
			put("energyDetails Consumption", 		"$.energyDetails.meters[?(@.type=='Consumption')].values[-1].value");
			put("energyDetails SelfConsumption", 	"$.energyDetails.meters[?(@.type=='SelfConsumption')].values[-1].value");
			put("energyDetails FeedIn", 			"$.energyDetails.meters[?(@.type=='FeedIn')].values[-1].value");
			put("energyDetails Purchased", 			"$.energyDetails.meters[?(@.type=='Purchased')].values[-1].value");
			put("powerDetails Production", 			"$.powerDetails.meters[?(@.type=='Production')].values[-1].value");
			put("powerDetails Consumption", 		"$.powerDetails.meters[?(@.type=='Consumption')].values[-1].value");
			put("powerDetails SelfConsumption", 	"$.powerDetails.meters[?(@.type=='SelfConsumption')].values[-1].value");
			put("powerDetails FeedIn", 				"$.powerDetails.meters[?(@.type=='FeedIn')].values[-1].value");
			put("powerDetails Purchased", 			"$.powerDetails.meters[?(@.type=='Purchased')].values[-1].value");
			put("storageData power", 				"$.storageData.batteries[0].telemetries[-1].power");
			put("storageData batteryState", 		"$.storageData.batteries[0].telemetries[-1].batteryState");
			put("storageData lifeTimeEnergyDischarged", "$.storageData.batteries[0].telemetries[-1].lifeTimeEnergyDischarged");
			put("storageData batteryPercentageState", "$.storageData.batteries[0].telemetries[-1].batteryPercentageState");
		}
	};

	public static Map<String, String> REQUEST_PATH_TIMES = new HashMap<String, String>() {
		private static final long serialVersionUID = 6752201577661088822L;
		{
			put("energyDetails Production", 		"$.energyDetails.meters[?(@.type=='Production')].values[-1].date");
			put("energyDetails Consumption", 		"$.energyDetails.meters[?(@.type=='Consumption')].values[-1].date");
			put("energyDetails SelfConsumption", 	"$.energyDetails.meters[?(@.type=='SelfConsumption')].values[-1].date");
			put("energyDetails FeedIn", 			"$.energyDetails.meters[?(@.type=='FeedIn')].values[-1].date");
			put("energyDetails Purchased", 			"$.energyDetails.meters[?(@.type=='Purchased')].values[-1].date");
			put("powerDetails Production", 			"$.powerDetails.meters[?(@.type=='Production')].values[-1].date");
			put("powerDetails Consumption", 		"$.powerDetails.meters[?(@.type=='Consumption')].values[-1].date");
			put("powerDetails SelfConsumption", 	"$.powerDetails.meters[?(@.type=='SelfConsumption')].values[-1].date");
			put("powerDetails FeedIn", 				"$.powerDetails.meters[?(@.type=='FeedIn')].values[-1].date");
			put("powerDetails Purchased", 			"$.powerDetails.meters[?(@.type=='Purchased')].values[-1].date");
			put("storageData power", 				"$.storageData.batteries[0].telemetries[-1].timeStamp");
			put("storageData batteryState", 		"$.storageData.batteries[0].telemetries[-1].timeStamp");
			put("storageData lifeTimeEnergyDischarged", "$.storageData.batteries[0].telemetries[-1].timeStamp");
			put("storageData batteryPercentageState","$.storageData.batteries[0].telemetries[-1].timeStamp");
		}
	};

	public static String QUARTER_OF_AN_HOUR = "QUARTER_OF_AN_HOUR";

	@SuppressWarnings("deprecation")
	public static Map<String, Long> TIME_UNITS = new HashMap<String, Long>() {
		private static final long serialVersionUID = -7142548851038714457L;
		{
			put("FIVE_MINUTE", new Long(5*60*1000));
			put(QUARTER_OF_AN_HOUR, new Long(15*60*1000));
			put("HALF_OF_AN_HOUR", new Long(30*60*1000));
			put("HOUR", new Long(60*60*1000));
			put("DAY", new Long(24*60*60*1000));
			put("WEEK", new Long((long)7*24*60*60*1000));
			put("MONTH", new Long((long)31*24*60*60*1000));
			put("YEAR", new Long((long)365*24*60*60*1000));
		}
	};

	public static String SITE_ID = "@siteId";
	public static String SERIAL_NUMBER = "@serialNumber";

	public static String TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
	public static String DATE_FORMAT = "yyyy-MM-dd";

	public final static Charset CHARSET = StandardCharsets.UTF_8;

	protected int siteId;
	protected TimeZone siteZone;
	protected HttpConnection handler;

	protected Map<HttpResponseKey, HttpResponse> responses = new HashMap<HttpResponseKey, HttpResponse>();
	protected Map<HttpResponseKey, TimeWrapper> requestTimes = new HashMap<HttpResponseKey, TimeWrapper>();

	protected SolarEdge() {
	}

	public SolarEdge(int siteId, String address, String apiKey) throws Exception {
		this.siteId = siteId;
		this.handler = HttpFactory.getHttpFactory().newConnection(address, apiKey).open();
		
		TimeValue timeValue = getTimeValue(SolarEdge.REQUEST_PATH_VALUES.get("details timeZone"), null, "YEAR", null);
		siteZone = TimeZone.getTimeZone((String) timeValue.getValue());
	}

	public void close() {
		handler.close();
	}

	public SolarEdge setHttpHandler(HttpConnection httpHandler) {
		this.handler = httpHandler;
		return this;
	}

	public int getSiteId() {
		return siteId;
	}

	public SolarEdge setSiteId(int siteId) {
		this.siteId = siteId;
		return this;
	}

	public TimeZone getSiteZone() {
		return siteZone;
	}

	public SolarEdge setSiteZone(TimeZone siteZone) {
		this.siteZone = siteZone;
		return this;
	}

	public TimeValue getTimeValue(String valuePath, String timePath, final String timeUnit,
			final String serialNumber) throws Exception {
		valuePath = replaceSerialNumber(valuePath, serialNumber);
		timePath = replaceSerialNumber(timePath, serialNumber);
		String requestKey = getRequestKey(valuePath);
		
		HttpResponseKey responseKey = new HttpResponseKey(requestKey, timeUnit);
		HttpResponse response = responses.get(responseKey);
		TimeWrapper time = requestTimes.get(responseKey);
		TimeValue timeValue = null;
		if (time == null) {
			time = new TimeWrapper(System.currentTimeMillis(), SolarEdge.TIME_FORMAT, siteZone);
		}
		if (response == null || isOlderThan(time.getTime(), SolarEdge.TIME_UNITS.get(timeUnit))) {
			// Attention: Member time is refreshed in this method getParameters
			SolarEdgeParameters parameters = getParameters(requestKey, timeUnit, time);
			if (parameters.getTimeWrapperNow() != null) {
				time = parameters.getTimeWrapperNow();
			}
			requestTimes.put(responseKey, time);
			
			try {
				response = handler.get(getQuery(requestKey, serialNumber), parameters.getParameters());
			}
			catch (Exception ex) {
				throw ex;
			}
			timeValue = getTimeValue(response, valuePath, timePath, time.getTime());
			if (timeValue.getValue() != null) {
				responses.put(responseKey, response);
			}
			return timeValue;
		}
		timeValue = getTimeValue(response, valuePath, timePath, time.getTime());
		return timeValue;
	}

	protected TimeValue getTimeValue(HttpResponse response, String valuePath, String timePath, Long time) 
			throws ParseException, HttpException {
		if (timePath != null) {
			return response.getTimeValue(valuePath, timePath, SolarEdge.TIME_FORMAT, siteZone);
		}
		else {
			return response.getTimeValue(valuePath, time);
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

	protected HttpQuery getQuery(String key, String serialNumber) {
		String path = replaceSerialNumber(replaceSiteId(SolarEdge.REQUEST_PATHS.get(key)), serialNumber);
		return new HttpQuery(path);
	}

	private String replaceSiteId(String string) {
		String retval = string;
		if (string.contains(SolarEdge.SITE_ID)) {
			retval = string.replaceAll(SolarEdge.SITE_ID, Integer.toString(siteId));
		}
		return retval;
	}

	private String replaceSerialNumber(String string, String serialNumber) {
		String retval = string;
		if (string != null && string.contains(SolarEdge.SERIAL_NUMBER)) {
			retval = string.replaceAll(SolarEdge.SERIAL_NUMBER, serialNumber);
		}
		return retval;
	}

	private boolean isOlderThan(long lastTime, long timeUnit) {
		long currentTime = System.currentTimeMillis();
		return lastTime + timeUnit < currentTime;
	}

	protected String getRequestKey(String valuePath) {
		StringTokenizer tokenizer = new StringTokenizer(valuePath, ".");
		tokenizer.nextToken();
		return tokenizer.nextToken();
	}

	class HttpResponseKey {
		private String key;
		private String time;
		
		public HttpResponseKey(String key, String timeUnit) {
			if (key == null || timeUnit == null) {
				throw new IllegalArgumentException();
			}
			this.key = key;
			this.time = timeUnit;
		}
		
		@Override
		public boolean equals(Object obj) {
			boolean retval = false;
			
			if (obj instanceof HttpResponseKey) {
				HttpResponseKey key = (HttpResponseKey)obj;
				retval = this.key.equals(key.key);
				if (retval) {
					retval = this.time.equals(key.time);
				}
			}						
			return retval;
		}
		
		@Override
		public int hashCode() {
			String str = this.key + this.time;
			return str.hashCode();			
		}
	}
}
