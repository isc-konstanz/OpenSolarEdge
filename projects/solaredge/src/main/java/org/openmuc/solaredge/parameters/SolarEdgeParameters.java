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
package org.openmuc.solaredge.parameters;

import java.text.ParseException;
import java.util.TimeZone;

import org.openmuc.jsonpath.request.HttpParameters;
import org.openmuc.solaredge.SolarEdge;
import org.openmuc.solaredge.data.TimeWrapper;

public class SolarEdgeParameters {

	protected HttpParameters parameters = new HttpParameters();
	protected String format = SolarEdge.TIME_FORMAT;
	protected TimeZone zone;
	protected TimeWrapper now;

	public SolarEdgeParameters(TimeZone zone) {
		this.zone = zone;
	}

	public HttpParameters getParameters() throws ParseException {
		return parameters;
	}

	public void addParameters() throws ParseException {
		now = new TimeWrapper(System.currentTimeMillis(), format, zone);
	}

	public TimeWrapper getTimeWrapperNow() {
		return now;
	}

	public void setFormatTimeFormat(String format) {
		this.format = format;
	}
}
