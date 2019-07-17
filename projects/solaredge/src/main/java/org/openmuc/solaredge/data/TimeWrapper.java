/* 
 * Copyright 2016-19 ISC Konstanz
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
import java.util.TimeZone;

import org.openmuc.jsonpath.data.TimeConverter;

public class TimeWrapper {

	private TimeZone zone;
	private String format;
	private String timeStr;
	private Long time;

	public TimeWrapper(String timeStr, String format, TimeZone zone) {
		this.zone = zone;
		this.format = format;
		this.timeStr = timeStr;
	}

	public TimeWrapper(Long time, String format, TimeZone zone) {
		this.zone = zone;
		this.format = format;
		this.time = time;
	}

	private TimeWrapper(String timeStr, Long time, String format, TimeZone zone) {
		this.zone = zone;
		this.format = format;
		this.timeStr = timeStr;
		this.time = time;
	}

	public String getTimeStr() {
		if (timeStr == null && time != null) {
			timeStr = TimeConverter.encode(time, format, zone);
		}
		return timeStr;
	}

	public void setTimeStr(String timeStr) {
		this.timeStr = timeStr;
		this.time = null;
	}

	public Long getTime() throws ParseException {
		if (time == null && timeStr != null) {
			time = TimeConverter.decode(timeStr, format, zone);
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
			timeStr = TimeConverter.encode(time, format, zone);
		}
	}
	
	public TimeZone getTimeZone() {
		return zone;
	}

	public void setTimeZone(TimeZone zone) {
		this.zone = zone;
		if (time != null) {
			timeStr = TimeConverter.encode(time, format, zone);
		}
	}

	public TimeWrapper clone() {
		return new TimeWrapper(timeStr, time, format, zone);
	}

}
