package org.openmuc.jsonpath;

import org.openmuc.jsonpath.HttpHandler;
import org.openmuc.jsonpath.com.HttpGeneralException;
import org.openmuc.jsonpath.data.Authentication;
import org.openmuc.jsonpath.request.HttpCallable;
import org.openmuc.jsonpath.request.HttpRequest;
import org.openmuc.jsonpath.request.json.JsonResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestHttpHandler extends HttpHandler {

	private static final Logger logger = LoggerFactory.getLogger(TestHttpHandler.class);

	String response;
	
	public TestHttpHandler(String address, Authentication authentication, int maxThreads) {
		super(address, authentication, maxThreads);
	}
	
	public void setResponse(String response) {
		this.response = response;
	}

	protected synchronized JsonResponse submitRequest(HttpRequest request) throws HttpGeneralException {
		if (logger.isTraceEnabled()) {
			logger.trace("Requesting \"{}\"", request.toString());
		}
		long start = System.currentTimeMillis();
		
		HttpCallable task = createCallable(request);
		JsonResponse response;
		try {
			response = task.call();
			if (logger.isTraceEnabled()) {
				String rsp = "Returned null";
				if (response != null) {
					rsp = response.getResponse();
				}
				logger.trace("Received response after {}ms: {}", System.currentTimeMillis() - start, rsp);
			}
			return response;		
		} catch (Exception e) {
			throw new HttpGeneralException("Aborted request \"" + request.toString() + "\": " + e);
		}
	}

	
	protected HttpCallable createCallable(HttpRequest request) {
		TestHttpCallable task = new TestHttpCallable(request);
		task.setResponse(response);
		return task;
	}
}
