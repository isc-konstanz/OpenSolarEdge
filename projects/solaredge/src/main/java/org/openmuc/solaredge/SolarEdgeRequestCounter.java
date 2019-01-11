package org.openmuc.solaredge;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

import org.openmuc.solaredge.data.TimeWrapper;

public class SolarEdgeRequestCounter {

	private static final Logger logger = LoggerFactory.getLogger(SolarEdgeRequestCounter.class);

	private static int COUNTER = 0;
	private static int DAY = 0;
	
	private static Properties PROPERTIES = new Properties();
	
	public static void augmentCounter(long time) {
		if (DAY == 0) {
			readProperties();
		}
		if (dayChange(time)) {
			COUNTER = 0; 
		}
		COUNTER++;
		writeProperties();
		logger.debug("SolarEdge requested for " + COUNTER);
	}

	private static void readProperties() {
		try (FileInputStream in = new FileInputStream("counter.properties")) {
			PROPERTIES.load(in);
			DAY = new Integer(PROPERTIES.getProperty("RequestDay"));
			COUNTER = new Integer(PROPERTIES.getProperty("Counter"));
			logger.debug("DAY: " + DAY + " COUNTER: " + COUNTER);
		} 
		catch (Exception e) {
		    logger.error("Could not read counter", e);
		    DAY = 0;
			logger.debug("DAY: " + DAY + " COUNTER: " + COUNTER);
		}
	}

	private static void writeProperties() {
		try (FileOutputStream out = new FileOutputStream("counter.properties")) {
			PROPERTIES.setProperty("Counter", new Integer(COUNTER).toString());
			logger.debug("Write DAY: " + PROPERTIES.getProperty("RequestDay") + " COUNTER: " + PROPERTIES.getProperty("Counter"));
			PROPERTIES.store(out, null);
		} 
		catch (IOException e) {
			logger.debug("Could not write counter", e);
			logger.debug("Write DAY: " + PROPERTIES.getProperty("RequestDay") + " COUNTER: " + PROPERTIES.getProperty("Counter"));
		}		
	}

	private static boolean dayChange(long time) {
		String date = new TimeWrapper(time, SolarEdgeConst.DATE_FORMAT).getTimeStr();
		int myday = new Integer(date.substring(date.length()-2));
		if (DAY != myday) {
			DAY = myday;
			PROPERTIES.setProperty("RequestDay", new Integer(DAY).toString());
			return true;
		}
		
		return false;
	}
	
	public static String getCounter() {
		return "SolarEdge requested for " + COUNTER;
	}
}
