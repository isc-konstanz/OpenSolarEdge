package org.openmuc.jsonpath.request.json;

import java.text.ParseException;

import org.openmuc.jsonpath.data.TimeConverter;
import org.openmuc.jsonpath.data.TimeValue;

import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;

import net.minidev.json.JSONArray;

public class JsonResponse {

	protected final DocumentContext jsonContext;
	protected final String response;

	public JsonResponse(String response) {
		this.response = response;
		this.jsonContext = JsonPath.parse(response);
	}
	
	public String getResponse() {
		return response;
	}
	
	public TimeValue getTimeValueWithTimePath(String valuePath, String timePath, String timeFormat) throws ParseException {
		Object val = getValue(valuePath);
		Long time = getTimeFromJson(timePath, timeFormat);
		return getTimeValuePair(val, time);
	}
	
	public TimeValue getTimeValueWithTime(String valuePath, Long time) {
		Object val = getValue(valuePath);
		Long myTime= System.currentTimeMillis();
		if (time != null) {
			long endTime = time;
			if (endTime < myTime) myTime = endTime;
		}
		return getTimeValuePair(val, myTime);
	}
	
	protected TimeValue getTimeValuePair(Object val, Long time) {
		return new TimeValue(val, time);		
	}
	
	public Object getValue(String path) {
		Object obj = null;
		try {
			obj = jsonContext.read(path);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return getValue(obj);
	}
	
	public Long getTimeFromJson(String path, String timeFormat) throws ParseException {
		Object obj = jsonContext.read(path);
		if (obj instanceof JSONArray) {
			obj = ((JSONArray)obj).get(0).toString();
		}
		Long time = null;
		time = TimeConverter.timeStringToTime(obj.toString(),timeFormat);
		return time;
	}
	
	protected Object getValue(Object obj) {
		if (obj instanceof JSONArray) {
			return getValue((JSONArray)obj);
		}
		else if (obj instanceof Boolean || obj instanceof byte[] ||
				 obj instanceof Byte    || obj instanceof Double ||
				 obj instanceof Float   || obj instanceof Integer ||
				 obj instanceof Long    || obj instanceof Short) {
			return obj;
		}
		else if (obj != null) {
			return new String(obj.toString());
		}
		return obj;
	}
	
	protected Object getValue(JSONArray arr) {
		Object val = null;
		
		if (arr.size() == 1) {
			val = getValue(arr.get(0));
		}
		else if (arr.size() > 1) {
			val = new String(arr.toString());
		}
		
		return val;
	}
}
