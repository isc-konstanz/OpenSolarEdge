/* 
 * Copyright 2016-18 ISC Konstanz
 * 
 * This file is part of OpenSolarEdge.
 * For more information visit https://github.com/isc-konstanz/OpenSolarEdge
 * 
 * OpenSolarEdge is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * OpenSolarEdge is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with OpenSolarEdge.  If not, see <http://www.gnu.org/licenses/>.
 */
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
