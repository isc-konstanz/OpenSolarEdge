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
package org.openmuc.jsonpath.data;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.Charset;

public class Authentication {

	private final static String PARAMETER = "api_key";

	private final String apiKey;

	public Authentication(String apiKey) {
		this.apiKey = apiKey;
	}

	public String getApiKey() {
		return apiKey;
	}

	public String encode(Charset charset) throws UnsupportedEncodingException {
		return URLEncoder.encode(PARAMETER, charset.name()) + 
				'=' + URLEncoder.encode(apiKey, charset.name());
	}

}
