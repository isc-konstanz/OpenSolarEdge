package org.openmuc.solaredge;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.StringTokenizer;

import org.openmuc.jsonpath.HttpHandler;
import org.openmuc.solaredge.data.TimeWrapper;

public class TestSolarEdgeResponseHandler extends SolarEdgeResponseHandler {
	
	private ResponseMapKey key = null;
	
	public TestSolarEdgeResponseHandler(int siteId, HttpHandler httpHandler) {
		super(siteId, httpHandler);
	}
	
	
	@Override
	protected ResponseMapKey createResponseMapKey(String requestKey, String timeUnit) {
		key = super.createResponseMapKey(requestKey, timeUnit);
		return key;
	}
	
	public TimeWrapper getTimeWrapper() {
		TimeWrapper time = requestTimeMap.get(key);
		if (time == null) {
			time = new TimeWrapper(System.currentTimeMillis(), SolarEdgeConst.TIME_FORMAT);
		}
		return time;
	}
	
	public TimeWrapper getLastTime() {
		return lastTime;
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
		String t = lastTime.getTimeStr();
		try {
			t = URLEncoder.encode(t, SolarEdgeConst.CHARSET.name());
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return t;
	}
}
