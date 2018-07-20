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
import org.openmuc.solaredge.TestSolarEdgeResponseHandler;
import org.openmuc.solaredge.data.TimeWrapper;
import org.openmuc.jsonpath.data.TimeValue;
import org.openmuc.jsonpath.request.HttpRequest;

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
	static String REQUEST = "http://monitoringapi.solaredge.com/site/78467/energy/";
	static String PARAMETERS = "startDate=&endDate=&timeUnit=&api_key=";
	
    
 	private static TestSolarEdgeResponseHandler HANDLER = 
 			new TestSolarEdgeResponseHandler(jsonString);
 	
	@Test
    public void test_EnergyJsonGetEnergy() {
        String testMethodName = "test_EnergyJsonGetEnergy";
        System.out.println(testMethodName);

        TimeValue timeValuePair = null;
		try {
			timeValuePair = HANDLER.getTimeValuePair("$.energy", null, SolarEdgeConst.QUARTER_OF_AN_HOUR, null);
		} catch (ParseException e) {
			e.printStackTrace();
			assertTrue(false);
		}
		Record rec = SolarEdgeConnection.timeValuePairToRecord(timeValuePair);
		System.out.println(SolarEdgeConnection.recordToString(rec));
        assertEquals("{timeUnit=DAY, unit=Wh, values=[{\"date\":\"2013-06-01 00:00:00\",\"value\":null},{\"date\":\"2013-06-02 00:00:00\",\"value\":null},{\"date\":\"2013-06-03 00:00:00\",\"value\":null},{\"date\":\"2013-06-04 00:00:00\",\"value\":67313.24}]}", rec.getValue().asString());
        try {
			assertEquals(HANDLER.getTimeWrapper().getTime(), rec.getTimestamp());
		} catch (ParseException e) {
			e.printStackTrace();
			assertTrue(false);
		}
         assertEquals(Flag.VALID, rec.getFlag());
	}
	
	@Test
    public void test_EnergyJsonGetValueLast() {
        String testMethodName = "test_EnergyJsonGetValueLast";
        System.out.println(testMethodName);

        TimeValue timeValuePair = null;
  		try {
  			timeValuePair = HANDLER.getTimeValuePair("$.energy.values[-1].value", 
 					"$.energy.values[-1].date", 
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
        assertEquals("67313.24", rec.getValue().asString());
 		assertEquals("2013-06-04 00:00:00", 
 					new TimeWrapper(rec.getTimestamp(), SolarEdgeConst.TIME_FORMAT).getTimeStr());
        assertEquals(Flag.VALID, rec.getFlag());
    }
    
	
}
