package org.openmuc.framework.driver.solaredge;

import java.util.HashMap;
import java.util.Map;

import org.openmuc.framework.config.ArgumentSyntaxException;
import org.openmuc.framework.config.DriverInfo;
import org.openmuc.framework.config.DriverInfoFactory;
import org.openmuc.framework.config.ScanException;
import org.openmuc.framework.config.ScanInterruptedException;
import org.openmuc.framework.driver.solaredge.SolarEdgeConnection.SolarEdgeConnectionCallbacks;
import org.openmuc.framework.driver.solaredge.settings.DeviceAddress;
import org.openmuc.framework.driver.solaredge.settings.DeviceSettings;
import org.openmuc.framework.driver.spi.Connection;
import org.openmuc.framework.driver.spi.ConnectionException;
import org.openmuc.framework.driver.spi.DriverDeviceScanListener;
import org.openmuc.framework.driver.spi.DriverService;
import org.openmuc.http.HttpHandler;
import org.openmuc.solaredge.SolarEdgeConfig;
import org.openmuc.solaredge.SolarEdgeHttpFactory;

public class SolarEdgeDriver implements DriverService, SolarEdgeConnectionCallbacks {

    private final static String ID = "SolarEdge";
    private final static String DESCRIPTION = "This driver can be used to access SolarEdge devices";

    public final DriverInfo driverInfo = DriverInfoFactory.getPreferences(SolarEdgeDriver.class);

	private final Map<Integer, SolarEdgeConnection> connectionsMap;
	private HttpHandler httpHandler;

	public SolarEdgeDriver() {
		connectionsMap = new HashMap<Integer, SolarEdgeConnection>();
	}
	
	@Override
	public Connection connect(String addressStr, String settingsStr) throws ArgumentSyntaxException, ConnectionException {

        DeviceAddress address = driverInfo.parse(addressStr, DeviceAddress.class);
        DeviceSettings settings = driverInfo.parse(settingsStr, DeviceSettings.class);
		
		SolarEdgeConnection connection = connectionsMap.get(address.getSiteId());
		
		if (connection == null) {
			SolarEdgeConfig config = new SolarEdgeConfig(address.getAddress(), settings.getAuthentication());				
			httpHandler = SolarEdgeHttpFactory.newAuthenticatedConnection(config);

			connection = new SolarEdgeConnection(address.getSiteId(), httpHandler, this);
			connectionsMap.put(address.getSiteId(), connection);
			httpHandler.start();
		}

		return connection;
	}

	@Override
	public DriverInfo getInfo() {
		return driverInfo;
	}

	@Override
	public void interruptDeviceScan() throws UnsupportedOperationException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void scanForDevices(String arg0, DriverDeviceScanListener arg1)
			throws UnsupportedOperationException, ArgumentSyntaxException, ScanException, ScanInterruptedException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onDisconnect(int siteId) {
		httpHandler.stop();
		connectionsMap.remove(siteId);
	}

}
