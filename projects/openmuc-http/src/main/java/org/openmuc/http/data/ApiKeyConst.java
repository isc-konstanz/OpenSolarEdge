package org.openmuc.http.data;

public class ApiKeyConst {

	private static ApiKeyConst API_KEY_CONST =
			new ApiKeyConst();
	
	public static ApiKeyConst getApiKeyConst() {
		return API_KEY_CONST;
	}

	private String key = "apikey";
	
	protected ApiKeyConst() {
	}

	public String getKey() {
		return key;
	}
}
