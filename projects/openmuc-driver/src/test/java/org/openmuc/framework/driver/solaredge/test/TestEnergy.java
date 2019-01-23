package org.openmuc.framework.driver.solaredge.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.text.ParseException;

import org.junit.Test;
import org.openmuc.framework.data.Flag;
import org.openmuc.framework.data.Record;
import org.openmuc.framework.driver.solaredge.SolarEdgeConnection;
import org.openmuc.framework.driver.solaredge.settings.DeviceAddress;
import org.openmuc.jsonpath.request.HttpRequest;
import org.openmuc.solaredge.SolarEdgeConst;
import org.openmuc.solaredge.TestSolarEdgeResponseHandler;
import org.openmuc.solaredge.data.TimeWrapper;

public class TestEnergy {

	private final static Charset CHARSET = SolarEdgeConst.CHARSET;

	static String jsonString =  "{\r\n" + 
    		" \"energy\":{ \r\n" + 
    		"  \"timeUnit\":\"DAY\", \r\n" + 
    		"  \"unit\":\"Wh\", \r\n" + 
    		"  \"values\":[{ \r\n" + 
    		"   \"date\":\"2013-06-01 00:00:00\", \r\n" + 
    		"   \"value\":null \r\n" + 
    		"  }, \r\n" + 
    		"  { \r\n" + 
    		"   \"date\":\"2013-06-02 00:00:00\", \r\n" + 
    		"   \"value\":null \r\n" + 
    		"  }, \r\n" + 
    		"  { \r\n" + 
    		"   \"date\":\"2013-06-03 00:00:00\", \r\n" + 
    		"   \"value\":null \r\n" + 
    		"  }, \r\n" + 
    		"  { \r\n" + 
    		"   \"date\":\"2013-06-04 00:00:00\", \r\n" + 
    		"   \"value\":67313.24 \r\n" + 
    		"  }] \r\n" + 
    		" } \r\n" + 
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

	static final String API = "energy/?startDate=&endDate=&timeUnit=&api_key=";
	static final String PARAMETERS = "startDate=&endDate=&timeUnit=";
    
    
	@Test
    public void test_EnergyJsonGetEnergy() {
        String testMethodName = "test_EnergyJsonGetEnergy";
        System.out.println(testMethodName);

        TestSolarEdgeDriver driver = new TestSolarEdgeDriver(jsonConnectString, jsonString);
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
        
        Record rec;
		try {
			rec = connection.getRecordForTest("$.energy", null, SolarEdgeConst.QUARTER_OF_AN_HOUR, null);
		} catch (Exception e) {
			e.printStackTrace();
			assertTrue(false);
			return;
		}
		System.out.println(SolarEdgeConnection.recordToString(rec));
        assertEquals("{timeUnit=DAY, unit=Wh, values=[{\"date\":\"2013-06-01 00:00:00\",\"value\":null},{\"date\":\"2013-06-02 00:00:00\",\"value\":null},{\"date\":\"2013-06-03 00:00:00\",\"value\":null},{\"date\":\"2013-06-04 00:00:00\",\"value\":67313.24}]}", rec.getValue().asString());
        try {
			assertEquals(responseHandler.getTimeWrapper().getTime(), rec.getTimestamp());
		} catch (ParseException e) {
			e.printStackTrace();
			assertTrue(false);
		}
         assertEquals(Flag.VALID, rec.getFlag());
	}
	
	@Test
    public void test_EnergyJsonGetValueLast() {
        String testMethodName = "test_EnergyJsonGetEnergy";
        System.out.println(testMethodName);
        
        TestSolarEdgeDriver driver = new TestSolarEdgeDriver(jsonConnectString, jsonString);
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

        Record rec;
		try {
			rec = connection.getRecordForTest("$.energy.values[-1].value", 
						"$.energy.values[-1].date", SolarEdgeConst.QUARTER_OF_AN_HOUR, null);
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
			resultRequest = responseHandler.fillRequest(resultRequest, SolarEdgeConst.QUARTER_OF_AN_HOUR, 
					driver.getDeviceSettings().getAuthentication());
			assertEquals(resultRequest, requestStr);
			String format = responseHandler.getTimeWrapper().getFormat();
			assertEquals("yyyy-MM-dd", format);
			format = responseHandler.getLastTime().getFormat();
			assertEquals("yyyy-MM-dd", format);
			if (request.getParameters() != null) {
				String params = responseHandler.fillParameters(PARAMETERS, SolarEdgeConst.QUARTER_OF_AN_HOUR);
				String requestParams = request.parseParameters(CHARSET);
				assertEquals(params, requestParams);
				System.out.println(requestParams);
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			assertTrue(false);
		}
		System.out.println(SolarEdgeConnection.recordToString(rec));
        assertEquals("67313.24", rec.getValue().asString());
 		assertEquals("2013-06-04 00:00:00", 
 					new TimeWrapper(rec.getTimestamp(), SolarEdgeConst.TIME_FORMAT).getTimeStr());
        assertEquals(Flag.VALID, rec.getFlag());
        
	}
	
}
