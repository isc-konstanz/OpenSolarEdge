package org.openmuc.solaredge.parameters;

import java.text.ParseException;

import org.openmuc.solaredge.SolarEdgeConst;
import org.openmuc.solaredge.data.TimeWrapper;
import org.openmuc.http.request.HttpRequestParameters;

public class SolarEdgeParameters {

	protected HttpRequestParameters parameters = new HttpRequestParameters();
	protected TimeWrapper now;
	protected String nowTimeFormat = SolarEdgeConst.TIME_FORMAT;

	public HttpRequestParameters getParameters() throws ParseException {
		addParameters();
		return parameters;
	}
	
	public void addParameters() throws ParseException {
		now = new TimeWrapper(System.currentTimeMillis(), nowTimeFormat);
	}
	
	public TimeWrapper getTimeWrapperNow() {
		return now;
	}
	
	public void setNowTimeFormat(String timeFormat) {
		nowTimeFormat = timeFormat;
	}
}
