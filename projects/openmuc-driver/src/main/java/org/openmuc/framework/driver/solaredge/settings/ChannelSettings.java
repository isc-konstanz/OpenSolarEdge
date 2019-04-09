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
package org.openmuc.framework.driver.solaredge.settings;

import org.openmuc.framework.config.PreferenceType;
import org.openmuc.framework.config.Preferences;
import org.openmuc.solaredge.config.SolarEdgeConst;

public class ChannelSettings extends Preferences {

    public static final PreferenceType TYPE = PreferenceType.SETTINGS_CHANNEL;

    @Option
    private String timeUnit = SolarEdgeConst.QUARTER_OF_AN_HOUR;

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
    			if (serialNumber != null && serialNumber.length() > 0) {
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
    			if (serialNumber != null && serialNumber.length() > 0) {
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
