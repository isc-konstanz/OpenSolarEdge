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
import org.openmuc.solaredge.SolarEdgeConst;
import org.openmuc.solaredge.core.TestSolarEdgeResponseHandler;
import org.openmuc.solaredge.data.TimeWrapper;
import org.openmuc.http.data.TimeValue;
import org.openmuc.http.request.HttpRequest;

public class TestEnergyDetails {
	
	private final static Charset CHARSET = SolarEdgeConst.CHARSET;

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
	static String REQUEST = "http://monitoringapi.solaredge.com/site/78467/energydetails/";
	static String PARAMETERS = "startTime=&endTime=&timeUnit=&api_key=";
	
 	private static TestSolarEdgeResponseHandler HANDLER = 
 			new TestSolarEdgeResponseHandler(jsonString);
 	
	@Test
    public void test_EnergyDetailsJsonGetEnergyDetails() {
        String testMethodName = "test_EnergyDetailsJsonGetEnergyDetails";
        System.out.println(testMethodName);

        TimeValue timeValuePair = null;
		try {
			timeValuePair = HANDLER.getTimeValuePair("$.energyDetails", null, SolarEdgeConst.QUARTER_OF_AN_HOUR, null);
		} catch (ParseException e) {
			e.printStackTrace();
			assertTrue(false);
		}
		HttpRequest request = HANDLER.getRequest();
		try {
			String requestStr = request.getRequest(CHARSET); 
			System.out.println(requestStr);
			HANDLER.getSiteId();
			assertEquals(REQUEST, requestStr);
			if (request.getParameters() != null) {
				String params = HANDLER.fillParameters(PARAMETERS, SolarEdgeConst.QUARTER_OF_AN_HOUR);
				String requestParams = request.parseParameters(CHARSET);
				assertEquals(params, requestParams);
				System.out.println(requestParams);
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			assertTrue(false);
		}
		Record rec = SolarEdgeConnection.timeValuePairToRecord(timeValuePair);
		System.out.println(SolarEdgeConnection.recordToString(rec));
        assertEquals("{timeUnit=WEEK, unit=Wh, meters=[{\"type\":\"Production\",\"values\":[{\"date\":\"2015-10-19 00:00:00\"},{\"date\":\"2015-10-26 00:00:00\",\"value\":null},{\"date\":\"2015-11-02 00:00:00\"},{\"date\":\"2015-11-09 00:00:00\"},{\"date\":\"2015-11-16 00:00:00\",\"value\":2953}]},{\"type\":\"Consumption\",\"values\":[{\"date\":\"2015-10-19 00:00:00\"},{\"date\":\"2015-10-26 00:00:00\"},{\"date\":\"2015-11-02 00:00:00\"},{\"date\":\"2015-11-09 00:00:00\"},{\"date\":\"2015-11-16 00:00:00\",\"value\":29885}]}]}", rec.getValue().asString());
        try {
			assertEquals(HANDLER.getTimeWrapper().getTime(), rec.getTimestamp());
		} catch (ParseException e) {
			e.printStackTrace();
			assertTrue(false);
		}
         assertEquals(Flag.VALID, rec.getFlag());
	}
	
	@Test
	public void test_EnergyDetailsJsonGetProductionValue0() {
        String testMethodName = "test_EnergyDetailsJsonGetProductionValue0";
        System.out.println(testMethodName);

        TimeValue timeValuePair = null;
 		try {
 			timeValuePair = HANDLER.getTimeValuePair("$.energyDetails.meters[?(@.type=='Production')].values[0].value", 
					"$.energyDetails.meters[?(@.type=='Production')].values[0].date", 
					SolarEdgeConst.QUARTER_OF_AN_HOUR, null);
		} catch (ParseException e) {
			e.printStackTrace();
			assertTrue(false);
		}
		HttpRequest request = HANDLER.getRequest();
		try {
			String requestStr = request.getRequest(CHARSET); 
			System.out.println(requestStr);
			assertEquals(REQUEST, requestStr);
			if (request.getParameters() != null) {
				String params = HANDLER.fillParameters(PARAMETERS, SolarEdgeConst.QUARTER_OF_AN_HOUR);
				String requestParams = request.parseParameters(CHARSET);
				assertEquals(params, requestParams);
				System.out.println(requestParams);
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			assertTrue(false);
		}
		Record rec = SolarEdgeConnection.timeValuePairToRecord(timeValuePair);
		System.out.println(SolarEdgeConnection.recordToString(rec));
        assertEquals(null, rec.getValue().asString());
		assertEquals("2015-10-19 00:00:00", 
					new TimeWrapper(rec.getTimestamp(), SolarEdgeConst.TIME_FORMAT).getTimeStr());
        assertEquals(Flag.NO_VALUE_RECEIVED_YET, rec.getFlag());
	}
	
	@Test
	public void test_EnergyDetailsJsonGetProductionValue1() {
        String testMethodName = "test_EnergyDetailsJsonGetProductionValue1";
        System.out.println(testMethodName);

        TimeValue timeValuePair = null;
  		try {
  			timeValuePair = HANDLER.getTimeValuePair("$.energyDetails.meters[?(@.type=='Production')].values[1].value", 
 					"$.energyDetails.meters[?(@.type=='Production')].values[1].date", 
 					SolarEdgeConst.QUARTER_OF_AN_HOUR, null);
 		} catch (ParseException e) {
 			e.printStackTrace();
 			assertTrue(false);
 		}
		HttpRequest request = HANDLER.getRequest();
		try {
			String requestStr = request.getRequest(CHARSET); 
			System.out.println(requestStr);
			assertEquals(REQUEST, requestStr);
			if (request.getParameters() != null) {
				String params = HANDLER.fillParameters(PARAMETERS, SolarEdgeConst.QUARTER_OF_AN_HOUR);
				String requestParams = request.parseParameters(CHARSET);
				assertEquals(params, requestParams);
				System.out.println(requestParams);
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			assertTrue(false);
		}
		Record rec = SolarEdgeConnection.timeValuePairToRecord(timeValuePair);
		System.out.println(SolarEdgeConnection.recordToString(rec));
        assertEquals(null, rec.getValue().asString());
 		assertEquals("2015-10-26 00:00:00", 
 					new TimeWrapper(rec.getTimestamp(), SolarEdgeConst.TIME_FORMAT).getTimeStr());
        assertEquals(Flag.NO_VALUE_RECEIVED_YET, rec.getFlag());
	}
	
	@Test
    public void test_EnergyDetailsJsonGetProductionValueLast() {
        String testMethodName = "test_EnergyDetailsJsonGetProductionValueLast";
        System.out.println(testMethodName);

        TimeValue timeValuePair = null;
  		try {
  			timeValuePair = HANDLER.getTimeValuePair("$.energyDetails.meters[?(@.type=='Production')].values[-1].value", 
 					"$.energyDetails.meters[?(@.type=='Production')].values[-1].date", 
 					SolarEdgeConst.QUARTER_OF_AN_HOUR, null);
 		} catch (ParseException e) {
 			e.printStackTrace();
 			assertTrue(false);
 		}
		HttpRequest request = HANDLER.getRequest();
		try {
			String requestStr = request.getRequest(CHARSET); 
			System.out.println(requestStr);
			assertEquals(REQUEST, requestStr);
			if (request.getParameters() != null) {
				String params = HANDLER.fillParameters(PARAMETERS, SolarEdgeConst.QUARTER_OF_AN_HOUR);
				String requestParams = request.parseParameters(CHARSET);
				assertEquals(params, requestParams);
				System.out.println(requestParams);
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			assertTrue(false);
		}
		Record rec = SolarEdgeConnection.timeValuePairToRecord(timeValuePair);
		System.out.println(SolarEdgeConnection.recordToString(rec));
        assertEquals(2953, rec.getValue().asInt());
 		assertEquals("2015-11-16 00:00:00", 
 					new TimeWrapper(rec.getTimestamp(), SolarEdgeConst.TIME_FORMAT).getTimeStr());
        assertEquals(Flag.VALID, rec.getFlag());
    }
}
