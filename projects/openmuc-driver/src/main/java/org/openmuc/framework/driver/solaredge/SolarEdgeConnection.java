package org.openmuc.framework.driver.solaredge;

import java.text.ParseException;
import java.util.List;

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
import org.openmuc.jsonpath.HttpHandler;
import org.openmuc.jsonpath.data.TimeValue;
import org.openmuc.solaredge.SolarEdgeConst;
import org.openmuc.solaredge.SolarEdgeResponseHandler;
import org.openmuc.solaredge.data.TimeWrapper;

public class SolarEdgeConnection implements Connection {
    protected final DriverPreferences preferences = DriverInfoFactory.getPreferences(SolarEdgeDriver.class);

    /**
     * Interface used by {@link SolarEdgeConnection} to notify the {@link SolarEdgeDriver} about events
     */
    public interface SolarEdgeConnectionCallbacks {
        
        public void onDisconnect(int siteId);
    }

    //	private int siteId;
	protected SolarEdgeResponseHandler responseHandler;
	protected final int siteId;
	
    /**
     * The Connections current callback object, which is used to notify of connection events
     */
    private SolarEdgeConnectionCallbacks callbacks;

	public SolarEdgeConnection(int siteId, HttpHandler httpHandler, SolarEdgeConnectionCallbacks callbacks) {
		this.siteId = siteId;
		createResponseHandler(httpHandler);
	}
	
	protected void createResponseHandler(HttpHandler httpHandler) {
		responseHandler = new SolarEdgeResponseHandler(siteId, httpHandler);
	}
	
	public SolarEdgeResponseHandler getResponseHandler() {
		return responseHandler;
	}
	
	@Override
	public void disconnect() {
		if (callbacks != null) {
		  callbacks.onDisconnect(siteId);
          callbacks = null;
		}
	}

	@Override
	public Object read(List<ChannelRecordContainer> containers, Object containerListHandle, String samplingGroup)
			throws UnsupportedOperationException, ConnectionException {
    	try {
	        for (ChannelRecordContainer container : containers) {
	        	Record record = getRecord(container.getChannelAddress(), container.getChannelSettings());
	        	container.setRecord(record);
	        }
		} catch (Exception e) {
            throw new ConnectionException("channel not found");
		}
		
		return new Record(Flag.UNKNOWN_ERROR);
	}
	
	protected Record getRecord(String channelAddress, String channelSettings) {
         Record record;
		try {
		    ChannelSettings settings = preferences.get(channelSettings, ChannelSettings.class);
	    	String timePath = settings.getTimePath(channelAddress);
	    	String timeUnit = settings.getTimeUnit();
	    	String valuePath = settings.getValuePath(channelAddress);
	    	String serialNumber = settings.getSerialNumber();
	    	record = getRecord(valuePath, timePath, timeUnit, serialNumber);
		} catch (ArgumentSyntaxException e) {
			record = new Record(Flag.DRIVER_ERROR_CHANNEL_ADDRESS_SYNTAX_INVALID);
		}
		return record;
	}
	
	protected Record getRecord(String valuePath, String timePath, String timeUnit, String serialNumber) {
    	Record record;
		try {
			TimeValue timeValuePair = responseHandler.
					getTimeValuePair(valuePath, timePath, timeUnit, serialNumber);
	    	record = timeValuePairToRecord(timeValuePair);
		} catch (ParseException e) {
			record = new Record(Flag.DRIVER_ERROR_CHANNEL_VALUE_TYPE_CONVERSION_EXCEPTION);
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

	public static String recordToString(Record r) {
		String timeStr = new TimeWrapper(r.getTimestamp(),SolarEdgeConst.TIME_FORMAT).getTimeStr();
		return "value: " + r.getValue() + "; time: " + timeStr + "; flag: " + r.getFlag();
	}

	public static Record timeValuePairToRecord(TimeValue timeValuePair) {
		Record rec = null;
		if (timeValuePair.getValue() instanceof Boolean) {
			rec = new Record(new BooleanValue((Boolean)timeValuePair.getValue()),
					timeValuePair.getTime(), Flag.VALID);
		}
		else if (timeValuePair.getValue() instanceof byte[]) {
			rec = new Record(new ByteArrayValue((byte[])timeValuePair.getValue()),
					timeValuePair.getTime(), Flag.VALID);
		}
		else if (timeValuePair.getValue() instanceof Byte) {
			rec = new Record(new ByteValue((Byte)timeValuePair.getValue()),
					timeValuePair.getTime(), Flag.VALID);
		}
		else if (timeValuePair.getValue() instanceof Double) {
			rec = new Record(new DoubleValue((Double)timeValuePair.getValue()),
					timeValuePair.getTime(), Flag.VALID);
		}
		else if (timeValuePair.getValue() instanceof Float) {
			rec = new Record(new FloatValue((Float)timeValuePair.getValue()),
					timeValuePair.getTime(), Flag.VALID);
		}
		else if (timeValuePair.getValue() instanceof Integer) {
			rec = new Record(new IntValue((Integer)timeValuePair.getValue()),
					timeValuePair.getTime(), Flag.VALID);
		}
		else if (timeValuePair.getValue() instanceof Long) {
			rec = new Record(new LongValue((Long)timeValuePair.getValue()),
					timeValuePair.getTime(), Flag.VALID);
		}
		else if (timeValuePair.getValue() instanceof Short) {
			rec = new Record(new ShortValue((Short)timeValuePair.getValue()),
					timeValuePair.getTime(), Flag.VALID);
		}
		else if (timeValuePair.getValue() instanceof String) {
			rec = new Record(new StringValue((String)timeValuePair.getValue()),
					timeValuePair.getTime(), Flag.VALID);
		}
		else if (timeValuePair.getValue() == null) {
			rec = new Record(new StringValue(null),
					timeValuePair.getTime(), Flag.NO_VALUE_RECEIVED_YET);
		}
		else {
			rec = new Record(new StringValue(timeValuePair.getValue().toString()),
					timeValuePair.getTime(), Flag.VALID);
		}
		return rec;
	}

}
