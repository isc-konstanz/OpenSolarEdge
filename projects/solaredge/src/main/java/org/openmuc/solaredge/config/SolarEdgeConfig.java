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
package org.openmuc.solaredge.config;

import org.openmuc.jsonpath.data.Config;

public class SolarEdgeConfig extends Config {

	private final static String ADDRESS_DEFAULT = "http://monitoringapi.solaredge.com/";
	private final static String API_KEY_DEFAULT = "L4QLVQ1LOKCQX2193VSEICXW61NP6B1O";
	private final static String AUTHORIZATION = "READ";

	private int maxThreads = 1;

	public SolarEdgeConfig() {
		super(ADDRESS_DEFAULT, API_KEY_DEFAULT, AUTHORIZATION, MAX_THREADS_DEFAULT);
	}
	
	public SolarEdgeConfig(String url) {
		super(url==null?ADDRESS_DEFAULT:url, API_KEY_DEFAULT, AUTHORIZATION, MAX_THREADS_DEFAULT);
	}
	
	public SolarEdgeConfig(String url, String authentication) {
		super(url==null?ADDRESS_DEFAULT:url, 
			  authentication==null?API_KEY_DEFAULT:authentication,
			  AUTHORIZATION, 
			  MAX_THREADS_DEFAULT);
	}
	
	@Override
	public int getMaxThreads() {
		if (maxThreads > 0 && maxThreads <= 3) {
			return maxThreads;
		}
		return MAX_THREADS_DEFAULT;
	}
}
