package org.openmuc.framework.driver.solaredge.test;

import org.openmuc.framework.data.Record;
import org.openmuc.framework.driver.solaredge.SolarEdgeConnection;
import org.openmuc.solaredge.TestHandler;

public class TestConnection extends SolarEdgeConnection {

	public TestConnection(TestHandler handler, SolarEdgeConnectionCallbacks callbacks) {
		super(handler, callbacks);
	}

	public TestHandler getHandler() {
		return (TestHandler) handler;
	}

	public Record getTestRecord(String channelAddress, String channelSettings) throws Exception {
		return getRecord(channelAddress, channelSettings, "");
	}

	public Record getTestRecord(String valuePath, String timePath, String timeUnit, String serialNumber) throws Exception {
		return getRecord(valuePath, timePath, timeUnit, serialNumber);
	}

}
