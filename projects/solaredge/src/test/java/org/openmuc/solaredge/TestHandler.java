package org.openmuc.solaredge;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.ParseException;
import java.util.StringTokenizer;
import java.util.TimeZone;

import org.openmuc.jsonpath.HttpHandler;
import org.openmuc.jsonpath.data.TimeValue;
import org.openmuc.solaredge.config.SolarEdgeConst;
import org.openmuc.solaredge.data.TimeWrapper;
import org.openmuc.solaredge.parameters.SolarEdgeParameters;
import org.openmuc.solaredge.parameters.TimeParameters;

public class TestHandler extends SolarEdge {

	private ResponseMapKey key = null;
	private TimeWrapper time;

	public TestHandler(int siteId, HttpHandler handler) throws Exception {
		this.siteId = siteId;
		this.handler = handler;
		this.handler.start();
		
		TimeValue timeValue = getTimeValuePair(SolarEdgeConst.REQUEST_VALUE_PATH_MAP.get("details timeZone"), null, "YEAR", null);
		siteZone = TimeZone.getTimeZone((String) timeValue.getValue());
	}

	@Override
	protected ResponseMapKey createResponseMapKey(String requestKey, String timeUnit) {
		key = super.createResponseMapKey(requestKey, timeUnit);
		return key;
	}

	public TimeWrapper getTimeWrapper() {
		TimeWrapper time = requestTimeMap.get(key);
		if (time == null) {
			time = new TimeWrapper(System.currentTimeMillis(), SolarEdgeConst.TIME_FORMAT, siteZone);
		}
		return time;
	}

	public TimeWrapper getLastTime() {
		return time;
	}

	public String fillRequest(String request, String timeUnit, String authenticiation) {
		StringTokenizer tokenizer = new StringTokenizer(request, "=");
		String retVal = tokenizer.nextToken() + "=" + addStartTime();
		retVal += tokenizer.nextToken() + "=" + addEndTime();
		if (timeUnit != null) {
			retVal += tokenizer.nextToken() + "=" + timeUnit;
		}
		retVal += tokenizer.nextToken() + "=" + authenticiation;
		return retVal;
	}

	public String fillParameters(String parameters, String timeUnit) {
		StringTokenizer tokenizer = new StringTokenizer(parameters, "=");
		String retVal = tokenizer.nextToken() + "=" + addStartTime();
		retVal += tokenizer.nextToken() + "=" + addEndTime();
		if (timeUnit != null) {
			retVal += tokenizer.nextToken() + "=" + timeUnit;
		}
		//retVal += tokenizer.nextToken() + "=" + getAuthenticiation();
		return retVal;
	}

	@Override
	protected SolarEdgeParameters getParameters(String requestKey, String timeUnit, TimeWrapper time) throws ParseException {
		SolarEdgeParameters parameters = super.getParameters(requestKey, timeUnit, time);
		if (parameters instanceof TimeParameters) {
	        this.time = ((TimeParameters) parameters).getLastTime();
	    }
	    else {
	        this.time = time;
	    }
		return parameters;
	}

	private String addEndTime() {
		String t = this.getTimeWrapper().getTimeStr();
		try {
			t = URLEncoder.encode(t, SolarEdgeConst.CHARSET.name());
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return t;
	}

	private String addStartTime() {
		String t = time.getTimeStr();
		try {
			t = URLEncoder.encode(t, SolarEdgeConst.CHARSET.name());
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return t;
	}
}
