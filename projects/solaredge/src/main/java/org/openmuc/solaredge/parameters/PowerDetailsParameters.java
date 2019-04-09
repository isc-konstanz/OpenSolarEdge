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
package org.openmuc.solaredge.parameters;

import java.text.ParseException;

import org.openmuc.solaredge.config.SolarEdgeConst;
import org.openmuc.solaredge.data.TimeWrapper;

public class PowerDetailsParameters extends TimeParameters {

	public PowerDetailsParameters(TimeWrapper lastTime) {
		super(lastTime, SolarEdgeConst.QUARTER_OF_AN_HOUR);
		start = "startTime";
		end = "endTime";
	}

	@Override
	protected void getCheckedTime(TimeWrapper time, String timeUnit) throws ParseException {
		// This API is limited to one-month period. This means that the 
		// period between endTime and startTime should not exceed one month. If 
		// the period is longer, the system will generate error 403 with proper 
		// description. 
		if (now.getTime()-time.getTime() > SolarEdgeConst.TIME_UNIT_MAP.get("MONTH")) {
			//TODO test this with month february because a month is defined by 31 days
			time.setTime(now.getTime() - SolarEdgeConst.TIME_UNIT_MAP.get("MONTH"));
		}
	}
}
