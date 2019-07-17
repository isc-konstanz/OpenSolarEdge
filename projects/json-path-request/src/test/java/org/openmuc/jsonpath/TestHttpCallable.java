package org.openmuc.jsonpath;

import java.io.IOException;

import org.openmuc.jsonpath.request.HttpCallable;
import org.openmuc.jsonpath.request.HttpRequest;
import org.openmuc.jsonpath.request.HttpResponse;

public class TestHttpCallable extends HttpCallable {

	private String response;

	public TestHttpCallable(HttpRequest request) {
		super(request);
	}

	@Override
	protected HttpResponse get(HttpRequest request) throws IOException {
		if (response == null) {
			return null;
		}
		return new HttpResponse(response);
	}

	@Override
	protected HttpResponse post(HttpRequest request) throws IOException {
		if (response == null) {
			return null;
		}
		return new HttpResponse(response);
	}

	public void setResponse(String response) {
		this.response = response;
		
	}

}
