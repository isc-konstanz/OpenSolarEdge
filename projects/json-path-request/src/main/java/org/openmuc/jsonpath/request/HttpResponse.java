/* 
 * Copyright 2016-19 ISC Konstanz
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
package org.openmuc.jsonpath.request;

import java.text.ParseException;
import java.util.TimeZone;

import org.openmuc.jsonpath.HttpException;
import org.openmuc.jsonpath.data.TimeConverter;
import org.openmuc.jsonpath.data.TimeValue;

import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;

import net.minidev.json.JSONArray;

public class HttpResponse {

	protected final DocumentContext json;

	public HttpResponse(String response) {
		this.json = JsonPath.parse(response);
	}

	public TimeValue getTimeValue(String valuePath, String timePath, String timeFormat, TimeZone timeZone) throws ParseException, HttpException {
		Long time = getTime(timePath, timeFormat, timeZone);
		Object val = getValue(valuePath);
		return getTimeValue(val, time);
	}

	public TimeValue getTimeValue(String path, Long time) throws HttpException {
		Object val = getValue(path);
		Long myTime= System.currentTimeMillis();
		if (time != null) {
			long endTime = time;
			if (endTime < myTime) myTime = endTime;
		}
		return getTimeValue(val, myTime);
	}

	protected TimeValue getTimeValue(Object val, Long time) {
		return new TimeValue(val, time);		
	}

	public Object getValue(String path) throws HttpException {
		Object obj = null;
		try {
			obj = json.read(path);
		}
		catch (Exception e) {
			RuntimeException re = new RuntimeException(e);
			throw new HttpException("Read path " + path + " failed: " + e, re);
		}
		return getValue(obj);
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

	public Long getTime(String path, String format, TimeZone zone) throws ParseException {
		Object obj = json.read(path);
		if (obj instanceof JSONArray) {
			obj = ((JSONArray)obj).get(0).toString();
		}
		Long time = null;
		time = TimeConverter.decode(obj.toString(), format, zone);
		return time;
	}

	@Override
	public String toString() {
		return json.jsonString();
	}

}
