package org.openmuc.jsonpath;

import org.openmuc.jsonpath.data.Authentication;
import org.openmuc.jsonpath.request.HttpCallable;
import org.openmuc.jsonpath.request.HttpRequest;

public class TestHttpHandler extends HttpHandler {
	String response;

	public TestHttpHandler(String address, Authentication authentication, int maxThreads) {
		super(address, authentication, maxThreads);
	}

	public void setResponse(String response) {
		this.response = response;
	}

	@Override
	protected HttpCallable createCallable(HttpRequest request) {
		TestHttpCallable task = new TestHttpCallable(request);
		task.setResponse(response);
		return task;
	}
}
