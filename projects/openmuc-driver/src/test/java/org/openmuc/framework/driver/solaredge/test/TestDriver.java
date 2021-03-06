package org.openmuc.framework.driver.solaredge.test;

import org.openmuc.framework.config.ArgumentSyntaxException;
import org.openmuc.framework.driver.solaredge.SolarEdgeConnection;
import org.openmuc.framework.driver.solaredge.SolarEdgeDriver;
import org.openmuc.framework.driver.solaredge.settings.DeviceAddress;
import org.openmuc.framework.driver.solaredge.settings.DeviceSettings;
import org.openmuc.framework.driver.spi.Connection;
import org.openmuc.framework.driver.spi.ConnectionException;
import org.openmuc.jsonpath.TestHttpHandler;
import org.openmuc.solaredge.TestHandler;

public class TestDriver extends SolarEdgeDriver {

	private DeviceAddress address;
	private DeviceSettings settings;

	private String jsonString;
	private String jsonConnectString;

	public TestDriver(String jsonConnectString, String jsonString) {
		this.jsonString = jsonString;
		this.jsonConnectString = jsonConnectString;
	}

	@Override
	public Connection connect(String addressStr, String settingsStr) throws ArgumentSyntaxException, ConnectionException {
		address = info.parse(addressStr, DeviceAddress.class);
		settings = info.parse(settingsStr, DeviceSettings.class);
		
		TestConnection connection = (TestConnection) super.connect(addressStr, settingsStr);
		connection.getHandler().getHttpHandler().setResponse(jsonString);
		return connection;
	}

	@Override
	public SolarEdgeConnection connect(int siteId, String address, String apiKey) throws Exception {
		TestConnection connection = (TestConnection) connectionsMap.get(siteId);
		if (connection == null) {
			TestHttpHandler handler = new TestHttpHandler(address, apiKey).setResponse(jsonConnectString);
			connection = new TestConnection(new TestHandler(siteId, handler), this);
			connectionsMap.put(siteId, connection);
		}
		return connection;
	}

	public DeviceAddress getDeviceAddress() {
		return address;
	}

	public DeviceSettings getDeviceSettings() {
		return settings;
	}
}
