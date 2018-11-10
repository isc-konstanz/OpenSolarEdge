/* 
 * Copyright 2016-18 ISC Konstanz
 * 
 * This file is part of OpenSolarEdge.
 * For more information visit https://github.com/isc-konstanz/OpenSolarEdge
 * 
 * OpenSolarEdge is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * OpenSolarEdge is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with OpenSolarEdge.  If not, see <http://www.gnu.org/licenses/>.
 */
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
