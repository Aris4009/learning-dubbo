package com.example.exception.handler;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;

/**
 * 统一错误处理controller
 */
@ControllerAdvice
public class ExControllerAdvice {

	@ExceptionHandler(Exception.class)
	public ResponseEntity<Map<String, Object>> ex(@RequestBody Exception ex, WebRequest request) {
		ServletWebRequest servletWebRequest = (ServletWebRequest) request;
		HttpServletRequest httpServletRequest = servletWebRequest.getRequest();
		httpServletRequest.setAttribute("ex", ex);
		Map<String, Object> map = ExResponseEntity.map(ex, request);
		return new ResponseEntity<>(map, HttpStatus.INTERNAL_SERVER_ERROR);
	}
}
