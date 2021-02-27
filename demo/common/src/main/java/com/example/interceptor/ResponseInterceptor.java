package com.example.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.example.request.wrapper.RequestWrapper;

public class ResponseInterceptor implements HandlerInterceptor {

	private final Logger log = LoggerFactory.getLogger(this.getClass());

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		if (request instanceof RequestWrapper) {
			log.info("{}", ((RequestWrapper) request).getRequestBody());
		}
	}
}
