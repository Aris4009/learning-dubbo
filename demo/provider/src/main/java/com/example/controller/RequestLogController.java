package com.example.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.db1.model.MyPageInfo;
import com.example.interceptor.RequestLog;
import com.example.response.entity.Response;
import com.example.test_log.service.RequestLogService;

@RestController
@RequestMapping("/api/request/log")
public class RequestLogController {

	private final RequestLogService requestLogService;

	private final HttpServletRequest httpServletRequest;

	public RequestLogController(RequestLogService requestLogService, HttpServletRequest httpServletRequest) {
		this.requestLogService = requestLogService;
		this.httpServletRequest = httpServletRequest;
	}

	@PostMapping("/page")
	public Response<MyPageInfo<RequestLog>> page(@RequestBody MyPageInfo<RequestLog> page) {
		return Response.ok(requestLogService.page(page), httpServletRequest);
	}
}
