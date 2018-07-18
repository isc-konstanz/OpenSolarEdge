package org.openmuc.solaredge;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class SolarEdgeConst {

	public static Map<String, String> REQUEST_PATH_MAP = new HashMap<String, String>() {
		private static final long serialVersionUID = 7565668512142972983L;
	{
		put("Sites", 			"sites/list");
		put("details", 			"site/@siteId/details");
		put("dataPeriod", 		"site/@siteId/dataPeriod");
		put("energy", 			"site/@siteId/energy");
		put("timeFrameEnergy", 	"site/@siteId/timeFrameEnergy");
		put("power", 			"site/@siteId/power");
		put("overview", 		"site/@siteId/overview");
		put("powerDetails", 	"site/@siteId/powerDetails");
		put("energyDetails", 	"site/@siteId/energyDetails");
		put("siteCurrentPowerFlow", "site/@siteId/currentPowerFlow");
		put("storageData", 		"site/@siteId/storageData");
		put("envBenefits", 		"site/@siteId/envBenefits");
		put("list", 			"equipment/@siteId/list");
		put("Inventory", 		"site/@siteId/Inventory");
		put("data", 			"equipment/@siteId/@serialNumber/data");
		put("ChangeLog", 		"equipment/@siteId/@serialNumber/changeLog");
		put("accounts", 		"accounts/list");
		put("SiteSensors", 		"equipment/@siteId/sensors");
		put("siteSensors", 		"site/@siteId/sensors");
		put("version", 			"version/current");
		put("supported", 		"version/supported");
	}};
	
	public static String QUARTER_OF_AN_HOUR = "QUARTER_OF_AN_HOUR";
	
	public static Map<String, Long> TIME_UNIT_MAP = new HashMap<String, Long>() {
		private static final long serialVersionUID = -7142548851038714457L;

	{
		put("FIVE_MINUTE", new Long(5*60*1000));
		put(QUARTER_OF_AN_HOUR, new Long(15*60*1000));
		put("HALF_OF_AN_HOUR", new Long(30*60*1000));
		put("HOUR", new Long(60*60*1000));
		put("DAY", new Long(24*60*60*1000));
		put("WEEK", new Long((long)7*24*60*60*1000));
		put("MONTH", new Long((long)31*24*60*60*1000));
		put("YEAR", new Long((long)365*24*60*60*1000));
	}};
	
	public static String SITE_ID = "@siteId";
	public static String SERIAL_NUMBER = "@serialNumber";
	
	public static String TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
	public static String DATE_FORMAT = "yyyy-MM-dd";

	public final static Charset CHARSET = StandardCharsets.UTF_8;

}
