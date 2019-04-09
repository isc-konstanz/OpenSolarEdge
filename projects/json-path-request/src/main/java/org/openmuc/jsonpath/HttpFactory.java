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
package org.openmuc.jsonpath;

import java.util.ArrayList;
import java.util.List;

import org.openmuc.jsonpath.data.ApiKeyConst;
import org.openmuc.jsonpath.data.Authentication;
import org.openmuc.jsonpath.data.Authorization;
import org.openmuc.jsonpath.data.Config;



public class HttpFactory {

	protected final List<HttpHandler> httpSingletonList = new ArrayList<HttpHandler>();

	protected final static HttpFactory factory = new HttpFactory();
	
	public static HttpFactory getHttpFactory() {
		return factory;
	}

	public HttpHandler newAuthenticatedConnection(Config config) {
		Authentication credentials = getCredentials(config.getAuthorization(), config.getAuthentication());
		
		return getConnection(config.getUrl(), credentials, config.getMaxThreads());
	}

	private HttpHandler getConnection(String address, Authentication credentials, Integer maxThreads) {
		if (address != null) {
			address = verifyAddress(address);
		}
		else {
			return null;
		}

		for (HttpHandler handler : httpSingletonList) {
			if (handler.getAddress().equals(address)) {
				if (!handler.getAuthentication().equals(credentials)) {
					handler.setAuthentication(credentials);
				}
				if (maxThreads != null && handler.getMaxThreads() != maxThreads) {
					handler.setMaxThreads(maxThreads);
				}
				return handler;
			}
		}
		if (maxThreads == null) {
			maxThreads = Config.MAX_THREADS_DEFAULT;
		}
		HttpHandler handler = new HttpHandler(address, credentials, maxThreads);
		httpSingletonList.add(handler);

		return handler;
	}

	public Authentication getCredentials(String type, String key) {
		
		Authentication authentication = null;
		if (type != null && key != null) {
			Authorization.setApiKey(ApiKeyConst.getApiKeyConst().getKey());
			Authorization authorization = Authorization.valueOf(type);
			authentication = new Authentication(authorization, key);
		}
		return authentication;
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
