package org.openmuc.framework.driver.solaredge;

import java.util.HashMap;
import java.util.Map;

import org.openmuc.framework.config.ArgumentSyntaxException;
import org.openmuc.framework.config.DriverInfo;
import org.openmuc.framework.config.DriverInfoFactory;
import org.openmuc.framework.driver.solaredge.SolarEdgeConnection.SolarEdgeConnectionCallbacks;
import org.openmuc.framework.driver.solaredge.settings.DeviceAddress;
import org.openmuc.framework.driver.solaredge.settings.DeviceSettings;
import org.openmuc.framework.driver.spi.Connection;
import org.openmuc.framework.driver.spi.ConnectionException;
import org.openmuc.framework.driver.spi.DriverService;
import org.openmuc.jsonpath.HttpHandler;
import org.openmuc.solaredge.SolarEdgeConfig;
import org.openmuc.solaredge.SolarEdgeHttpFactory;
import org.osgi.service.component.annotations.Component;

@Component
public class SolarEdgeDriver implements DriverService, SolarEdgeConnectionCallbacks {
    protected final DriverInfo info = DriverInfoFactory.getPreferences(SolarEdgeDriver.class);

	protected final Map<Integer, SolarEdgeConnection> connectionsMap;
	protected HttpHandler httpHandler;
	protected DeviceAddress address;
	protected DeviceSettings settings;

	public SolarEdgeDriver() {
		connectionsMap = new HashMap<Integer, SolarEdgeConnection>();
	}
	
	@Override
	public Connection connect(String addressStr, String settingsStr) throws ArgumentSyntaxException, ConnectionException {

        address = info.parse(addressStr, DeviceAddress.class);
        settings = info.parse(settingsStr, DeviceSettings.class);
		
		SolarEdgeConnection connection = connectionsMap.get(address.getSiteId());
		
		if (connection == null) {
			SolarEdgeConfig config = new SolarEdgeConfig(address.getAddress(), settings.getAuthentication());				
			httpHandler = getHttpHandler(config);

			connection = createConnection(address);
			connectionsMap.put(address.getSiteId(), connection);
			httpHandler.start();
		}

		return connection;
	}
	
	protected HttpHandler getHttpHandler(SolarEdgeConfig config) {
		return SolarEdgeHttpFactory.getHttpFactory().newAuthenticatedConnection(config);
	}

	protected SolarEdgeConnection createConnection(DeviceAddress address) {
		return new SolarEdgeConnection(address.getSiteId(), httpHandler, this);
	}
	
	@Override
	public DriverInfo getInfo() {
		return info;
	}

	@Override
	public void onDisconnect(int siteId) {
		httpHandler.stop();
		connectionsMap.remove(siteId);
	}

}
