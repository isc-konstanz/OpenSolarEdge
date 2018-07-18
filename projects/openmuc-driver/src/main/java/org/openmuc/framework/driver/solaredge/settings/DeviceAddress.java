package org.openmuc.framework.driver.solaredge.settings;

import org.openmuc.framework.config.PreferenceType;
import org.openmuc.framework.config.Preferences;

public class DeviceAddress  extends Preferences {

    public static final PreferenceType TYPE = PreferenceType.ADDRESS_DEVICE;

    @Option
    private String address;

    @Option
    private int siteId;

    @Override
    public PreferenceType getPreferenceType() {
        return TYPE;
    }

    public String getAddress() {
        return address;
    }

    public int getSiteId() {
        return siteId;
    }

}
