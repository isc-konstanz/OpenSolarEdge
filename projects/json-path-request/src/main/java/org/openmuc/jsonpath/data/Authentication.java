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
package org.openmuc.jsonpath.data;


public class Authentication {

	private final Authorization authorization;
	private final String key;

	public Authentication(Authorization authorization, String key) {
		this.authorization = authorization;
		this.key = key;
	}

	public Authorization getAuthorization() {
		return authorization;
	}
	
	public boolean isDefault() {
		if (authorization == Authorization.DEFAULT) {
			return true;
		}
		return false;
	}

	public String getKey() {
		return key;
	}

	@Override
	public boolean equals(Object object) {
		if (object instanceof Authentication) {
			Authentication authentication = (Authentication) object;
			if (authentication.getAuthorization() == authorization &&
					authentication.getKey().equals(key)) {
				return true;
			}
		}
		return false;
	}

	@Override
	public String toString() {
		switch (authorization) {
		case NONE:
			return "None";
		case DEFAULT:
			return "Default";
		default:
			return authorization.getValue() + "=" + key;
		}
	}
}
