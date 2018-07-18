/* 
 * Copyright 2016-17 ISC Konstanz
 * 
 * This file is part of emonjava.
 * For more information visit https://github.com/isc-konstanz/emonjava
 * 
 * Emonjava is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * Emonjava is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with emonjava.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.openmuc.http.request;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

import org.openmuc.http.request.json.ToJsonObject;

public class HttpRequestParameters extends LinkedHashMap<String, String> {
	
	private static final long serialVersionUID = 4688227144913816378L;

	public HttpRequestParameters() {
		super();
	}
	
	public void addParameter(String key, ToJsonObject value) {
		super.put(key, value.toString());
	}
	
	public void addParameter(String key, String value) {
		super.put(key, value);
	}
	
	public void addParameter(String key, double value) {
		super.put(key, String.valueOf(value));
	}
	
	public void addParameter(String key, long value) {
		super.put(key, String.valueOf(value));
	}
	
	public void addParameter(String key, int value) {
		super.put(key, String.valueOf(value));
	}
	
	public void addParameter(String key, boolean value) {
		super.put(key, String.valueOf(value));
	}
	
	public String parseParameters(Charset charset) throws UnsupportedEncodingException {
		StringBuilder parameterListBuilder = new StringBuilder();

		Iterator<Map.Entry<String, String>> iteratorParameterList = super.entrySet().iterator();
		while (iteratorParameterList.hasNext()) {
			Map.Entry<String, String> parameter = iteratorParameterList.next();
			
			parameterListBuilder.append(URLEncoder.encode(parameter.getKey(), charset.name()));
			parameterListBuilder.append('=');
			parameterListBuilder.append(URLEncoder.encode(parameter.getValue(), charset.name()));

			if (iteratorParameterList.hasNext()) {
				parameterListBuilder.append('&');
			}
		}
		
		return parameterListBuilder.toString();
	}

	@Override
	public String toString() {
		StringBuilder parameterListBuilder = new StringBuilder();

		Iterator<Map.Entry<String, String>> iteratorParameterList = super.entrySet().iterator();
		while (iteratorParameterList.hasNext()) {
			Map.Entry<String, String> parameter = iteratorParameterList.next();
			
			parameterListBuilder.append(parameter.getKey());
			parameterListBuilder.append('=');
			parameterListBuilder.append(parameter.getValue());

			if (iteratorParameterList.hasNext()) {
				parameterListBuilder.append('&');
			}
		}
		
		return parameterListBuilder.toString();
	}
}
