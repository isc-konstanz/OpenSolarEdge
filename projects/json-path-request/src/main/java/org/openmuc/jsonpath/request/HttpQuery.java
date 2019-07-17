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

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

public class HttpQuery extends LinkedHashMap<String, String>{
	private static final long serialVersionUID = 7622558815668412483L;

	private final String query;

	private HttpQuery(String query) {
		super();
		this.query = query;
	}

	public HttpQuery() {
		this("");
	}

	public HttpQuery(String ... q) {
		this(String.join("/", q));
	}

	public void addParameter(String key, JsonBuilder value) {
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

	public String parse(Charset charset) throws UnsupportedEncodingException {
		StringBuilder uriBuilder = new StringBuilder();
		uriBuilder.append(query);
		if (size() > 0) {
			uriBuilder.append('?');
		}
		
		Iterator<Map.Entry<String, String>> iteratorParameterList = super.entrySet().iterator();
		while (iteratorParameterList.hasNext()) {
			Map.Entry<String, String> parameter = iteratorParameterList.next();
			
			uriBuilder.append(URLEncoder.encode(parameter.getKey(), charset.name()));
			uriBuilder.append('=');
			uriBuilder.append(URLEncoder.encode(parameter.getValue(), charset.name()));
			
			if (iteratorParameterList.hasNext()) {
				uriBuilder.append('&');
			}
		}
		return uriBuilder.toString();
	}

	@Override
	public String toString() {
		try {
			return parse(StandardCharsets.UTF_8);
			
		} catch (UnsupportedEncodingException e) {
		}
		return null;
	}
}
