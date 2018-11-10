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

import java.util.HashMap;

public class Options extends HashMap<String, String> {
	
	private static final long serialVersionUID = -1529415379371262163L;

	/**
	 * Sets the logging interval of the corresponding feed engine in milliseconds.
	 * 
	 * @param interval
	 * 	the logging interval of the corresponding feed engine in milliseconds
	 * 
	 * @return 
	 * 	the previous value associated with key, or null if there was no mapping for key. 
	 * 	(A null return can also indicate that the map previously associated null with key.)
	 */
	public String setInterval(int interval) {
		return put("interval", String.valueOf(interval));
	}
}
