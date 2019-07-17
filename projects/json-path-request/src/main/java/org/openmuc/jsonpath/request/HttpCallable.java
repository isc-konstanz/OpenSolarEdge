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
package org.openmuc.jsonpath.request;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.Callable;

import org.openmuc.jsonpath.HttpException;


public class HttpCallable implements Callable<HttpResponse> {

	private final static Charset CHARSET = StandardCharsets.UTF_8;

	private final HttpRequest request;
	private HttpURLConnection connection = null;
	private InputStream stream = null;

	public HttpCallable(HttpRequest request) {
		this.request = request;
	}

	public HttpRequest getRequest() {
		return request;
	}

	@Override
	public HttpResponse call() throws Exception {
		HttpMethod method = request.getMethod();
		switch (method) {
		case GET:
			return get(request);
		case POST:
			return post(request);
		default:
		  	throw new HttpException("No HTTP request method " + method.toString() + "implemented");
		}
	}

	protected HttpResponse get(HttpRequest request) throws IOException {
		try {
			URL url = new URL(request.parse(CHARSET));
			connection = (HttpURLConnection) url.openConnection();
			
			connection.setRequestMethod(HttpMethod.GET.name());
			connection.setRequestProperty("Charset", CHARSET.name());
			connection.setRequestProperty("Accept-Charset", CHARSET.name());
			connection.setRequestProperty("Connection", "Close");
			connection.setRequestProperty("Content-Type", "application/json;charset="+CHARSET.name());
			connection.setRequestProperty("Content-length", "0");
			
			connection.setInstanceFollowRedirects(false);
			connection.setAllowUserInteraction(false);
			connection.setUseCaches(false);
			connection.setDoOutput(true);
			connection.setDoInput(true);
			connection.connect();
			
			if (verifyResponse(connection.getResponseCode())) {
				stream = connection.getInputStream();
				
				return read();
			}
			throw new HttpException("HTTP status code " + connection.getResponseCode() + ": " + connection.getResponseMessage());
		
		} finally {
			try {
				if (stream != null) {
					stream.close();
					stream = null;
				}
				if (connection != null) {
				  	connection.disconnect();
				  	connection = null;
			   	}
		  	} catch (Exception e) {
				throw new HttpException("Unknown exception while closing connection: " + e.getMessage());
		  	}
		}
	}

	protected HttpResponse post(HttpRequest request) throws IOException {
		try {
			URL url = new URL(request.parse(CHARSET));
			
			connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod(HttpMethod.POST.name());
			connection.setRequestProperty("Connection", "Close");
			connection.setRequestProperty("Charset", CHARSET.name());
			connection.setRequestProperty("Accept-Charset", CHARSET.name());
			connection.setRequestProperty("Content-Type", "application/json;charset="+CHARSET.name());
			
			byte[] content = null;
			if (request.getParameters() != null) {
				content = request.parseParameters(CHARSET).getBytes(CHARSET);
				connection.setRequestProperty("Content-Length", Integer.toString(content.length));
			}
			else {
				connection.setRequestProperty("Content-length", "0");
			}
			
			connection.setInstanceFollowRedirects(false);
			connection.setAllowUserInteraction(false);
			connection.setUseCaches(false);
			connection.setDoOutput(true);
			connection.setDoInput(true);
			
			if (content != null) {
				DataOutputStream wr = new DataOutputStream(connection.getOutputStream());
				wr.write(content);
				wr.flush();
				wr.close();
			}
			connection.connect();
			
			if (verifyResponse(connection.getResponseCode())) {
				stream = connection.getInputStream();
				
				return read();
			}
			throw new HttpException("HTTP status code " + connection.getResponseCode() + ": " + connection.getResponseMessage());
		
		} finally {
			try {
				if (stream != null) {
					stream.close();
					stream = null;
				}
				if (connection != null) {
				  	connection.disconnect();
				  	connection = null;
			   	}
		  	} catch (Exception e) {
				throw new HttpException("Unknown exception while closing connection: " + e.getMessage());
		  	}
		}
	}

	private HttpResponse read() throws IOException {
		BufferedReader reader = new BufferedReader(new InputStreamReader(stream, CHARSET.name()));
		
		StringBuilder sb = new StringBuilder();
		String line;
		while (!Thread.currentThread().isInterrupted() && 
				(line = reader.readLine()) != null) {
			
			sb.append(line);
		}
		if (sb.length() != 0 && !sb.toString().isEmpty()) {
			return new HttpResponse(sb.toString());
		}
		return null;
	}

	private boolean verifyResponse(int httpStatus) throws HttpException {
		switch (httpStatus) {
		case HttpURLConnection.HTTP_OK:
			return true;
		default:
			return false;
		}
	}

}
