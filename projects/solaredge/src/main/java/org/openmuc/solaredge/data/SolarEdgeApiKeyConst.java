package org.openmuc.solaredge.data;

import org.openmuc.jsonpath.data.ApiKeyConst;

public class SolarEdgeApiKeyConst extends ApiKeyConst {

	private static ApiKeyConst API_KEY_CONST =
			new SolarEdgeApiKeyConst();
	
	public static ApiKeyConst getApiKeyConst() {
		return API_KEY_CONST;
	}

	private String key = "api_key";
	
	@Override
	public String getKey() {
		return key;
	}
}
