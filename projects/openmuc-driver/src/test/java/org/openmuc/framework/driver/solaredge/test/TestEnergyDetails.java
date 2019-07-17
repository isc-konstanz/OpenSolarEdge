package org.openmuc.framework.driver.solaredge.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.text.ParseException;
import java.util.TimeZone;

import org.junit.Test;
import org.openmuc.framework.data.Flag;
import org.openmuc.framework.data.Record;
import org.openmuc.framework.driver.solaredge.settings.DeviceAddress;
import org.openmuc.jsonpath.request.HttpRequest;
import org.openmuc.solaredge.SolarEdge;
import org.openmuc.solaredge.TestHandler;
import org.openmuc.solaredge.data.TimeWrapper;

public class TestEnergyDetails {

	private final static Charset CHARSET = SolarEdge.CHARSET;

	private final static TimeZone ZONE = TimeZone.getTimeZone("GMT");

	static String jsonString =  "{\r\n" + 
			"\"energyDetails\": {\r\n" + 
			"\"timeUnit\": \"WEEK\",\r\n" + 
			"\"unit\": \"Wh\",\r\n" + 
			"\"meters\": [\r\n" + 
			"{\r\n" + 
			"\"type\": \"Production\",\r\n" + 
			"\"values\": [\r\n" + 
			"{\r\n" + 
			"\"date\": \"2015-10-19 00:00:00\"\r\n" + 
			"},\r\n" + 
			"{\r\n" + 
			"\"date\": \"2015-10-26 00:00:00\",\r\n" + 
			"\"value\": null\r\n" + 
			"},\r\n" + 
			"{\r\n" + 
			"\"date\": \"2015-11-02 00:00:00\"\r\n" + 
			"},\r\n" + 
			"{\r\n" + 
			"\"date\": \"2015-11-09 00:00:00\"\r\n" + 
			"},\r\n" + 
			"{\r\n" + 
			"\"date\": \"2015-11-16 00:00:00\",\r\n" + 
			"\"value\": 2953\r\n" + 
			"}\r\n" + 
			"]\r\n" + 
			"},\r\n" + 
			"{\r\n" + 
			"\"type\": \"Consumption\",\r\n" + 
			"\"values\": [\r\n" + 
			"{\r\n" + 
			"\"date\": \"2015-10-19 00:00:00\"\r\n" + 
			"},\r\n" + 
			"{\r\n" + 
			"\"date\": \"2015-10-26 00:00:00\"\r\n" + 
			"},\r\n" + 
			"{\r\n" + 
			"\"date\": \"2015-11-02 00:00:00\"\r\n" + 
			"},\r\n" + 
			"{\r\n" + 
			"\"date\": \"2015-11-09 00:00:00\"\r\n" + 
			"},\r\n" + 
			"{\r\n" + 
			"\"date\": \"2015-11-16 00:00:00\",\r\n" + 
			"\"value\": 29885\r\n" + 
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

	static final String ADDRESS = "https://monitoringapi.solaredge.com/;249379";
	static final String SETTINGS = "authentication=L4QLVQ1LOKCQX2193VSEICXW61NP6B1O";

	static String API = "energyDetails?startTime=&endTime=&timeUnit=&api_key=";
	static String PARAMETERS = "startTime=&endTime=&timeUnit=";

	@Test
	public void testEnergyDetailsJsonGetEnergyDetails() {
		TestDriver driver = new TestDriver(jsonConnectString, jsonString);
		TestConnection connection = null;
		try {
			connection = (TestConnection) driver.connect(ADDRESS, SETTINGS);
			
		} catch (Exception e) {
			assertTrue(e.getMessage(), false);
			return;
		}
		
		Record record;
		try {
			record = connection.getTestRecord("$.energyDetails", null, 
					SolarEdge.QUARTER_OF_AN_HOUR, null);
			
		} catch (Exception e) {
			assertTrue(e.getMessage(), false);
			return;
		}
		
		TestHandler handler = connection.getHandler();
		HttpRequest request = handler.getRequest();
		try {
			DeviceAddress address = driver.getDeviceAddress();
			String requestResult = address.getAddress() + "site/" + address.getSiteId() + "/" + API;
			requestResult = handler.fillRequest(requestResult, SolarEdge.QUARTER_OF_AN_HOUR, 
					driver.getDeviceSettings().getAuthentication());
			assertEquals(requestResult, request.toString(CHARSET));
			assertEquals("yyyy-MM-dd HH:mm:ss", handler.getTimeWrapper().getFormat());
			assertEquals("yyyy-MM-dd HH:mm:ss", handler.getLastTime().getFormat());
			
			if (request.getParameters() != null) {
				String parameters = handler.fillParameters(PARAMETERS, SolarEdge.QUARTER_OF_AN_HOUR);
				assertEquals(parameters, request.parseParameters(CHARSET));
			}
		} catch (UnsupportedEncodingException e) {
			assertTrue(e.getMessage(), false);
		}
		
		assertEquals("{timeUnit=WEEK, unit=Wh, meters=[{\"type\":\"Production\",\"values\":[{\"date\":\"2015-10-19 00:00:00\"},{\"date\":\"2015-10-26 00:00:00\",\"value\":null},{\"date\":\"2015-11-02 00:00:00\"},{\"date\":\"2015-11-09 00:00:00\"},{\"date\":\"2015-11-16 00:00:00\",\"value\":2953}]},{\"type\":\"Consumption\",\"values\":[{\"date\":\"2015-10-19 00:00:00\"},{\"date\":\"2015-10-26 00:00:00\"},{\"date\":\"2015-11-02 00:00:00\"},{\"date\":\"2015-11-09 00:00:00\"},{\"date\":\"2015-11-16 00:00:00\",\"value\":29885}]}]}", record.getValue().asString());
		try {
			assertEquals(handler.getTimeWrapper().getTime(), record.getTimestamp());
			
		} catch (ParseException e) {
			assertTrue(e.getMessage(), false);
		}
		assertEquals(Flag.VALID, record.getFlag());
	}

	@Test
	public void testEnergyDetailsJsonGetProductionValue0() {
		TestDriver driver = new TestDriver(jsonConnectString, jsonString);
		TestConnection connection = null;
		try {
			connection = (TestConnection) driver.connect(ADDRESS, SETTINGS);
			
		} catch (Exception e) {
			assertTrue(e.getMessage(), false);
			return;
		}
		
		Record record;
		try {
			record = connection.getTestRecord("$.energyDetails.meters[?(@.type=='Production')].values[0].value", 
						"$.energyDetails.meters[?(@.type=='Production')].values[0].date", 
						SolarEdge.QUARTER_OF_AN_HOUR, null);
			
		} catch (Exception e) {
			assertTrue(e.getMessage(), false);
			return;
		}
		
		TestHandler responseHandler = connection.getHandler();;
 		HttpRequest request = responseHandler.getRequest();
		try {
			DeviceAddress address = driver.getDeviceAddress();
			String requestResult = address.getAddress() + "site/" + address.getSiteId() + "/" + API;
			requestResult = responseHandler.fillRequest(requestResult, SolarEdge.QUARTER_OF_AN_HOUR, 
					driver.getDeviceSettings().getAuthentication());
			assertEquals(requestResult, request.toString(CHARSET));
			assertEquals("yyyy-MM-dd HH:mm:ss", responseHandler.getTimeWrapper().getFormat());
			assertEquals("yyyy-MM-dd HH:mm:ss", responseHandler.getLastTime().getFormat());
			
			if (request.getParameters() != null) {
				String parameters = responseHandler.fillParameters(PARAMETERS, SolarEdge.QUARTER_OF_AN_HOUR);
				assertEquals(parameters, request.parseParameters(CHARSET));
			}
		} catch (UnsupportedEncodingException e) {
			assertTrue(e.getMessage(), false);
		}
		assertEquals("2015-10-19 00:00:00", new TimeWrapper(record.getTimestamp(), SolarEdge.TIME_FORMAT, ZONE).getTimeStr());
		assertEquals(null, record.getValue().asString());
		assertEquals(Flag.NO_VALUE_RECEIVED_YET, record.getFlag());
	}

	@Test
	public void testEnergyDetailsJsonGetProductionValue1() {
		TestDriver driver = new TestDriver(jsonConnectString, jsonString);
		TestConnection connection = null;
		try {
			connection = (TestConnection) driver.connect(ADDRESS, SETTINGS);
			
		} catch (Exception e) {
			assertTrue(e.getMessage(), false);
			return;
		}
		
		Record record;
		try {
			record = connection.getTestRecord("$.energyDetails.meters[?(@.type=='Production')].values[1].value", 
						"$.energyDetails.meters[?(@.type=='Production')].values[1].date", 
						SolarEdge.QUARTER_OF_AN_HOUR, null);
			
		} catch (Exception e) {
			assertTrue(e.getMessage(), false);
			return;
		}

		TestHandler handler = connection.getHandler();;
		HttpRequest request = handler.getRequest();
		try {
			DeviceAddress address = driver.getDeviceAddress();
			String requestResult = address.getAddress() + "site/" + address.getSiteId() + "/" + API;
			requestResult = handler.fillRequest(requestResult, SolarEdge.QUARTER_OF_AN_HOUR, 
					driver.getDeviceSettings().getAuthentication());
			assertEquals(requestResult, request.toString(CHARSET));
			assertEquals("yyyy-MM-dd HH:mm:ss", handler.getTimeWrapper().getFormat());
			assertEquals("yyyy-MM-dd HH:mm:ss", handler.getLastTime().getFormat());
			
			if (request.getParameters() != null) {
				String parameters = handler.fillParameters(PARAMETERS, SolarEdge.QUARTER_OF_AN_HOUR);
				assertEquals(parameters, request.parseParameters(CHARSET));
			}
		} catch (UnsupportedEncodingException e) {
			assertTrue(e.getMessage(), false);
		}
		assertEquals(null, record.getValue().asString());
 		assertEquals("2015-10-26 00:00:00", new TimeWrapper(record.getTimestamp(), SolarEdge.TIME_FORMAT, ZONE).getTimeStr());
		assertEquals(Flag.NO_VALUE_RECEIVED_YET, record.getFlag());
	}

	@Test
	public void testEnergyDetailsJsonGetProductionValueLast() {
		TestDriver driver = new TestDriver(jsonConnectString, jsonString);
		TestConnection connection = null;
		try {
			connection = (TestConnection) driver.connect(ADDRESS, SETTINGS);
			
		} catch (Exception e) {
			assertTrue(e.getMessage(), false);
			return;
		}
		
		Record record;
		try {
			record = connection.getTestRecord("energyDetails Production", "timeUnit="+SolarEdge.QUARTER_OF_AN_HOUR);
			
		} catch (Exception e) {
			assertTrue(e.getMessage(), false);
			return;
		}
		
		TestHandler responseHandler = connection.getHandler();
		HttpRequest request = responseHandler.getRequest();
		try {
			DeviceAddress address = driver.getDeviceAddress();
			String requestResult = address.getAddress() + "site/" + address.getSiteId() + "/" + API;
			requestResult = responseHandler.fillRequest(requestResult, SolarEdge.QUARTER_OF_AN_HOUR, 
					driver.getDeviceSettings().getAuthentication());
			
			assertEquals(requestResult, request.toString(CHARSET));
			assertEquals("yyyy-MM-dd HH:mm:ss", responseHandler.getTimeWrapper().getFormat());
			assertEquals("yyyy-MM-dd HH:mm:ss", responseHandler.getLastTime().getFormat());
			
			if (request.getParameters() != null) {
				String parameters = responseHandler.fillParameters(PARAMETERS, SolarEdge.QUARTER_OF_AN_HOUR);
				assertEquals(parameters, request.parseParameters(CHARSET));
			}
		} catch (UnsupportedEncodingException e) {
			assertTrue(e.getMessage(), false);
		}
 		assertEquals("2015-11-16 00:00:00", new TimeWrapper(record.getTimestamp(), SolarEdge.TIME_FORMAT, ZONE).getTimeStr());
		assertEquals(2953, record.getValue().asInt());
		assertEquals(Flag.VALID, record.getFlag());
	}
}
