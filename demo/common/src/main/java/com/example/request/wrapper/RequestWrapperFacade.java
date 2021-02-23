package com.example.request.wrapper;

import javax.servlet.http.HttpServletRequest;

/**
 * 提供可重用的request body
 */
public class RequestWrapperFacade {

	private final RequestWrapper requestWrapper;

	public RequestWrapperFacade(HttpServletRequest httpServletRequest) throws Exception {
		this.requestWrapper = (RequestWrapper) httpServletRequest;
		this.requestWrapper.getInputStream();
	}

	public RequestWrapper getRequestWrapper() {
		return requestWrapper;
	}
}
