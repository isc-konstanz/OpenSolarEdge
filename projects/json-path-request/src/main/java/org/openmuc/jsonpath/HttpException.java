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

import java.io.IOException;


public class HttpException extends IOException {

	private static final long serialVersionUID = -5207405404653956732L;
	private String message = "Unknown HTTP error";

	public HttpException() {
	}

	public HttpException(String message) {
		this.message = message;
	}

	public HttpException(int httpCode) {
		this.message = "HTTP status code: " + httpCode;
	}

	@Override
	public String getMessage() {
		return message;
	}
}
