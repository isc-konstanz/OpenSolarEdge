package org.openmuc.framework.driver.solaredge.test;

import org.openmuc.framework.driver.solaredge.SolarEdgeConnection;
import org.openmuc.framework.driver.solaredge.SolarEdgeDriver;
import org.openmuc.framework.driver.solaredge.settings.DeviceAddress;
import org.openmuc.framework.driver.solaredge.settings.DeviceSettings;
import org.openmuc.jsonpath.HttpHandler;
import org.openmuc.jsonpath.TestHttpHandler;
import org.openmuc.solaredge.SolarEdgeConfig;
import org.openmuc.solaredge.TestHttpFactory;

public class TestSolarEdgeDriver extends SolarEdgeDriver {
	
	private String jsonString;
	
	public TestSolarEdgeDriver(String jsonString) {
		this.jsonString = jsonString;
	}

	protected HttpHandler getHttpHandler(SolarEdgeConfig config) {
		TestHttpHandler httpHandler = TestHttpFactory.getHttpFactory().newAuthenticatedConnection(config);
		httpHandler.setResponse(jsonString);
		System.out.println("JsonString: " + jsonString);
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
