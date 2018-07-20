package org.openmuc.solaredge;

import org.openmuc.jsonpath.data.Config;

public class SolarEdgeConfig extends Config {

	private final static String ADDRESS_DEFAULT = "http://monitoringapi.solaredge.com/";
	private final static String API_KEY_DEFAULT = "L4QLVQ1LOKCQX2193VSEICXW61NP6B1O";
	private final static String AUTHORIZATION = "READ";

	private int maxThreads = 1;
	
	public SolarEdgeConfig() {
		super(ADDRESS_DEFAULT, API_KEY_DEFAULT, AUTHORIZATION, MAX_THREADS_DEFAULT);
	}
	
	public SolarEdgeConfig(String url) {
		super(url==null?ADDRESS_DEFAULT:url, API_KEY_DEFAULT, AUTHORIZATION, MAX_THREADS_DEFAULT);
	}
	
	public SolarEdgeConfig(String url, String authentication) {
		super(url==null?ADDRESS_DEFAULT:url, 
			  authentication==null?API_KEY_DEFAULT:authentication,
			  AUTHORIZATION, 
			  MAX_THREADS_DEFAULT);
	}
	
	@Override
	public int getMaxThreads() {
		if (maxThreads > 0 && maxThreads <= 3) {
			return maxThreads;
		}
		return MAX_THREADS_DEFAULT;
	}
}
