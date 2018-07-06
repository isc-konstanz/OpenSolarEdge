/*
 * Copyright 2016-18 ISC Konstanz
 *
 * This file is part of OpenSkeleton.
 * For more information visit https://github.com/isc-konstanz/OpenSkeleton.
 *
 * OpenSkeleton is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * OpenSkeleton is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with OpenSkeleton.  If not, see <http://www.gnu.org/licenses/>.
 *
 */
package org.openmuc.solaredge;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SolarEdgeSite {
	private final static Logger logger = LoggerFactory.getLogger(SolarEdgeSite.class);

	public SolarEdgeSite(String address) {
		logger.info("Registered skeleton test: {}", address);
		
	}

	public Object read(String foo) {
		logger.info("Reading skeleton value for argument: {}", foo);
		
		return null;
	}

}
