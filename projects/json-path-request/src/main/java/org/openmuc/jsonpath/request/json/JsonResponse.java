/* 
 * Copyright 2016-18 ISC Konstanz
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
package org.openmuc.jsonpath.request.json;

import java.text.ParseException;
import java.util.TimeZone;

import org.openmuc.jsonpath.com.HttpGeneralException;
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
	
	public TimeValue getTimeValueWithTimePath(String valuePath, String timePath, String timeFormat, TimeZone timeZone) throws ParseException, HttpGeneralException {
		Long time = getTimeFromJson(timePath, timeFormat, timeZone);
		Object val = getValue(valuePath);
		return getTimeValuePair(val, time);
	}
	
	public TimeValue getTimeValueWithTime(String valuePath, Long time) throws HttpGeneralException {
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
	
	public Object getValue(String path) throws HttpGeneralException {
		Object obj = null;
		try {
			obj = jsonContext.read(path);
		}
		catch (Exception e) {
			RuntimeException re = new RuntimeException(e);
			throw new HttpGeneralException("Read path " + path + " failed: " + e, re);
		}
		return getValue(obj);
	}
	
	public Long getTimeFromJson(String path, String format, TimeZone zone) throws ParseException {
		Object obj = jsonContext.read(path);
		if (obj instanceof JSONArray) {
			obj = ((JSONArray)obj).get(0).toString();
		}
		Long time = null;
		time = TimeConverter.decode(obj.toString(), format, zone);
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
