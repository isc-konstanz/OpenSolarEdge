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

import java.security.InvalidParameterException;
import java.text.ParseException;

import org.openmuc.solaredge.SolarEdge;
import org.openmuc.solaredge.data.TimeWrapper;

public class SiteSensorsParameters extends TimeParameters {

	public SiteSensorsParameters(TimeWrapper lastTime, String timeUnit) {
		super(lastTime, timeUnit);
	}

	@Override
	protected void checkTimeUnit (String timeUnit) {
		if (timeUnit.equals("WEEK") || timeUnit.equals("MONTH") ||
				timeUnit.equals("YEAR")) {
				throw new InvalidParameterException("Time Unit: " + timeUnit + "is not FIVE_MINUTE, QUARTER_OF_AN_HOUR, HALF_OF_AN_HOUR, HOUR of DAY");
		}
	}

	@Override
	protected void getCheckedTime(TimeWrapper time, String timeUnit) throws ParseException {
		// This API is limited to one-week period. This means that the period
		// between endDate and startDate should not exceed one week). If the 
		// period is longer, the system will generate error 403 with a 
		// description.  
		if (now.getTime()-time.getTime() > SolarEdge.TIME_UNITS.get("WEEK")) {
			time.setTime(now.getTime() - SolarEdge.TIME_UNITS.get("WEEK"));
		}
	}
	
}
