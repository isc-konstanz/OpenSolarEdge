package org.openmuc.solaredge;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.ParseException;
import java.util.StringTokenizer;
import java.util.TimeZone;

import org.openmuc.jsonpath.TestHttpHandler;
import org.openmuc.jsonpath.data.TimeValue;
import org.openmuc.jsonpath.request.HttpMethod;
import org.openmuc.jsonpath.request.HttpParameters;
import org.openmuc.jsonpath.request.HttpQuery;
import org.openmuc.jsonpath.request.HttpRequest;
import org.openmuc.solaredge.data.TimeWrapper;
import org.openmuc.solaredge.parameters.SolarEdgeParameters;
import org.openmuc.solaredge.parameters.TimeParameters;

public class TestHandler extends SolarEdge {

	private HttpQuery query;
	private HttpParameters params;
	private HttpResponseKey key;
	private TimeWrapper time;

	public TestHandler(int siteId, TestHttpHandler handler) throws Exception {
		this.siteId = siteId;
		this.handler = handler.open();
		
		TimeValue timeValue = getTimeValue(SolarEdge.REQUEST_PATH_VALUES.get("details timeZone"), null, "YEAR", null);
		siteZone = TimeZone.getTimeZone((String) timeValue.getValue());
	}

	public TestHttpHandler getHttpHandler() {
		return (TestHttpHandler) handler;
	}

	public TimeWrapper getTimeWrapper() {
		TimeWrapper time = requestTimes.get(key);
		if (time == null) {
			time = new TimeWrapper(System.currentTimeMillis(), SolarEdge.TIME_FORMAT, siteZone);
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
		SolarEdgeParameters params = super.getParameters(requestKey, timeUnit, time);
		if (params instanceof TimeParameters) {
	        this.time = ((TimeParameters) params).getLastTime();
	    }
	    else {
	        this.time = time;
	    }
		this.params = params.getParameters();
		this.key = new HttpResponseKey(requestKey, timeUnit);
		return params;
	}

	@Override
	protected HttpQuery getQuery(String key, String serialNumber) {
		HttpQuery query = super.getQuery(key, serialNumber);
		this.query = query;
		return query;
	}

	public HttpRequest getRequest() {
		return new HttpRequest(HttpMethod.GET, handler.getAddress(), query, params, handler.getAuthentication());
	}

	private String addEndTime() {
		String t = this.getTimeWrapper().getTimeStr();
		try {
			t = URLEncoder.encode(t, SolarEdge.CHARSET.name());
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return t;
	}

	private String addStartTime() {
		String t = time.getTimeStr();
		try {
			t = URLEncoder.encode(t, SolarEdge.CHARSET.name());
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return t;
	}
}
