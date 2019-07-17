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

public class HttpConfig {

	public final static int MAX_THREADS_DEFAULT = 1;

	private String url;
	private String apiKey;
	private int maxThreads = 1;

	public HttpConfig(String url, String apiKey) {
		this.url = url;
		this.apiKey = apiKey;
	}

	public HttpConfig(String url, String apiKey, int maxThreads) {
		this.url = url;
		this.apiKey = apiKey;
		this.maxThreads = maxThreads;
	}

	public String getAddress() {
		return url;
	}

	public String getApiKey() {
		return apiKey;
	}

	public int getMaxThreads() {
		if (maxThreads > 0) {
			return maxThreads;
		}
		return MAX_THREADS_DEFAULT;
	}
}
