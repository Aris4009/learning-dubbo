package com.example.request.wrapper;

import javax.servlet.http.HttpServletRequest;

import org.apache.catalina.connector.RequestFacade;

/**
 * 提供可重用的request body
 */
public class RequestWrapperFacade {

	private final RequestWrapper requestWrapper;

	public RequestWrapperFacade(HttpServletRequest httpServletRequest) throws Exception {
		if (httpServletRequest instanceof RequestFacade) {
			this.requestWrapper = new RequestWrapper(httpServletRequest);
		} else {
			this.requestWrapper = (RequestWrapper) httpServletRequest;
			this.requestWrapper.getInputStream();
		}
	}

	public RequestWrapper getRequestWrapper() {
		return requestWrapper;
	}
}
