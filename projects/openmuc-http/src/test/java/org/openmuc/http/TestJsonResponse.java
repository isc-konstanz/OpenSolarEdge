package org.openmuc.http;

import org.openmuc.http.data.TimeValue;
import org.openmuc.http.request.json.JsonResponse;

public class TestJsonResponse extends JsonResponse {

	public TestJsonResponse(String response) {
		super(response);
	}
	
	public TimeValue getTimeValuePair(String valuePath, String timePath, 
			Long time, String timeFormat) {
		TimeValue retVal;
		if (timePath != null) {
			retVal = getTimeValueWithTimePath(valuePath, timePath, timeFormat);
		}
		else {
			retVal = getTimeValueWithTime(valuePath, time);
		}
		return retVal;
	}
}
