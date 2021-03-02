package com.example.exception.handler;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;

import com.example.config.RequestLogConfig;

/**
 * 统一错误处理controller
 */
@ControllerAdvice
public class ExControllerAdvice {

	private final Logger log = LoggerFactory.getLogger(this.getClass());

	private final RequestLogConfig.RequestLog requestLog;

	public ExControllerAdvice(RequestLogConfig.RequestLog requestLog) {
		this.requestLog = requestLog;
	}

	@ExceptionHandler(Exception.class)
	@SuppressWarnings("unchecked")
	public ResponseEntity<Map<String, Object>> ex(@RequestBody Exception ex, WebRequest request) {
		ServletWebRequest servletWebRequest = (ServletWebRequest) request;
		HttpServletRequest httpServletRequest = servletWebRequest.getRequest();
		httpServletRequest.setAttribute("ex", ex);
		Map<String, Object> map = ExResponseEntity.map(ex, request);
		Map<String, Object> preHandleEx = (Map<String, Object>) httpServletRequest.getAttribute("preHandle");
		if (preHandleEx != null) {
			String requestId = (String) preHandleEx.get("request-id");
			String url = (String) preHandleEx.get("url");
			HttpMethod method = (HttpMethod) preHandleEx.get("method");
			Exception exception = (Exception) preHandleEx.get("exception");
			if (requestLog.isError()) {
				log.error("request-id:{},url:{},method:{}", requestId, url, method, exception);
			}
		}
		return new ResponseEntity<>(map, HttpStatus.INTERNAL_SERVER_ERROR);
	}
}
