package org.openmuc.jsonpath;

import org.openmuc.jsonpath.request.HttpCallable;
import org.openmuc.jsonpath.request.HttpRequest;

public class TestHttpHandler extends HttpConnection {
	String response;

	public TestHttpHandler(String address, String apiKey) {
		super(address, apiKey, HttpConfig.MAX_THREADS_DEFAULT);
	}

	public TestHttpHandler setResponse(String response) {
		this.response = response;
		return this;
	}

	@Override
	protected HttpCallable createCallable(HttpRequest request) {
		TestHttpCallable task = new TestHttpCallable(request);
		task.setResponse(response);
		return task;
	}
}
