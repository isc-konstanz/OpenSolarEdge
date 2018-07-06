/*
 * Copyright 2016-18 ISC Konstanz
 *
 * This file is part of OpenSkeleton.
 * For more information visit https://github.com/isc-konstanz/OpenSkeleton.
 *
 * OpenSkeleton is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * OpenSkeleton is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with OpenSkeleton.  If not, see <http://www.gnu.org/licenses/>.
 *
 */
package org.openmuc.framework.driver.solaredge;

import java.util.List;

import org.openmuc.framework.config.ArgumentSyntaxException;
import org.openmuc.framework.config.ChannelScanInfo;
import org.openmuc.framework.config.DriverInfoFactory;
import org.openmuc.framework.config.DriverPreferences;
import org.openmuc.framework.config.ScanException;
import org.openmuc.framework.data.DoubleValue;
import org.openmuc.framework.data.Flag;
import org.openmuc.framework.data.Record;
import org.openmuc.framework.data.Value;
import org.openmuc.framework.driver.solaredge.settings.ChannelAddress;
import org.openmuc.framework.driver.solaredge.settings.ChannelSettings;
import org.openmuc.framework.driver.solaredge.settings.DeviceAddress;
import org.openmuc.framework.driver.spi.ChannelRecordContainer;
import org.openmuc.framework.driver.spi.ChannelValueContainer;
import org.openmuc.framework.driver.spi.Connection;
import org.openmuc.framework.driver.spi.ConnectionException;
import org.openmuc.framework.driver.spi.RecordsReceivedListener;
import org.openmuc.solaredge.SolarEdgeSite;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SolarEdgeConnection implements Connection {
    private final static Logger logger = LoggerFactory.getLogger(SolarEdgeConnection.class);

    private final DriverPreferences prefs = DriverInfoFactory.getPreferences(SolarEdgeDriver.class);

    private final SolarEdgeSite connection;

    public SolarEdgeConnection(String addressStr, String deviceStr) throws ArgumentSyntaxException {
        DeviceAddress address = prefs.parse(addressStr, DeviceAddress.class);
        
        this.connection = new SolarEdgeSite(address.getAddress());
    }

    @Override
    public List<ChannelScanInfo> scanForChannels(String settings)
            throws UnsupportedOperationException, ArgumentSyntaxException, ScanException, ConnectionException {

        // TODO Implement your channel scan or remove the whole method
        return null;
    }

    @Override
    public Object read(List<ChannelRecordContainer> containers, Object containerListHandle, String samplingGroup)
            throws UnsupportedOperationException, ConnectionException {
        
        long samplingTime = System.currentTimeMillis();
        
        for (ChannelRecordContainer container : containers) {
            try {
                ChannelAddress address = prefs.get(container.getChannelAddress(), ChannelAddress.class);
                ChannelSettings settings = prefs.get(container.getChannelSettings(), ChannelSettings.class);
                
                int bar = settings.getBar();
                logger.info("Channel \"{}\" was configured with setting bar: {}", container.getChannel().getId(), bar);                
                
                String foo = address.getFoo();
                Value value = new DoubleValue((double) connection.read(foo));
                container.setRecord(new Record(value, samplingTime, Flag.VALID));
                
            } catch (ArgumentSyntaxException e) {
                logger.warn("Unable to configure channel address \"{}\": {}", container.getChannelAddress(), e);
                container.setRecord(new Record(null, samplingTime, Flag.DRIVER_ERROR_CHANNEL_ADDRESS_SYNTAX_INVALID));
            }
        }
        
        return null;
    }

    @Override
    public void startListening(List<ChannelRecordContainer> containers, RecordsReceivedListener listener)
            throws UnsupportedOperationException, ConnectionException {

        // TODO Implement your listener registration or remove the whole method
        
    }

    @Override
    public Object write(List<ChannelValueContainer> containers, Object containerListHandle)
            throws UnsupportedOperationException, ConnectionException {

        // TODO Implement your write functionality or remove the whole method
        return null;
    }

    @Override
    public void disconnect() {

        // TODO Implement your resource cleanup
    }

}
