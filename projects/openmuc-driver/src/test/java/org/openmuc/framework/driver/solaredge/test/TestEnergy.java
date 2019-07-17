package org.openmuc.framework.driver.solaredge.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.text.ParseException;

import org.junit.Test;
import org.openmuc.framework.data.Flag;
import org.openmuc.framework.data.Record;
import org.openmuc.framework.driver.solaredge.settings.DeviceAddress;
import org.openmuc.jsonpath.request.HttpRequest;
import org.openmuc.solaredge.SolarEdge;
import org.openmuc.solaredge.TestHandler;
import org.openmuc.solaredge.data.TimeWrapper;

public class TestEnergy {

	private final static Charset CHARSET = SolarEdge.CHARSET;

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

	static final String ADDRESS = "https://monitoringapi.solaredge.com/;249379";
	static final String SETTINGS = "authentication=L4QLVQ1LOKCQX2193VSEICXW61NP6B1O";

	static final String API = "energy?startDate=&endDate=&timeUnit=&api_key=";
	static final String PARAMETERS = "startDate=&endDate=&timeUnit=";

	@Test
	public void testEnergyJsonGetEnergy() {
		TestDriver driver = new TestDriver(jsonConnectString, jsonString);
		TestConnection connection = null;
		try {
			connection = (TestConnection) driver.connect(ADDRESS, SETTINGS);
			
		} catch (Exception e) {
			assertTrue(e.toString(), false);
			return;
		}
		
		Record rec;
		try {
			rec = connection.getTestRecord("$.energy", null, SolarEdge.QUARTER_OF_AN_HOUR, null);
		} catch (Exception e) {
			assertTrue(e.toString(), false);
			return;
		}
		assertEquals("{timeUnit=DAY, unit=Wh, values=[{\"date\":\"2013-06-01 00:00:00\",\"value\":null},{\"date\":\"2013-06-02 00:00:00\",\"value\":null},{\"date\":\"2013-06-03 00:00:00\",\"value\":null},{\"date\":\"2013-06-04 00:00:00\",\"value\":67313.24}]}", rec.getValue().asString());
		try {
			assertEquals(connection.getHandler().getTimeWrapper().getTime(), rec.getTimestamp());
		} catch (ParseException e) {
			assertTrue(e.toString(), false);
		}
		 assertEquals(Flag.VALID, rec.getFlag());
	}
	
	@Test
	public void testEnergyJsonGetValueLast() {
		TestDriver driver = new TestDriver(jsonConnectString, jsonString);
		TestConnection connection = null;
		try {
			connection = (TestConnection) driver.connect(ADDRESS, SETTINGS);
			
		} catch (Exception e) {
			assertTrue(e.toString(), false);
			return;
		}
		
		Record record;
		try {
			record = connection.getTestRecord("$.energy.values[-1].value", 
						"$.energy.values[-1].date", SolarEdge.QUARTER_OF_AN_HOUR, null);
		
		} catch (Exception e) {
			assertTrue(e.toString(), false);
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
			assertEquals("yyyy-MM-dd", handler.getTimeWrapper().getFormat());
			assertEquals("yyyy-MM-dd", handler.getLastTime().getFormat());
			
			if (request.getParameters() != null) {
				String parameters = handler.fillParameters(PARAMETERS, SolarEdge.QUARTER_OF_AN_HOUR);
				assertEquals(parameters, request.parseParameters(CHARSET));
			}
		} catch (UnsupportedEncodingException e) {
			assertTrue(e.toString(), false);
		}
 		assertEquals("2013-06-04 00:00:00", new TimeWrapper(record.getTimestamp(), SolarEdge.TIME_FORMAT, handler.getSiteZone()).getTimeStr());
		assertEquals("67313.24", record.getValue().asString());
		assertEquals(Flag.VALID, record.getFlag());
	}

}
