package org.openmuc.http.data;


public class Authentication {

	private final Authorization authorization;
	private final String key;

	public Authentication(Authorization authorization, String key) {
		this.authorization = authorization;
		this.key = key;
	}

	public Authorization getAuthorization() {
		return authorization;
	}
	
	public boolean isDefault() {
		if (authorization == Authorization.DEFAULT) {
			return true;
		}
		return false;
	}

	public String getKey() {
		return key;
	}

	@Override
	public boolean equals(Object object) {
		if (object instanceof Authentication) {
			Authentication authentication = (Authentication) object;
			if (authentication.getAuthorization() == authorization &&
					authentication.getKey().equals(key)) {
				return true;
			}
		}
		return false;
	}

	@Override
	public String toString() {
		switch (authorization) {
		case NONE:
			return "None";
		case DEFAULT:
			return "Default";
		default:
			return authorization.getValue() + "=" + key;
		}
	}
}
