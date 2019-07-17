/* 
 * Copyright 2016-19 ISC Konstanz
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

import java.text.ParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import org.openmuc.framework.config.ArgumentSyntaxException;
import org.openmuc.framework.config.ChannelScanInfo;
import org.openmuc.framework.config.DriverInfoFactory;
import org.openmuc.framework.config.DriverPreferences;
import org.openmuc.framework.config.ScanException;
import org.openmuc.framework.data.BooleanValue;
import org.openmuc.framework.data.ByteArrayValue;
import org.openmuc.framework.data.ByteValue;
import org.openmuc.framework.data.DoubleValue;
import org.openmuc.framework.data.Flag;
import org.openmuc.framework.data.FloatValue;
import org.openmuc.framework.data.IntValue;
import org.openmuc.framework.data.LongValue;
import org.openmuc.framework.data.Record;
import org.openmuc.framework.data.ShortValue;
import org.openmuc.framework.data.StringValue;
import org.openmuc.framework.driver.solaredge.settings.ChannelSettings;
import org.openmuc.framework.driver.spi.ChannelRecordContainer;
import org.openmuc.framework.driver.spi.ChannelValueContainer;
import org.openmuc.framework.driver.spi.Connection;
import org.openmuc.framework.driver.spi.ConnectionException;
import org.openmuc.framework.driver.spi.RecordsReceivedListener;
import org.openmuc.jsonpath.data.TimeValue;
import org.openmuc.solaredge.SolarEdge;
import org.openmuc.solaredge.data.TimeWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SolarEdgeConnection implements Connection {
	private static final Logger logger = LoggerFactory.getLogger(SolarEdgeConnection.class);

    private static final DriverPreferences preferences = DriverInfoFactory.getPreferences(SolarEdgeDriver.class);

    private Map<String, TimeValue> timeValues = new HashMap<String, TimeValue>();

    /**
     * Interface used by {@link SolarEdgeConnection} to notify the {@link SolarEdgeDriver} about events
     */
    public interface SolarEdgeConnectionCallbacks {
        public void onDisconnect(int siteId);
    }

    /**
     * The Connections current callback object, which is used to notify of connection events
     */
    protected SolarEdgeConnectionCallbacks callbacks;

	protected final SolarEdge handler;

	public SolarEdgeConnection(SolarEdge handler, SolarEdgeConnectionCallbacks callbacks) {
		this.handler = handler;
		this.callbacks = callbacks;
	}

	@Override
	public void disconnect() {
		handler.close();
		if (callbacks != null) {
			callbacks.onDisconnect(handler.getSiteId());
			callbacks = null;
		}
	}

	@Override
	public Object read(List<ChannelRecordContainer> containers, Object containerListHandle, String samplingGroup)
			throws UnsupportedOperationException, ConnectionException {
    	try {
	        for (ChannelRecordContainer container : containers) {
	        	Record record = getRecord(container.getChannelAddress(), container.getChannelSettings(), container.getChannel().getId());
	        	container.setRecord(record);
	        }
		} catch (Exception e) {
            throw new ConnectionException(e.getMessage(), e);
		}
		return null;
	}

	protected Record getRecord(String channelAddress, String channelSettings, String id) throws Exception {
        Record record;
		try {
			logger.debug("channelAddress test {}, channelSettings {}, id {}", channelAddress, channelSettings, id);
			ChannelSettings settings = preferences.get(channelSettings, ChannelSettings.class);
			
	    	String timePath = settings.getTimePath(channelAddress);
	    	String timeUnit = settings.getTimeUnit();
	    	String valuePath = settings.getValuePath(channelAddress);
	    	String serialNumber = settings.getSerialNumber();
	    	
	    	record = getRecord(valuePath, timePath, timeUnit, serialNumber);
	    	logger.debug("Record: " + new TimeWrapper(record.getTimestamp(), SolarEdge.TIME_FORMAT, handler.getSiteZone()).getTimeStr() + 
	    			" Value " + channelAddress + ": " + record.getValue());

		} catch (ArgumentSyntaxException e) {
			record = new Record(Flag.DRIVER_ERROR_CHANNEL_ADDRESS_SYNTAX_INVALID);
			logger.debug(e.getMessage());
		}
		return record;
	}

	protected Record getRecord(String valuePath, String timePath, String timeUnit, String serialNumber) throws Exception {
    	Record record = null;
		try {
			logger.debug("valuePath {}, timePath {}, timeUnit {}, serialNumber {}", valuePath, timePath, timeUnit, serialNumber);
			TimeValue timeValue = timeValues.get(valuePath);
			if (timeValue != null && System.currentTimeMillis() - timeValue.getTime() < 60000) {
				timeValue = timeValues.get(valuePath);
			}
			else {
				timeValue = handler.getTimeValue(valuePath, timePath, timeUnit, serialNumber);
				timeValues.put(valuePath, timeValue);
			}
			if (timeValue == null) {
				record = new Record(Flag.DRIVER_ERROR_READ_FAILURE);
			}
			else {
		    	record = getRecord(timeValue);
			}
	    	
		} catch (ParseException e) {
			logger.error(e.getMessage());
			record = new Record(Flag.DRIVER_ERROR_CHANNEL_VALUE_TYPE_CONVERSION_EXCEPTION);
			
		} catch (Exception e) {
			record = new Record(Flag.CONNECTION_EXCEPTION);
			logger.error(e.getMessage());
			if (e.getCause() instanceof ExecutionException || 
				e.getCause() instanceof RuntimeException) {
				throw e;
			}
		}
		return record;
	}

	protected Record getRecord(TimeValue timeValue) {
		Record record = null;
		if (timeValue.getValue() instanceof Boolean) {
			record = new Record(new BooleanValue((Boolean)timeValue.getValue()),
					timeValue.getTime(), Flag.VALID);
		}
		else if (timeValue.getValue() instanceof byte[]) {
			record = new Record(new ByteArrayValue((byte[])timeValue.getValue()),
					timeValue.getTime(), Flag.VALID);
		}
		else if (timeValue.getValue() instanceof Byte) {
			record = new Record(new ByteValue((Byte)timeValue.getValue()),
					timeValue.getTime(), Flag.VALID);
		}
		else if (timeValue.getValue() instanceof Double) {
			record = new Record(new DoubleValue((Double)timeValue.getValue()),
					timeValue.getTime(), Flag.VALID);
		}
		else if (timeValue.getValue() instanceof Float) {
			record = new Record(new FloatValue((Float)timeValue.getValue()),
					timeValue.getTime(), Flag.VALID);
		}
		else if (timeValue.getValue() instanceof Integer) {
			record = new Record(new IntValue((Integer)timeValue.getValue()),
					timeValue.getTime(), Flag.VALID);
		}
		else if (timeValue.getValue() instanceof Long) {
			record = new Record(new LongValue((Long)timeValue.getValue()),
					timeValue.getTime(), Flag.VALID);
		}
		else if (timeValue.getValue() instanceof Short) {
			record = new Record(new ShortValue((Short)timeValue.getValue()),
					timeValue.getTime(), Flag.VALID);
		}
		else if (timeValue.getValue() instanceof String) {
			record = new Record(new StringValue((String)timeValue.getValue()),
					timeValue.getTime(), Flag.VALID);
		}
		else if (timeValue.getValue() == null) {
			record = new Record(new StringValue(null),
					timeValue.getTime(), Flag.NO_VALUE_RECEIVED_YET);
		}
		else {
			record = new Record(new StringValue(timeValue.getValue().toString()),
					timeValue.getTime(), Flag.VALID);
		}
		return record;
	}

	@Override
	public List<ChannelScanInfo> scanForChannels(String settings)
			throws UnsupportedOperationException, ArgumentSyntaxException, ScanException, ConnectionException {
		throw new UnsupportedOperationException();
	}

	@Override
	public void startListening(List<ChannelRecordContainer> arg0, RecordsReceivedListener arg1)
			throws UnsupportedOperationException, ConnectionException {
        throw new UnsupportedOperationException();
	}

	@Override
	public Object write(List<ChannelValueContainer> arg0, Object arg1)
			throws UnsupportedOperationException, ConnectionException {
        throw new UnsupportedOperationException();
	}

}
