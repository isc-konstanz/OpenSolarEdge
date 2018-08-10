package org.openmuc.framework.driver.solaredge.test;

import java.text.ParseException;

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
	
	public Record getRecordForTest(String channelAddress, String channelSettings) throws ParseException {
		return getRecord(channelAddress, channelSettings);
	}
}
