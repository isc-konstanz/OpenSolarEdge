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
import org.openmuc.solaredge.SolarEdge;
import org.openmuc.solaredge.config.SolarEdgeConfig;
import org.osgi.service.component.annotations.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component
public class SolarEdgeDriver implements DriverService, SolarEdgeConnectionCallbacks {
	private static final Logger logger = LoggerFactory.getLogger(SolarEdgeDriver.class);
	
	protected final DriverInfo info = DriverInfoFactory.getPreferences(SolarEdgeDriver.class);

	protected final Map<Integer, SolarEdgeConnection> connectionsMap;

	public SolarEdgeDriver() {
		connectionsMap = new HashMap<Integer, SolarEdgeConnection>();
	}

	@Override
	public Connection connect(String addressStr, String settingsStr) throws ArgumentSyntaxException, ConnectionException {
		logger.debug("Connect SolarEdge device: \"{}\" for settings: {}", addressStr, settingsStr);
		
		DeviceAddress address = info.parse(addressStr, DeviceAddress.class);
		DeviceSettings settings = info.parse(settingsStr, DeviceSettings.class);
		try {
			return connect(address.getSiteId(), new SolarEdgeConfig(address.getAddress(), settings.getAuthentication()));
			
		} catch (Exception e) {
			throw new ConnectionException("Unable to connect to SolarEdge site: " + e.toString());
		}
	}

	public SolarEdgeConnection connect(int siteId, SolarEdgeConfig config) throws Exception {
		SolarEdgeConnection connection = connectionsMap.get(siteId);
		if (connection == null) {
			SolarEdge handler = new SolarEdge(siteId, config);
			
			connection = new SolarEdgeConnection(handler, this);
			connectionsMap.put(siteId, connection);
		}
		return connection;
	}

	@Override
	public DriverInfo getInfo() {
		return info;
	}

	@Override
	public void onDisconnect(int siteId) {
		connectionsMap.remove(siteId);
	}

}
