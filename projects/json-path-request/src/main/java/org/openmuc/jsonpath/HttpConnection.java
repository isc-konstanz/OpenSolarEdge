/* 
 * Copyright 2016-19 ISC Konstanz
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

import org.openmuc.jsonpath.data.Authentication;
import org.openmuc.jsonpath.request.HttpCallable;
import org.openmuc.jsonpath.request.HttpMethod;
import org.openmuc.jsonpath.request.HttpParameters;
import org.openmuc.jsonpath.request.HttpQuery;
import org.openmuc.jsonpath.request.HttpRequest;
import org.openmuc.jsonpath.request.HttpResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.JsonSyntaxException;

public class HttpConnection {

	private static final Logger logger = LoggerFactory.getLogger(HttpConnection.class);

	private static final int TIMEOUT = 30000;

	private final String address;
	private Authentication authentication;

	private int maxThreads;
	private ThreadPoolExecutor executor = null;

	protected HttpConnection(String address, String apiKey, int maxThreads) {
		this.address = address;
		this.authentication = new Authentication(apiKey);
		this.maxThreads = maxThreads;
	}

	public String getAddress() {
		return address;
	}

	public Authentication getAuthentication() {
		return authentication;
	}

	public HttpConnection setAuthentication(String apiKey) {
		this.authentication = new Authentication(apiKey);
		return this;
	}

	public int getMaxThreads() {
		return maxThreads;
	}

	public HttpConnection setMaxThreads(int max) {
		this.maxThreads = max;
		return this;
	}

	public HttpConnection open() {
		logger.info("Initializing communication \"{}\"", address);
		
		initialize();
		return this;
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

	public HttpConnection close() {
		logger.info("Shutting connection \"{}\" down", address);
		
		if (executor != null) {
			executor.shutdown();
		}
		return this;
	}

	public HttpResponse get(HttpQuery uri, HttpParameters parameters) throws HttpException {
		return request(HttpMethod.GET, uri, parameters);
	}

	public HttpResponse post(HttpQuery uri, HttpParameters parameters) throws HttpException {
		return request(HttpMethod.POST, uri, parameters);
	}

	public HttpResponse put(HttpQuery uri, HttpParameters parameters) throws HttpException {
		return request(HttpMethod.PUT, uri, parameters);
	}

	public HttpResponse delete(HttpQuery uri, HttpParameters parameters) throws HttpException {
		return request(HttpMethod.DELETE, uri, parameters);
	}

	protected HttpResponse request(HttpMethod method, HttpQuery uri, HttpParameters parameters) throws HttpException {
		return request(new HttpRequest(method, address, uri, parameters, authentication));
	}

	protected synchronized HttpResponse request(HttpRequest request) throws HttpException {
		if (logger.isTraceEnabled()) {
			logger.trace("Requesting \"{}\"", request.toString());
		}
		long start = System.currentTimeMillis();
		
		try {
			HttpCallable task = createCallable(request);
			final Future<HttpResponse> submit = executor.submit(task);
			try {
				HttpResponse response = submit.get(TIMEOUT, TimeUnit.MILLISECONDS);
				
				if (logger.isTraceEnabled()) {
					logger.trace("Received response after {}ms: {}", System.currentTimeMillis() - start, response);
				}
				return response;
			}
			catch (JsonSyntaxException e) {
				throw new HttpException("Received invalid JSON response: " + e, e);
			}
			catch (CancellationException | TimeoutException e) {
				submit.cancel(true);
				throw new HttpException("Aborted request \"" + request.toString() + "\": " + e, e);
			}
		} catch (InterruptedException | ExecutionException e) {
			initialize();
			throw new HttpException("Communication failed: " + e, e);
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
