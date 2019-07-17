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

public class TimeParameters extends SolarEdgeParameters {

	protected TimeWrapper lastTime;
	protected String timeUnit;
	protected String start = "startDate";
	protected String end = "endDate";
	protected String timeStr;

	public TimeParameters(TimeWrapper time, String timeUnit) throws InvalidParameterException {
		super(time.getTimeZone());
		this.lastTime = time;
		this.timeUnit = timeUnit;
        
		checkTimeUnit(timeUnit);
	}

	protected void checkTimeUnit (String timeUnit) throws InvalidParameterException {
		if (timeUnit.equals("FIVE_MINUTE")) {
			throw new InvalidParameterException("Time Unit: " + timeUnit + "is not QUARTER_OF_AN_HOUR, HALF_OF_AN_HOUR, HOUR, DAY, WEEK, MONTH, YEAR");			
		}
	}

	@Override
	public void addParameters() throws ParseException {
		super.addParameters();
		
		TimeWrapper nextLastTime;		
		if (lastTime == null || (now.getTime()-SolarEdge.TIME_UNITS.get(timeUnit))<lastTime.getTime()) {
			nextLastTime = new TimeWrapper(now.getTime()-SolarEdge.TIME_UNITS.get(timeUnit), format, zone);
		}
		else {
			nextLastTime =  lastTime.clone();
		}
		getCheckedTime(nextLastTime, timeUnit);
		String startDateStr = nextLastTime.getTimeStr();
		String endDateStr = now.getTimeStr();
		parameters.addParameter(start, startDateStr);
		parameters.addParameter(end, endDateStr);
		lastTime = nextLastTime;
	}

	protected void getCheckedTime(TimeWrapper time, String timeUnit) throws ParseException {
		// This API is limited to one year when using timeUnit=DAY (i.e., 
		// daily resolution) and to one month when using timeUnit=
		// QUARTER_OF_AN_HOUR or timeUnit=HOUR. This means that the period between
		// endTime and startTime should not exceed one year or one month respectively. 
		// If the period is longer, the system will generate error 403 with proper 
		// description. 
		if (timeUnit.equals("QUARTER_OF_AN_HOUR") || 
			timeUnit.equals("HALF_OF_AN_HOUR") ||
			timeUnit.equals("HOUR")) {
			if (now.getTime()-time.getTime() > SolarEdge.TIME_UNITS.get("MONTH")) {
				//TODO test this with month february because a month is defined by 31 days
				time.setTime(now.getTime() - SolarEdge.TIME_UNITS.get("MONTH"));
			}
		}
		else if (timeUnit.equals("DAY")) {
			if (now.getTime()-time.getTime() > SolarEdge.TIME_UNITS.get("YEAR")) {
				time.setTime(now.getTime() - SolarEdge.TIME_UNITS.get("YEAR"));
			}
		}
		//TODO test this for time unit WEEK, MONTH, YEAR because we do not restrict it to one YEAR.
	}
	
	public TimeWrapper getLastTime() {
		return lastTime;
	}
}
