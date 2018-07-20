package org.openmuc.solaredge.data;

import java.text.ParseException;

import org.openmuc.jsonpath.data.TimeConverter;

public class TimeWrapper {
	
	private String timeStr;
	private Long time;
	private String format;
	
	public TimeWrapper(String timeStr, String format) {
		this.timeStr = timeStr;
		this.format = format;
	}
	
	public TimeWrapper(Long time, String format) {
		this.time = time;
		this.format = format;
	}
	
	public TimeWrapper(String timeStr, Long time, String format) {
		this.timeStr = timeStr;
		this.time = time;
		this.format = format;		
	}

	public String getTimeStr() {
		if (timeStr == null && time != null) {
			timeStr = TimeConverter.longToTimeString(time, format);
		}
		return timeStr;
	}

	public void setTimeStr(String timeStr) {
		this.timeStr = timeStr;
		this.time = null;
	}

	public Long getTime() throws ParseException {
		if (time == null && timeStr != null) {
			time = TimeConverter.timeStringToTime(timeStr, format);
		}
		return time;
	}

	public void setTime(Long time) {
		this.time = time;
		this.timeStr = null;
	}
	
	public String getFormat() {
		return format;
	}

	public void setFormat(String format) {
		this.format = format;
		if (time != null) {
			timeStr = TimeConverter.longToTimeString(time, format);
		}
	}

	public TimeWrapper clone() {
		TimeWrapper retVal = new TimeWrapper(timeStr, time, format);
		return retVal;
	}
	
}
