package org.openmuc.solaredge.core;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.StringTokenizer;

import org.openmuc.http.TestHttpHandler;
import org.openmuc.solaredge.SolarEdgeConfig;
import org.openmuc.solaredge.SolarEdgeConst;
import org.openmuc.solaredge.SolarEdgeResponseHandler;
import org.openmuc.solaredge.data.TimeWrapper;

public class TestSolarEdgeResponseHandler extends SolarEdgeResponseHandler {

	SolarEdgeConfig config;
	
	public TestSolarEdgeResponseHandler(String jsonString) {
		TestHttpHandler httpHandler;

		config = new SolarEdgeConfig();
			
		httpHandler = TestHttpFactory.newAuthenticatedConnection(config);
		httpHandler.setResponse(jsonString);
		httpHandler.start();
		this.setSiteId(78467);
		this.setHttpHandler(httpHandler);

		System.out.println("JsonString: " + jsonString);
	}
	
	String getAuthenticiation() {
		return config.getAuthentication();
	}

	public TimeWrapper getTimeWrapper() {
		return time;
	}

	public String fillParameters(String parameters, String timeUnit) {
		StringTokenizer tokenizer = new StringTokenizer(parameters, "=");
		String retVal = tokenizer.nextToken() + "=" + addStartTime();
		retVal += tokenizer.nextToken() + "=" + addEndTime();
		retVal += tokenizer.nextToken() + "=" + timeUnit;
		retVal += tokenizer.nextToken() + "=" + getAuthenticiation();
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
