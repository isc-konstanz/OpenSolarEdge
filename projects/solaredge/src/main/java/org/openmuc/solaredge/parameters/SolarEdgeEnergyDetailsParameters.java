package org.openmuc.solaredge.parameters;

import java.text.ParseException;

import org.openmuc.solaredge.SolarEdgeConst;
import org.openmuc.solaredge.data.TimeWrapper;

public class SolarEdgeEnergyDetailsParameters extends SolarEdgeStartEndTimeParameters {

	public SolarEdgeEnergyDetailsParameters(TimeWrapper lastTime, String timeUnit) {
		super(lastTime, timeUnit);
		start = "startTime";
		end = "endTime";
	}

	@Override
	public void addParameters() throws ParseException {
		super.addParameters();
		if (timeUnit.equals("HALF_OF_AN_HOUR")) timeUnit = SolarEdgeConst.QUARTER_OF_AN_HOUR;
		parameters.addParameter("timeUnit", timeUnit);		
	}

}
