package org.openmuc.solaredge;

import org.openmuc.jsonpath.HttpFactory;
import org.openmuc.jsonpath.data.Authentication;
import org.openmuc.jsonpath.data.Authorization;
import org.openmuc.solaredge.data.SolarEdgeApiKeyConst;

public class SolarEdgeHttpFactory extends HttpFactory {

	protected final static HttpFactory factory = new SolarEdgeHttpFactory();
	
		@Override
	public Authentication getCredentials(String type, String key) {
		
		Authentication authentication = null;
		if (type != null && key != null) {
			Authorization.setApiKey(SolarEdgeApiKeyConst.getApiKeyConst());
			Authorization authorization = Authorization.valueOf(type);
			authentication = new Authentication(authorization, key);
		}
		return authentication;
	}
}
