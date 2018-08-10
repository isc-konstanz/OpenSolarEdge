package org.openmuc.solaredge.data;

public class SolarEdgeApiKeyConst {

	private static SolarEdgeApiKeyConst API_KEY_CONST =
			new SolarEdgeApiKeyConst();
	
	public static SolarEdgeApiKeyConst getApiKeyConst() {
		return API_KEY_CONST;
	}

	public String getKey() {
		return "api_key";
	}
}
