package org.openmuc.http;

import java.io.IOException;

import org.openmuc.http.request.HttpCallable;
import org.openmuc.http.request.HttpRequest;
import org.openmuc.http.request.json.JsonResponse;

public class TestHttpCallable extends HttpCallable {

	private String response;

	public TestHttpCallable(HttpRequest request) {
		super(request);
	}
	
	protected JsonResponse get(HttpRequest request) throws IOException {
		if (response == null) {
			return null;
		}
		return new TestJsonResponse(response);
	}
	
	protected JsonResponse post(HttpRequest request) throws IOException {
		if (response == null) {
			return null;
		}
		return new TestJsonResponse(response);
	}

	public void setResponse(String response) {
		this.response = response;
		
	}

}
