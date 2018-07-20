package org.openmuc.jsonpath.data;

public class TimeValue {

	Long time;
	Object value;

	public TimeValue(Object value, Long time) {
		this.time = time;
		this.value = value;
	}
	
	public Long getTime() {
		return time;
	}
	public void setTime(Long time) {
		this.time = time;
	}
	public Object getValue() {
		return value;
	}
	public void setValue(Object value) {
		this.value = value;
	}
}
