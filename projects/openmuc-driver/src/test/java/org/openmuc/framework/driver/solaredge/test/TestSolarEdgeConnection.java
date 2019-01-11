package org.openmuc.framework.driver.solaredge.test;

import org.openmuc.framework.data.Record;
import org.openmuc.framework.driver.solaredge.SolarEdgeConnection;
import org.openmuc.jsonpath.HttpHandler;
import org.openmuc.solaredge.TestSolarEdgeResponseHandler;

public class TestSolarEdgeConnection extends SolarEdgeConnection {

	public TestSolarEdgeConnection(int siteId, HttpHandler httpHandler, SolarEdgeConnectionCallbacks callbacks) {
		super(siteId, httpHandler, callbacks);
	}
	
	@Override
	protected void createResponseHandler(HttpHandler httpHandler) {
		responseHandler = new TestSolarEdgeResponseHandler(siteId, httpHandler);
	}
	
	public Record getRecordForTest(String channelAddress, String channelSettings) throws Exception {
		return getRecord(channelAddress, channelSettings, "");
	}
	
	public Record getRecordForTest(String valuePath, String timePath, String timeUnit, String serialNumber) throws Exception {
		return getRecord(valuePath, timePath, timeUnit, serialNumber);
	}
	
}
