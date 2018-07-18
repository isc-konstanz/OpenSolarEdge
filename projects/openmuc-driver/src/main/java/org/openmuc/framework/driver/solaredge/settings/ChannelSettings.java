package org.openmuc.framework.driver.solaredge.settings;

import org.openmuc.framework.config.PreferenceType;
import org.openmuc.framework.config.Preferences;

public class ChannelSettings extends Preferences {

    public static final PreferenceType TYPE = PreferenceType.SETTINGS_CHANNEL;

    @Option
    private String timePath;

    @Option
    private String timeUnit;

    @Option
    private String serialNumber;

    @Override
    public PreferenceType getPreferenceType() {
        return TYPE;
    }

    public String getTimePath() {
    	return timePath;
    }

    public String getTimeUnit() {
    	return timeUnit;
    }

    public String getSerialNumber() {
    	return serialNumber;
    }

}
