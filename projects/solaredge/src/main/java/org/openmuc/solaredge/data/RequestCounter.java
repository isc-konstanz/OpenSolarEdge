package org.openmuc.solaredge.data;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.TimeZone;

import org.openmuc.solaredge.config.SolarEdgeConst;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RequestCounter {

	private static final Logger logger = LoggerFactory.getLogger(RequestCounter.class);

	private static final String FILE_NAME = "counter.properties";
	private static final String FOLDER = "org.openmuc.solaredge.folder";
	private static final String FOLDER_DEFAULT = "";

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
		String fileName = getFileName();
		try (FileInputStream in = new FileInputStream(fileName)) {
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

	private static String getFileName() {
		String fileName = System.getProperty(FOLDER, FOLDER_DEFAULT);
		fileName = fileName + FILE_NAME;
		return fileName;
	}

	private static void writeProperties() {
		String fileName = getFileName();
		try (FileOutputStream out = new FileOutputStream(fileName)) {
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
		String date = new TimeWrapper(time, SolarEdgeConst.DATE_FORMAT, TimeZone.getTimeZone("GMT")).getTimeStr();
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
