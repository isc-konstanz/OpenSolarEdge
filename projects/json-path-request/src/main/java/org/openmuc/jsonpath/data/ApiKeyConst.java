package org.openmuc.jsonpath.data;

public class ApiKeyConst {

	private static ApiKeyConst API_KEY_CONST =
			new ApiKeyConst();
	
	public static ApiKeyConst getApiKeyConst() {
		return API_KEY_CONST;
	}

	public String getKey() {
		return "apikey";
	}
}
