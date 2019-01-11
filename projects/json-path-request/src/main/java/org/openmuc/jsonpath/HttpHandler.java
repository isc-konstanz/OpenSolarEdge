/* 
 * Copyright 2016-18 ISC Konstanz
 * 
 * This file is part of OpenSolarEdge.
 * For more information visit https://github.com/isc-konstanz/OpenSolarEdge
 * 
 * OpenSolarEdge is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * OpenSolarEdge is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with OpenSolarEdge.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.openmuc.jsonpath;

import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicInteger;

import org.openmuc.jsonpath.com.HttpGeneralException;
import org.openmuc.jsonpath.data.Authentication;
import org.openmuc.jsonpath.request.HttpCallable;
import org.openmuc.jsonpath.request.HttpRequest;
import org.openmuc.jsonpath.request.HttpRequestAction;
import org.openmuc.jsonpath.request.HttpRequestCallbacks;
import org.openmuc.jsonpath.request.HttpRequestMethod;
import org.openmuc.jsonpath.request.HttpRequestParameters;
import org.openmuc.jsonpath.request.json.JsonResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.JsonSyntaxException;

public class HttpHandler implements HttpRequestCallbacks {

	private static final Logger logger = LoggerFactory.getLogger(HttpHandler.class);

	private static final int TIMEOUT = 30000;

	private final String address;
	private Authentication authentication;

	private int maxThreads;
	private ThreadPoolExecutor executor = null;


	public HttpHandler(String address, Authentication authentication, int maxThreads) {
		
		this.address = address;
		this.authentication = authentication;
		this.maxThreads = maxThreads;
	}

	public String getAddress() {
		return address;
	}

	public Authentication getAuthentication() {
		return authentication;
	}

	public void setAuthentication(Authentication authentication) {
		this.authentication = authentication;
	}

	public int getMaxThreads() {
		return maxThreads;
	}

	public void setMaxThreads(int max) {
		this.maxThreads = max;
	}

	public void start() {
		logger.info("Initializing communication \"{}\"", address);
		initialize();
	}

	protected void initialize() {
		// The HttpURLConnection implementation is in older JREs somewhat buggy with keeping connections alive. 
		// To avoid this, the http.keepAlive system property can be set to false. 
		System.setProperty("http.keepAlive", "false");
		
		if (executor != null) {
			executor.shutdown();
		}
		NamedThreadFactory namedThreadFactory = new NamedThreadFactory("HTTP request pool - thread-");
		executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(maxThreads, namedThreadFactory);
	}

	public void stop() {
		logger.info("Shutting emoncms connection \"{}\" down", address);
		
		if (executor != null) {
			executor.shutdown();
		}
	}

	@Override
	public HttpRequest getRequest(String parent, HttpRequestAction action, HttpRequestParameters parameters,
			HttpRequestMethod method) {
		return getRequest(parent, authentication, action, parameters, method);
	}
	
	@Override
	public HttpRequest getRequest(String parent, Authentication authentication, HttpRequestAction action,
			HttpRequestParameters parameters, HttpRequestMethod method) {
		String url = address + parent;
		if (!url.endsWith("/"))
			url += "/";
		
		return new HttpRequest(url, authentication, action, parameters, method);
	}
	
	@Override
	public JsonResponse onRequest(HttpRequest request) throws HttpGeneralException {
		try {
			JsonResponse response = submitRequest(request);
			if (response != null) {
				// TODO is that needed, ask Adrian ?
//				if (response.isSuccess()) {
					return response;
//				}
//				throw new HttpException("Request responsed \"false\"");
			}
			throw new HttpGeneralException("Request failed");
		}
		catch (Exception e) {
			logger.debug(e.getMessage());
			throw e;
		}
	}
	
	protected synchronized JsonResponse submitRequest(HttpRequest request) throws HttpGeneralException {
		if (logger.isTraceEnabled()) {
			logger.trace("Requesting \"{}\"", request.toString());
		}
		long start = System.currentTimeMillis();
		
		try {
			HttpCallable task = createCallable(request);
			final Future<JsonResponse> submit = executor.submit(task);
			try {
				JsonResponse response = submit.get(TIMEOUT, TimeUnit.MILLISECONDS);
				
				if (logger.isTraceEnabled()) {
					String rsp = "Returned null";
					if (response != null) {
						rsp = response.getResponse();
					}
					logger.trace("Received response after {}ms: {}", System.currentTimeMillis() - start, rsp);
				}
				return response;
			}
			catch (JsonSyntaxException e) {
				throw new HttpGeneralException("Received invalid JSON response: " + e, e);
			}
			catch (CancellationException | TimeoutException e) {
				submit.cancel(true);
				throw new HttpGeneralException("Aborted request \"" + request.toString() + "\": " + e, e);
			}
		} catch (InterruptedException | ExecutionException e) {
			initialize();
			throw new HttpGeneralException("Communication failed: " + e, e);
		}
	}
	
	protected HttpCallable createCallable(HttpRequest request) {
		return new HttpCallable(request);
	}

	private class NamedThreadFactory implements ThreadFactory {

		private final String name;
		private final AtomicInteger counter = new AtomicInteger(0);

		public NamedThreadFactory(String name) {
			this.name = name;
		}

		@Override
		public Thread newThread(Runnable r) {
			String threadName = name + counter.incrementAndGet();
			return new Thread(r, threadName);
		}
	}
}
