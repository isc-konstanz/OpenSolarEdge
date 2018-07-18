package org.openmuc.solaredge.parameters;

import java.security.InvalidParameterException;
import java.text.ParseException;

import org.openmuc.solaredge.SolarEdgeConst;
import org.openmuc.solaredge.data.TimeWrapper;

public class SolarEdgeDataParameters extends SolarEdgeStartEndTimeParameters {
	
	public SolarEdgeDataParameters(TimeWrapper lastTime, String timeUnit) {
		super(lastTime, timeUnit);
		start = "startTime";
		end = "endTime";
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
		// between endTime and startTime should not exceed one week). If the 
		// period is longer, the system will generate error 403 with proper 
		// description.  
		if (now.getTime()-time.getTime() > SolarEdgeConst.TIME_UNIT_MAP.get("WEEK")) {
			time.setTime(now.getTime() - SolarEdgeConst.TIME_UNIT_MAP.get("WEEK"));
		}
	}
	
}
