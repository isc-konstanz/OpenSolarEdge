package org.openmuc.jsonpath;

import java.util.ArrayList;
import java.util.List;

import org.openmuc.jsonpath.data.ApiKeyConst;
import org.openmuc.jsonpath.data.Authentication;
import org.openmuc.jsonpath.data.Authorization;
import org.openmuc.jsonpath.data.Config;



public class HttpFactory {

	protected final List<HttpHandler> httpSingletonList = new ArrayList<HttpHandler>();

	protected final static HttpFactory factory = new HttpFactory();
	
	public static HttpFactory getHttpFactory() {
		return factory;
	}
	

	public HttpHandler newAuthenticatedConnection(Config config) {

		Authentication credentials = getCredentials(config.getAuthorization(), config.getAuthentication());
		
		return getConnection(config.getUrl(), credentials, config.getMaxThreads());
	}

	private HttpHandler getConnection(String address, Authentication credentials, Integer maxThreads) {

		if (address != null) {
			address = verifyAddress(address);
		}
		else {
			return null;
		}

		for (HttpHandler handler : httpSingletonList) {
			if (handler.getAddress().equals(address)) {
				if (!handler.getAuthentication().equals(credentials)) {
					handler.setAuthentication(credentials);
				}
				if (maxThreads != null && handler.getMaxThreads() != maxThreads) {
					handler.setMaxThreads(maxThreads);
				}
				return handler;
			}
		}
		if (maxThreads == null) {
			maxThreads = Config.MAX_THREADS_DEFAULT;
		}
		HttpHandler handler = new HttpHandler(address, credentials, maxThreads);
		httpSingletonList.add(handler);

		return handler;
	}

	public Authentication getCredentials(String type, String key) {
		
		Authentication authentication = null;
		if (type != null && key != null) {
			Authorization.setApiKey(ApiKeyConst.getApiKeyConst().getKey());
			Authorization authorization = Authorization.valueOf(type);
			authentication = new Authentication(authorization, key);
		}
		return authentication;
	}

	public static String verifyAddress(String address) {

		String url;
		if (!(address.startsWith("http://") || address.startsWith("https://"))) {
			url = "http://".concat(address);
		}
		else {
			url = address;
		}
		if (!url.endsWith("/")) {
			url = url.concat("/");
		}
		return url;
	}
}
