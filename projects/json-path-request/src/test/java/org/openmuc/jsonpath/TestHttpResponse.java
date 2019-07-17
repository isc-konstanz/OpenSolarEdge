package org.openmuc.jsonpath;

import java.text.ParseException;
import java.util.TimeZone;

import org.openmuc.jsonpath.data.TimeValue;
import org.openmuc.jsonpath.request.HttpResponse;

public class TestHttpResponse extends HttpResponse {

	public TestHttpResponse(String response) {
		super(response);
	}

	public TimeValue getTimeValue(String valuePath, String timePath, 
			Long time, String timeFormat, TimeZone timeZone) throws ParseException, HttpException {
		TimeValue retVal;
		if (timePath != null) {
			retVal = getTimeValue(valuePath, timePath, timeFormat, timeZone);
		}
		else {
			retVal = getTimeValue(valuePath, time);
		}
		return retVal;
	}

}
