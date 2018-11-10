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
package org.openmuc.solaredge;

import org.openmuc.jsonpath.HttpFactory;
import org.openmuc.jsonpath.data.Authentication;
import org.openmuc.jsonpath.data.Authorization;
import org.openmuc.solaredge.data.SolarEdgeApiKeyConst;

public class SolarEdgeHttpFactory extends HttpFactory {

	protected final static SolarEdgeHttpFactory factory = new SolarEdgeHttpFactory();
	
	public static SolarEdgeHttpFactory getHttpFactory() {
		return factory;
	}
	
	@Override
	public Authentication getCredentials(String type, String key) {
		
		Authentication authentication = null;
		if (type != null && key != null) {
			Authorization.setApiKey(SolarEdgeApiKeyConst.getApiKeyConst().getKey());
			Authorization authorization = Authorization.valueOf(type);
			authentication = new Authentication(authorization, key);
		}
		return authentication;
	}
}
