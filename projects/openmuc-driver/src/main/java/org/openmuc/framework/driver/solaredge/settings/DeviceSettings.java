package org.openmuc.framework.driver.solaredge.settings;

import org.openmuc.framework.config.PreferenceType;
import org.openmuc.framework.config.Preferences;

public class DeviceSettings extends Preferences {

    public static final PreferenceType TYPE = PreferenceType.SETTINGS_DEVICE;

    @Option
    private String authentication ;

    @Override
    public PreferenceType getPreferenceType() {
        return TYPE;
    }

    public String getAuthentication() {
    	return authentication;
    }
}
