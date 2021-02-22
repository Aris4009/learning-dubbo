package com.example.ex;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;

public class ExResponseEntity extends ResponseEntity<Map<String, Object>> {

	public ExResponseEntity(Map<String, Object> map) {
		super(map, HttpStatus.INTERNAL_SERVER_ERROR);
	}

	public static Map<String, Object> map(Exception ex, HttpServletRequest request) {
		Map<String, Object> map = new HashMap<>();
		map.put("timestamp", LocalDateTime.now().toString());
		map.put("status", 500);
		if (ex instanceof HttpMessageNotReadableException) {
			map.put("message", "unsupported params type");
		} else {
			map.put("message", ex.getMessage());
		}
		map.put("path", request.getRequestURI());
		return map;
	}
}
