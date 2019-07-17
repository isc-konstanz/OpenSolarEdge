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
package org.openmuc.jsonpath;

import java.util.ArrayList;
import java.util.List;

public class HttpFactory {

	protected final List<HttpConnection> httpSingletonList = new ArrayList<HttpConnection>();

	protected final static HttpFactory factory = new HttpFactory();

	public static HttpFactory getHttpFactory() {
		return factory;
	}

	public HttpConnection newConnection(HttpConfig config) {
		return newConnection(config.getAddress(), config.getApiKey(), config.getMaxThreads());
	}

	public HttpConnection newConnection(String address, String apiKey) {
		return newConnection(address, apiKey, null);
	}

	public HttpConnection newConnection(String address, String apiKey, Integer maxThreads) {
		if (address != null) {
			address = verifyAddress(address);
		}
		else {
			return null;
		}
		for (HttpConnection handler : httpSingletonList) {
			if (handler.getAddress().equals(address)) {
				if (!handler.getAuthentication().getApiKey().equals(apiKey)) {
					handler.setAuthentication(apiKey);
				}
				if (maxThreads != null && handler.getMaxThreads() != maxThreads) {
					handler.setMaxThreads(maxThreads);
				}
				return handler;
			}
		}
		if (maxThreads == null) {
			maxThreads = HttpConfig.MAX_THREADS_DEFAULT;
		}
		HttpConnection handler = new HttpConnection(address, apiKey, maxThreads);
		httpSingletonList.add(handler);
		
		return handler;
	}

	public static String verifyAddress(String address) {

		String url;
		if (!(address.startsWith("http://") || address.startsWith("https://"))) {
			url = "http://".concat(address);
		}
		else {
			url = address;
		}
		if (!url.endsWith("/")) {
			url = url.concat("/");
		}
		return url;
	}
}
