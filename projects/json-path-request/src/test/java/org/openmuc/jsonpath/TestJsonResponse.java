package org.openmuc.jsonpath;

import java.text.ParseException;
import java.util.TimeZone;

import org.openmuc.jsonpath.com.HttpGeneralException;
import org.openmuc.jsonpath.data.TimeValue;
import org.openmuc.jsonpath.request.json.JsonResponse;

public class TestJsonResponse extends JsonResponse {

	public TestJsonResponse(String response) {
		super(response);
	}
	
	public TimeValue getTimeValuePair(String valuePath, String timePath, 
			Long time, String timeFormat, TimeZone timeZone) throws ParseException, HttpGeneralException {
		TimeValue retVal;
		if (timePath != null) {
			retVal = getTimeValueWithTimePath(valuePath, timePath, timeFormat, timeZone);
		}
		else {
			retVal = getTimeValueWithTime(valuePath, time);
		}
		return retVal;
	}
}
