package com.example.ex;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * 统一错误处理controller
 */
@ControllerAdvice
public class ExControllerAdvice {

	private final Logger log = LoggerFactory.getLogger(this.getClass());

	@ExceptionHandler(Exception.class)
	public ResponseEntity<Map<String, Object>> ex(@RequestBody Exception ex, HttpServletRequest request) {
		log.error("url:{},error:{}", request.getRequestURI(), ex);
		Exception exception = new Exception("internal error");
		Map<String, Object> map = ExResponseEntity.map(exception, request);
		return new ResponseEntity<>(map, HttpStatus.INTERNAL_SERVER_ERROR);
	}
}
