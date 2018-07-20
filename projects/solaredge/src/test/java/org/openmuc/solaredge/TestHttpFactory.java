package org.openmuc.solaredge;

import org.openmuc.jsonpath.HttpHandler;
import org.openmuc.jsonpath.TestHttpHandler;
import org.openmuc.jsonpath.data.Authentication;
import org.openmuc.jsonpath.data.Config;
import org.openmuc.solaredge.SolarEdgeHttpFactory;

public class TestHttpFactory extends SolarEdgeHttpFactory {

	public static TestHttpHandler newAuthenticatedConnection(Config config) {

		Authentication credentials = factory.getCredentials(config.getAuthorization(), config.getAuthentication());
		
		return getConnection(config.getUrl(), credentials, config.getMaxThreads());
	}

	private static TestHttpHandler getConnection(String address, Authentication credentials, Integer maxThreads) {

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
				return (TestHttpHandler) handler;
			}
		}
		if (maxThreads == null) {
			maxThreads = Config.MAX_THREADS_DEFAULT;
		}
		TestHttpHandler handler = new TestHttpHandler(address, credentials, maxThreads);
		httpSingletonList.add(handler);

		return handler;
	}

}
