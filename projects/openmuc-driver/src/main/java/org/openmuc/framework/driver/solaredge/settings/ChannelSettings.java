package org.openmuc.framework.driver.solaredge.settings;

import org.openmuc.framework.config.PreferenceType;
import org.openmuc.framework.config.Preferences;
import org.openmuc.solaredge.SolarEdgeConst;

public class ChannelSettings extends Preferences {

    public static final PreferenceType TYPE = PreferenceType.SETTINGS_CHANNEL;

    @Option
    private String timeUnit;

    @Option
    private String serialNumber;

    @Override
    public PreferenceType getPreferenceType() {
        return TYPE;
    }

    public String getValuePath(String address) {
    	String path = SolarEdgeConst.REQUEST_VALUE_PATH_MAP.get(address);
    	switch (address) {
    		case "storageData power" :
    		case "storageData batteryState" :
    		case "storageData lifeTimeEnergyDischarged" :
    		case "storageData stateOfCharge" :
    			if (serialNumber != null) {
    				if (path.contains("[0]")) {
    					path = path.replaceAll("[0]", "?(@.serialNumber=='@serialNumber')");
    				}
    			}
    			break;    			
    	}
    	return path;
    }

    public String getTimePath(String address) {
    	String path = SolarEdgeConst.REQUEST_TIME_PATH_MAP.get(address);
    	switch (address) {
    		case "storageData power" :
    		case "storageData batteryState" :
    		case "storageData lifeTimeEnergyDischarged" :
    		case "storageData stateOfCharge" :
    			if (serialNumber != null) {
    				if (path.contains("[0]")) {
    					path = path.replaceAll("[0]", "?(@.serialNumber=='@serialNumber')");
    				}
    			}
    			break;    			
    	}
    	return path;
    }

    public String getTimeUnit() {
    	return timeUnit;
    }

    public String getSerialNumber() {
    	return serialNumber;
    }

}
