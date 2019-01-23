package org.openmuc.framework.driver.solaredge.test;

import org.openmuc.framework.config.ArgumentSyntaxException;
import org.openmuc.framework.driver.solaredge.SolarEdgeConnection;
import org.openmuc.framework.driver.solaredge.SolarEdgeDriver;
import org.openmuc.framework.driver.solaredge.settings.DeviceAddress;
import org.openmuc.framework.driver.solaredge.settings.DeviceSettings;
import org.openmuc.framework.driver.spi.Connection;
import org.openmuc.framework.driver.spi.ConnectionException;
import org.openmuc.jsonpath.HttpHandler;
import org.openmuc.jsonpath.TestHttpHandler;
import org.openmuc.solaredge.SolarEdgeConfig;
import org.openmuc.solaredge.TestHttpFactory;

public class TestSolarEdgeDriver extends SolarEdgeDriver {
	
	private String jsonString;
	private String jsonConnectString;
	
	public TestSolarEdgeDriver(String jsonConnectString, String jsonString) {
		this.jsonString = jsonString;
		this.jsonConnectString = jsonConnectString;
	}

	@Override
	public Connection connect(String addressStr, String settingsStr) throws ArgumentSyntaxException, ConnectionException {
		Connection connection = super.connect(addressStr, settingsStr);
		((TestHttpHandler)httpHandler).setResponse(jsonString);
		System.out.println("jsonString: " + jsonString);		
		return connection;
	}
	
	protected HttpHandler getHttpHandler(SolarEdgeConfig config) {
		TestHttpHandler httpHandler = TestHttpFactory.getHttpFactory().newAuthenticatedConnection(config);
		httpHandler.setResponse(jsonConnectString);
		System.out.println("jsonConnectString: " + jsonConnectString);
		return httpHandler;
	}

	protected SolarEdgeConnection createConnection(DeviceAddress address) {
		return new TestSolarEdgeConnection(address.getSiteId(), httpHandler, this);
	}
	
	public DeviceAddress getDeviceAddress() {
		return address;
	}

	public DeviceSettings getDeviceSettings() {
		return settings;
	}
}
