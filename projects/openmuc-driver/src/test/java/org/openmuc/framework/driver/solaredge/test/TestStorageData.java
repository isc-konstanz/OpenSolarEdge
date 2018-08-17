package org.openmuc.framework.driver.solaredge.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;

import org.junit.Test;
import org.openmuc.framework.data.Flag;
import org.openmuc.framework.data.Record;
import org.openmuc.framework.driver.solaredge.SolarEdgeConnection;
import org.openmuc.framework.driver.solaredge.settings.DeviceAddress;
import org.openmuc.jsonpath.request.HttpRequest;
import org.openmuc.solaredge.SolarEdgeConst;
import org.openmuc.solaredge.TestSolarEdgeResponseHandler;
import org.openmuc.solaredge.data.TimeWrapper;

public class TestStorageData {

	private final static Charset CHARSET = SolarEdgeConst.CHARSET;

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
			"\"lifeTimeEnergyCharged\": 6\r\n" + 
			"},\r\n" + 
			"{\r\n" + 
			"\"timeStamp\": \"2015-10-13 08:05:00\",\r\n" + 
			"\"power\": 12,\r\n" + 
			"\"batteryState\": 3,\r\n" + 
			"\"lifeTimeEnergyCharged\": 6\r\n" + 
			"},\r\n" + 
			"{\r\n" + 
			"\"timeStamp\": \"2015-10-13 08:10:00\",\r\n" + 
			"\"power\": 12,\r\n" + 
			"\"batteryState\": 3,\r\n" + 
			"\"lifeTimeEnergyCharged\": 6\r\n" + 
			"},\r\n" + 
			"{\r\n" + 
			"\"timeStamp\": \"2015-10-13 08:15:00\",\r\n" + 
			"\"power\": 12,\r\n" + 
			"\"batteryState\": 3,\r\n" + 
			"\"lifeTimeEnergyCharged\": 6\r\n" + 
			"},\r\n" + 
			"{\r\n" + 
			"\"timeStamp\": \"2015-10-13 08:20:00\",\r\n" + 
			"\"power\": 12,\r\n" + 
			"\"batteryState\": 3,\r\n" + 
			"\"lifeTimeEnergyCharged\": 6\r\n" + 
			"},\r\n" + 
			"{\r\n" + 
			"\"timeStamp\": \"2015-10-13 08:25:00\",\r\n" + 
			"\"power\": 12,\r\n" + 
			"\"batteryState\": 3,\r\n" + 
			"\"lifeTimeEnergyCharged\": 6\r\n" + 
			"},\r\n" + 
			"{\r\n" + 
			"\"timeStamp\": \"2015-10-13 08:30:00\",\r\n" + 
			"\"power\": 12,\r\n" + 
			"\"batteryState\": 3,\r\n" + 
			"\"lifeTimeEnergyCharged\": 6\r\n" + 
			"},\r\n" + 
			"{\r\n" + 
			"\"timeStamp\": \"2015-10-13 08:35:00\",\r\n" + 
			"\"power\": 12,\r\n" + 
			"\"batteryState\": 3,\r\n" + 
			"\"lifeTimeEnergyCharged\": 6\r\n" + 
			"},\r\n" + 
			"{\r\n" + 
			"\"timeStamp\": \"2015-10-13 08:40:00\",\r\n" + 
			"\"power\": 12,\r\n" + 
			"\"batteryState\": 3,\r\n" + 
			"\"lifeTimeEnergyCharged\": 6\r\n" + 
			"}\r\n" + 
			"]\r\n" + 
			"}\r\n" + 
			"]\r\n" + 
			"}\r\n" + 
			"}";

	static final String ADDRESS = "https://monitoringapi.solaredge.com;249379";
    static final String SETTINGS = "authentication=L4QLVQ1LOKCQX2193VSEICXW61NP6B1O";

	static String API = "storageData/?startTime=&endTime=&api_key=";
	static String PARAMETERS = "startTime=&endTime=&serialNumber=";
	
	@Test
    public void test_StorageDataJsonGetPower0() {
        String testMethodName = "test_StorageDataJsonGetPower0";
        System.out.println(testMethodName);

        TestSolarEdgeDriver driver = new TestSolarEdgeDriver(jsonString);
        TestSolarEdgeResponseHandler responseHandler = null;
        TestSolarEdgeConnection connection = null;
        try {
        	connection = (TestSolarEdgeConnection) driver.connect(ADDRESS, SETTINGS);
        	responseHandler = (TestSolarEdgeResponseHandler)connection.getResponseHandler();
		} catch (Exception e) {
			e.printStackTrace();
			assertTrue(false);
			return;
		}
        
        Record rec = connection.getRecordForTest("$.storageData.batteries[?(@.serialNumber=='@serialNumber')].telemetries[0].power", 
					"$.storageData.batteries[?(@.serialNumber=='@serialNumber')].telemetries[0].timeStamp",
					"FIVE_MINUTE", "BFA");
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
			e.printStackTrace();
			assertTrue(false);
		}
		System.out.println(SolarEdgeConnection.recordToString(rec));
        assertEquals("12", rec.getValue().asString());
		assertEquals("2015-10-13 08:00:00", 
					new TimeWrapper(rec.getTimestamp(), SolarEdgeConst.TIME_FORMAT).getTimeStr());
        assertEquals(Flag.VALID, rec.getFlag());
	}

	@Test
    public void test_StorageDataJsonGetBatteryStateLast() {
        String testMethodName = "test_StorageDataJsonGetBatteryStateLast";
        System.out.println(testMethodName);

        TestSolarEdgeDriver driver = new TestSolarEdgeDriver(jsonString);
        TestSolarEdgeResponseHandler responseHandler = null;
        TestSolarEdgeConnection connection = null;
        try {
        	connection = (TestSolarEdgeConnection) driver.connect(ADDRESS, SETTINGS);
        	responseHandler = (TestSolarEdgeResponseHandler)connection.getResponseHandler();
		} catch (Exception e) {
			e.printStackTrace();
			assertTrue(false);
			return;
		}
        
        Record rec = connection.getRecordForTest("storageData batteryState", 
					"timeUnit=FIVE_MINUTE;serialNumber=BFA");
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
			e.printStackTrace();
			assertTrue(false);
		}
		System.out.println(SolarEdgeConnection.recordToString(rec));
        assertEquals("3", rec.getValue().asString());
		assertEquals("2015-10-13 08:40:00", 
					new TimeWrapper(rec.getTimestamp(), SolarEdgeConst.TIME_FORMAT).getTimeStr());
        assertEquals(Flag.VALID, rec.getFlag());
	}

	@Test
    public void test_StorageDataBattery0JsonGetLifeTimeEnergyChargedLast() {
        String testMethodName = "test_StorageDataBattery0JsonGetLifeTimeEnergyChargedLast";
        System.out.println(testMethodName);

        TestSolarEdgeDriver driver = new TestSolarEdgeDriver(jsonString);
        TestSolarEdgeResponseHandler responseHandler = null;
        TestSolarEdgeConnection connection = null;
        try {
        	connection = (TestSolarEdgeConnection) driver.connect(ADDRESS, SETTINGS);
        	responseHandler = (TestSolarEdgeResponseHandler)connection.getResponseHandler();
		} catch (Exception e) {
			e.printStackTrace();
			assertTrue(false);
			return;
		}
        
        Record rec = connection.getRecordForTest("storageData lifeTimeEnergyDischarged", 
					"timeUnit=FIVE_MINUTE");
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
			e.printStackTrace();
			assertTrue(false);
		}
		System.out.println(SolarEdgeConnection.recordToString(rec));
        assertEquals("6", rec.getValue().asString());
		assertEquals("2015-10-13 08:40:00", 
					new TimeWrapper(rec.getTimestamp(), SolarEdgeConst.TIME_FORMAT).getTimeStr());
        assertEquals(Flag.VALID, rec.getFlag());
	}

}
