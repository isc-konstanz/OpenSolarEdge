package org.openmuc.solaredge.parameters;

import java.text.ParseException;

import org.openmuc.solaredge.SolarEdgeConst;
import org.openmuc.solaredge.data.TimeWrapper;

public class SolarEdgeTimeFrameEnergyParameters extends SolarEdgeStartEndTimeParameters {

	public SolarEdgeTimeFrameEnergyParameters(TimeWrapper lastTime) {
		super(lastTime, "DAY");
		lastTime.setFormat(SolarEdgeConst.DATE_FORMAT);
		setNowTimeFormat(SolarEdgeConst.DATE_FORMAT);
	}

	@Override
	public void addParameters() throws ParseException {
		super.addParameters();
		if (timeUnit.equals("HALF_OF_AN_HOUR")) timeUnit = SolarEdgeConst.QUARTER_OF_AN_HOUR;
		parameters.addParameter("timeUnit", timeUnit);		
	}
}
