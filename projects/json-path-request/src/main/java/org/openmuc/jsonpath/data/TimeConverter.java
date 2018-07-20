package org.openmuc.jsonpath.data;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public final class TimeConverter {

	public static long timeStringToTime(String str, String format) throws ParseException {
		DateFormat formatter = new SimpleDateFormat(format);
		return formatter.parse(str).getTime();
	}

	public static String longToTimeString(long time, String format) {
	   SimpleDateFormat sdfDate = new SimpleDateFormat(format);
	   String strDate = sdfDate.format(time);
	   return strDate;
	}
}
