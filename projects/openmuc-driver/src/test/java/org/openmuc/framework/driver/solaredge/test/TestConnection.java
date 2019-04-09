package org.openmuc.framework.driver.solaredge.test;

import org.openmuc.framework.data.Record;
import org.openmuc.framework.driver.solaredge.SolarEdgeConnection;
import org.openmuc.solaredge.TestHandler;

public class TestConnection extends SolarEdgeConnection {

	public TestConnection(TestHandler handler, SolarEdgeConnectionCallbacks callbacks) {
		super(handler, callbacks);
	}

	public Record getRecordForTest(String channelAddress, String channelSettings) throws Exception {
		return getRecord(channelAddress, channelSettings, "");
	}

	public Record getRecordForTest(String valuePath, String timePath, String timeUnit, String serialNumber) throws Exception {
		return getRecord(valuePath, timePath, timeUnit, serialNumber);
	}
	
}
