package org.openmuc.solaredge.parameters;

import java.security.InvalidParameterException;
import java.text.ParseException;

import org.openmuc.solaredge.SolarEdgeConst;
import org.openmuc.solaredge.data.TimeWrapper;

public class SolarEdgeStartEndTimeParameters extends SolarEdgeParameters {

	protected TimeWrapper lastTime;
	protected String timeUnit;
	protected String start = "startDate";
	protected String end = "endDate";
	protected String timeStr;
			
	
	
	public SolarEdgeStartEndTimeParameters(TimeWrapper lastTime, String timeUnit) {
		checkTimeUnit(timeUnit);
		this.lastTime = lastTime;
		this.timeUnit = timeUnit;
	}

	protected void checkTimeUnit (String timeUnit) {
		if (timeUnit.equals("FIVE_MINUTE")) {
			throw new InvalidParameterException("Time Unit: " + timeUnit + "is not QUARTER_OF_AN_HOUR, HALF_OF_AN_HOUR, HOUR, DAY, WEEK, MONTH, YEAR");			
		}
	}

	@Override
	public void addParameters() throws ParseException {
		super.addParameters();
		
		TimeWrapper nextLastTime;		
		if (lastTime == null || (now.getTime()-SolarEdgeConst.TIME_UNIT_MAP.get(timeUnit))<lastTime.getTime()) {
			nextLastTime = new TimeWrapper(now.getTime()-SolarEdgeConst.TIME_UNIT_MAP.get(timeUnit), nowTimeFormat);
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
			if (now.getTime()-time.getTime() > SolarEdgeConst.TIME_UNIT_MAP.get("MONTH")) {
				//TODO test this with month february because a month is defined by 31 days
				time.setTime(now.getTime() - SolarEdgeConst.TIME_UNIT_MAP.get("MONTH"));
			}
		}
		else if (timeUnit.equals("DAY")) {
			if (now.getTime()-time.getTime() > SolarEdgeConst.TIME_UNIT_MAP.get("YEAR")) {
				time.setTime(now.getTime() - SolarEdgeConst.TIME_UNIT_MAP.get("YEAR"));
			}
		}
		//TODO test this for time unit WEEK, MONTH, YEAR because we do not restrict it to one YEAR.
	}
	
	public TimeWrapper getLastTime() {
		return lastTime;
	}
}
