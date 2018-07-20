package org.openmuc.jsonpath.request;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.Charset;

import org.openmuc.jsonpath.data.Authentication;



public class HttpRequest {

	private final String url;
	private final Authentication authentication;
	private final HttpRequestAction action;
	private final HttpRequestParameters parameters;
	private final HttpRequestMethod method;


	public HttpRequest(String url, Authentication authentication, 
			HttpRequestAction action, HttpRequestParameters parameters, 
			HttpRequestMethod method) {
		
		this.url = url;
		this.authentication = authentication;
		this.action = action;
		this.parameters = parameters;
		this.method = method;
	}

	public String getUrl() {
		return url;
	}

	public String getAuthentication(Charset charset) throws UnsupportedEncodingException {
		if (authentication != null) {
			return URLEncoder.encode(authentication.getAuthorization().getValue(), charset.name()) + 
					'=' + URLEncoder.encode(authentication.getKey(), charset.name());
		}
		return null;
	}

	public HttpRequestAction getAction() {
		return action;
	}

	public String parseAction(Charset charset) throws UnsupportedEncodingException {
		return action.parseAction(charset);
	}

	public HttpRequestParameters getParameters() {
		return parameters;
	}

	public String parseParameters(Charset charset) throws UnsupportedEncodingException {
		String content = parameters.parseParameters(charset);
		if (authentication != null) {
			switch (method) {
			case POST:
			case PUT:
				if (parameters != null && parameters.size() > 0) {
					content += '&';
				}
				content += getAuthentication(charset);
				break;
			default:
				break;
			}
		}
		return content;
	}

	public HttpRequestMethod getMethod() {
		return method;
	}

	public String getRequest(Charset charset) throws UnsupportedEncodingException {
		String request = url;
		if (action != null) {
			request += action.parseAction(charset);
		}
		if (authentication != null) {
			switch (method) {
			case POST:
			case PUT:
				break;
			default:
				if (action != null && action.size() > 0) {
					request += '&';
				}
				else {
					request += '?';
				}
				request += getAuthentication(charset);
				break;
			}
		}
		return request;
	}

	@Override
	public String toString() {
		String request = url;
		if (action != null) {
			request += action.toString();
		}
		if (parameters != null) {
			if (parameters.size() > 0 && action.size() == 0) {
				request += '?';
			}
			if (action != null && action.size() > 0) {
				request += '&';
			}
			request += parameters.toString();
		}
		return request;
	}
}
