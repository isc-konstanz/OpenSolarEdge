package org.openmuc.jsonpath.data;

public class Config {

	public final static int MAX_THREADS_DEFAULT = 1;

	private String url;
	private String authentication;
	private String authorization;
	private int maxThreads = 1;
	
	public Config(String url, String authentication, String authorization, int maxThreads) {
		this.url = url;
		this.authentication = authentication;
		this.authorization = authorization;
		this.maxThreads = maxThreads;
	}
	
	public String getUrl() {
		return url;
	}

	public String getAuthentication() {
		return authentication;
	}
	
	public String getAuthorization() {
		return authorization;
	}
	
	public int getMaxThreads() {
		if (maxThreads > 0) {
			return maxThreads;
		}
		return MAX_THREADS_DEFAULT;
	}
}
