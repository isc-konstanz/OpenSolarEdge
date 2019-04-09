package org.openmuc.framework.driver.solaredge.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.TimeZone;

import org.junit.Test;
import org.openmuc.framework.data.Flag;
import org.openmuc.framework.data.Record;
import org.openmuc.framework.driver.solaredge.settings.DeviceAddress;
import org.openmuc.jsonpath.request.HttpRequest;
import org.openmuc.solaredge.TestHandler;
import org.openmuc.solaredge.config.SolarEdgeConst;
import org.openmuc.solaredge.data.TimeWrapper;

public class TestStorageData {

	private final static Charset CHARSET = SolarEdgeConst.CHARSET;

	private final static TimeZone ZONE = TimeZone.getTimeZone("GMT");

	static String jsonString =  "{\r\n" + 
			"\"storageData\": {\r\n" + 
			"\"batteryCount\": 1,\r\n" + 
			"\"batteries\": [\r\n" + 
			"{\r\n" + 
			"\"nameplate\": 1,\r\n" + 
			"\"serialNumber\": \"BFA\",\r\n" + 
			"\"telemetryCount\": 9,\r\n" + 
			"\"telemetries\": [\r\n" + 
			"{\r\n" + 
			"\"timeStamp\": \"2015-10-13 08:00:00\",\r\n" + 
			"\"power\": 12,\r\n" + 
			"\"batteryState\": 3,\r\n" + 
			"\"lifeTimeEnergyDischarged\": 6\r\n" + 
			"},\r\n" + 
			"{\r\n" + 
			"\"timeStamp\": \"2015-10-13 08:05:00\",\r\n" + 
			"\"power\": 12,\r\n" + 
			"\"batteryState\": 3,\r\n" + 
			"\"lifeTimeEnergyDischarged\": 6\r\n" + 
			"},\r\n" + 
			"{\r\n" + 
			"\"timeStamp\": \"2015-10-13 08:10:00\",\r\n" + 
			"\"power\": 12,\r\n" + 
			"\"batteryState\": 3,\r\n" + 
			"\"lifeTimeEnergyDischarged\": 6\r\n" + 
			"},\r\n" + 
			"{\r\n" + 
			"\"timeStamp\": \"2015-10-13 08:15:00\",\r\n" + 
			"\"power\": 12,\r\n" + 
			"\"batteryState\": 3,\r\n" + 
			"\"lifeTimeEnergyDischarged\": 6\r\n" + 
			"},\r\n" + 
			"{\r\n" + 
			"\"timeStamp\": \"2015-10-13 08:20:00\",\r\n" + 
			"\"power\": 12,\r\n" + 
			"\"batteryState\": 3,\r\n" + 
			"\"lifeTimeEnergyDischarged\": 6\r\n" + 
			"},\r\n" + 
			"{\r\n" + 
			"\"timeStamp\": \"2015-10-13 08:25:00\",\r\n" + 
			"\"power\": 12,\r\n" + 
			"\"batteryState\": 3,\r\n" + 
			"\"lifeTimeEnergyDischarged\": 6\r\n" + 
			"},\r\n" + 
			"{\r\n" + 
			"\"timeStamp\": \"2015-10-13 08:30:00\",\r\n" + 
			"\"power\": 12,\r\n" + 
			"\"batteryState\": 3,\r\n" + 
			"\"lifeTimeEnergyDischarged\": 6\r\n" + 
			"},\r\n" + 
			"{\r\n" + 
			"\"timeStamp\": \"2015-10-13 08:35:00\",\r\n" + 
			"\"power\": 12,\r\n" + 
			"\"batteryState\": 3,\r\n" + 
			"\"lifeTimeEnergyDischarged\": 6\r\n" + 
			"},\r\n" + 
			"{\r\n" + 
			"\"timeStamp\": \"2015-10-13 08:40:00\",\r\n" + 
			"\"power\": 12,\r\n" + 
			"\"batteryState\": 3,\r\n" + 
			"\"lifeTimeEnergyDischarged\": 6\r\n" + 
			"}\r\n" + 
			"]\r\n" + 
			"}\r\n" + 
			"]\r\n" + 
			"}\r\n" + 
			"}";

	static String jsonConnectString =  "{\r\n" + 
			"\"details\":{\r\n" + 
			"\"id\":0,\r\n" + 
			"\"name\":\"site name\",\r\n" + 
			"\"accountId\":0,\r\n" + 
			"\"status\":\"Active\",\r\n" + 
			"\"peakPower\":9.8,\r\n" + 
			"\"currency\":\"EUR\",\r\n" + 
			"\"installationDate\":\"2012-08-16 00:00:00\",\r\n" + 
			"\"ptoDate\":\"2017-05-11\",\r\n" + 
			"\"notes\":\"my notes\",\r\n" + 
			"\"type\":\"Optimizers & Inverters\",\r\n" + 
			"\"location\":{\r\n" + 
			"\"country\":\"my country\",\r\n" + 
			"\"state\":\"my state\",\r\n" + 
			"\"city\":\"my city\",\r\n" + 
			"\"address\":\"my address\",\r\n" + 
			"\"address2\":\"\",\r\n" + 
			"\"zip\":\"0000\",\r\n" + 
			"\"timeZone\":\"GMT\"\r\n" + 
			"},\r\n" + 
			"\"alertQuantity\":0,\r\n" + 
			"\"alertSeverity\":\"NONE\",\r\n" + 
			"\"uris\":{\r\n" + 
			"\"IMAGE_URI\":\"site image uri\"\r\n" + 
			"},\r\n" + 
			"\"publicSettings\":{\r\n" + 
			"\"name\":null,\r\n" + 
			"\"isPublic\":false\r\n" + 
			"}\r\n" + 
			"}\r\n" + 
			"}";
	
	static final String ADDRESS = "https://monitoringapi.solaredge.com;249379";
    static final String SETTINGS = "authentication=L4QLVQ1LOKCQX2193VSEICXW61NP6B1O";

	static String API = "storageData/?startTime=&endTime=&api_key=";
	static String PARAMETERS = "startTime=&endTime=&serialNumber=";
	
	@Test
    public void testStorageDataJsonGetPower0() {
        String testMethodName = "testStorageDataJsonGetPower0";
        System.out.println(testMethodName);

        TestDriver driver = new TestDriver(jsonConnectString, jsonString);
        TestHandler responseHandler = null;
        TestConnection connection = null;
        try {
        	connection = (TestConnection) driver.connect(ADDRESS, SETTINGS);
        	responseHandler = (TestHandler)connection.getHandler();
		} catch (Exception e) {
			assertTrue(e.getMessage(), false);
			return;
		}
        
        Record rec;
		try {
			rec = connection.getRecordForTest("$.storageData.batteries[?(@.serialNumber=='@serialNumber')].telemetries[0].power", 
						"$.storageData.batteries[?(@.serialNumber=='@serialNumber')].telemetries[0].timeStamp",
						"FIVE_MINUTE", "BFA");
		} catch (Exception e) {
			assertTrue(e.getMessage(), false);
			return;
		}
 		HttpRequest request = responseHandler.getRequest();
		try {
			String requestStr = request.getRequest(CHARSET); 
			System.out.println(requestStr);
			DeviceAddress address = driver.getDeviceAddress();
			String resultRequest = address.getAddress() + "/site/" + address.getSiteId() + "/" + API;
			resultRequest = responseHandler.fillRequest(resultRequest, null, 
					driver.getDeviceSettings().getAuthentication());
			assertEquals(resultRequest, requestStr);
			String format = responseHandler.getTimeWrapper().getFormat();
			assertEquals("yyyy-MM-dd HH:mm:ss", format);
			format = responseHandler.getLastTime().getFormat();
			assertEquals("yyyy-MM-dd HH:mm:ss", format);
			if (request.getParameters() != null) {
				String params = responseHandler.fillParameters(PARAMETERS, null);
				String requestParams = request.parseParameters(CHARSET);
				assertEquals(params, requestParams);
				System.out.println(requestParams);
			}
		} catch (UnsupportedEncodingException e) {
			assertTrue(e.getMessage(), false);
		}
        assertEquals("12", rec.getValue().asString());
		assertEquals("2015-10-13 08:00:00", new TimeWrapper(rec.getTimestamp(), SolarEdgeConst.TIME_FORMAT, ZONE).getTimeStr());
        assertEquals(Flag.VALID, rec.getFlag());
	}

	@Test
    public void testStorageDataJsonGetBatteryStateLast() {
        String testMethodName = "testStorageDataJsonGetBatteryStateLast";
        System.out.println(testMethodName);

        TestDriver driver = new TestDriver(jsonConnectString, jsonString);
        TestHandler responseHandler = null;
        TestConnection connection = null;
        try {
        	connection = (TestConnection) driver.connect(ADDRESS, SETTINGS);
        	responseHandler = (TestHandler)connection.getHandler();
		} catch (Exception e) {
			e.printStackTrace();
			assertTrue(false);
			return;
		}
        
        Record rec;
		try {
			rec = connection.getRecordForTest("storageData batteryState", 
						"timeUnit=FIVE_MINUTE;serialNumber=BFA");
		} catch (Exception e) {
			assertTrue(e.getMessage(), false);
			return;
		}
 		HttpRequest request = responseHandler.getRequest();
		try {
			String requestStr = request.getRequest(CHARSET); 
			System.out.println(requestStr);
			DeviceAddress address = driver.getDeviceAddress();
			String resultRequest = address.getAddress() + "/site/" + address.getSiteId() + "/" + API;
			resultRequest = responseHandler.fillRequest(resultRequest, null, 
					driver.getDeviceSettings().getAuthentication());
			assertEquals(resultRequest, requestStr);
			String format = responseHandler.getTimeWrapper().getFormat();
			assertEquals("yyyy-MM-dd HH:mm:ss", format);
			format = responseHandler.getLastTime().getFormat();
			assertEquals("yyyy-MM-dd HH:mm:ss", format);
			if (request.getParameters() != null) {
				String params = responseHandler.fillParameters(PARAMETERS, null);
				String requestParams = request.parseParameters(CHARSET);
				assertEquals(params, requestParams);
				System.out.println(requestParams);
			}
		} catch (UnsupportedEncodingException e) {
			assertTrue(e.getMessage(), false);
		}
        assertEquals("3", rec.getValue().asString());
		assertEquals("2015-10-13 08:40:00", new TimeWrapper(rec.getTimestamp(), SolarEdgeConst.TIME_FORMAT, ZONE).getTimeStr());
        assertEquals(Flag.VALID, rec.getFlag());
	}

	@Test
    public void testStorageDataBattery0JsonGetLifeTimeEnergyChargedLast() {
        String testMethodName = "testStorageDataBattery0JsonGetLifeTimeEnergyChargedLast";
        System.out.println(testMethodName);

        TestDriver driver = new TestDriver(jsonConnectString, jsonString);
        TestHandler responseHandler = null;
        TestConnection connection = null;
        try {
        	connection = (TestConnection) driver.connect(ADDRESS, SETTINGS);
        	responseHandler = (TestHandler)connection.getHandler();
		} catch (Exception e) {
			assertTrue(e.getMessage(), false);
			return;
		}
        
        Record rec;
		try {
			rec = connection.getRecordForTest("storageData lifeTimeEnergyDischarged", 
						"timeUnit=FIVE_MINUTE");
		} catch (Exception e) {
			e.printStackTrace();
			assertTrue(false);
			return;
		}
 		HttpRequest request = responseHandler.getRequest();
		try {
			String requestStr = request.getRequest(CHARSET); 
			System.out.println(requestStr);
			DeviceAddress address = driver.getDeviceAddress();
			String resultRequest = address.getAddress() + "/site/" + address.getSiteId() + "/" + API;
			resultRequest = responseHandler.fillRequest(resultRequest, null, 
					driver.getDeviceSettings().getAuthentication());
			assertEquals(resultRequest, requestStr);
			String format = responseHandler.getTimeWrapper().getFormat();
			assertEquals("yyyy-MM-dd HH:mm:ss", format);
			format = responseHandler.getLastTime().getFormat();
			assertEquals("yyyy-MM-dd HH:mm:ss", format);
			if (request.getParameters() != null) {
				String params = responseHandler.fillParameters(PARAMETERS, null);
				String requestParams = request.parseParameters(CHARSET);
				assertEquals(params, requestParams);
				System.out.println(requestParams);
			}
		} catch (UnsupportedEncodingException e) {
			assertTrue(e.getMessage(), false);
		}
        assertEquals("6", rec.getValue().asString());
		assertEquals("2015-10-13 08:40:00", new TimeWrapper(rec.getTimestamp(), SolarEdgeConst.TIME_FORMAT, ZONE).getTimeStr());
        assertEquals(Flag.VALID, rec.getFlag());
	}
}
