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
package org.openmuc.jsonpath.request;

import org.openmuc.jsonpath.HttpHandler;
import org.openmuc.jsonpath.com.HttpGeneralException;
import org.openmuc.jsonpath.data.Authentication;
import org.openmuc.jsonpath.request.json.JsonResponse;

/**
 * Interface used to notify the {@link HttpHandler} 
 * implementation about request events
 */
public interface HttpRequestCallbacks {

	HttpRequest getRequest(String parent, HttpRequestAction action, HttpRequestParameters parameters,
			HttpRequestMethod method);
	
	HttpRequest getRequest(String parent, Authentication authentication, HttpRequestAction action,
			HttpRequestParameters parameters, HttpRequestMethod method);

	JsonResponse onRequest(HttpRequest request) throws HttpGeneralException;
}