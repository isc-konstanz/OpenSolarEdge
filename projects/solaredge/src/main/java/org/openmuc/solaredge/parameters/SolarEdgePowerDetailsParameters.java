package org.openmuc.solaredge.parameters;

import java.text.ParseException;

import org.openmuc.solaredge.SolarEdgeConst;
import org.openmuc.solaredge.data.TimeWrapper;

public class SolarEdgePowerDetailsParameters extends SolarEdgeStartEndTimeParameters {

	public SolarEdgePowerDetailsParameters(TimeWrapper lastTime) {
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
