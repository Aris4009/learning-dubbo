package com.example.controller;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.db1.model.MyPageInfo;
import com.example.db1.model.Test;
import com.example.db1.model.User;
import com.example.db1.service.IUserService;
import com.example.exception.BusinessException;
import com.example.json.JSON;
import com.example.response.entity.Response;

@RestController
@RequestMapping("/api/user")
public class UserController {

	private final IUserService userService;

	private HttpServletRequest request;

	private final Logger log = LoggerFactory.getLogger(this.getClass());

	public UserController(IUserService userService, HttpServletRequest request) {
		this.userService = userService;
		this.request = request;
	}

	@PostMapping("/insert")
	public Response<int[]> insert(@RequestBody Map<String, Object> param) throws BusinessException {
		if (param == null) {
			throw BusinessException.paramsError();
		}
		User user = null;
		Test test = null;
		try {
			user = parse(param.get("user"), User.class);
			test = parse(param.get("test"), Test.class);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			throw BusinessException.paramsError();
		}
		return Response.ok(this.userService.insert(user, test), request);
	}

	@PostMapping("/modify")
	public Response<int[]> modify(@RequestBody Map<String, Object> param) throws BusinessException {
		if (param == null) {
			throw BusinessException.paramsError();
		}
		User user = null;
		Test test = null;
		try {
			user = parse(param.get("user"), User.class);
			test = parse(param.get("test"), Test.class);
		} catch (Exception e) {
			throw BusinessException.paramsError();
		}
		return Response.ok(this.userService.modify(user, test), request);
	}

	@PostMapping("/delete")
	public Response<int[]> delete(@RequestBody Map<String, Object> param) throws BusinessException {
		if (param == null) {
			throw BusinessException.paramsError();
		}
		User user = null;
		Test test = null;
		try {
			user = parse(param.get("user"), User.class);
			test = parse(param.get("test"), Test.class);
		} catch (Exception e) {
			throw BusinessException.paramsError();
		}
		return Response.ok(this.userService.delete(user, test), request);
	}

	@PostMapping("/page")
	public Response<MyPageInfo<User>> page(@RequestBody MyPageInfo<User> page) {
		return Response.ok(this.userService.page(page), request);
	}

	private <T> T parse(Object obj, Class<T> clazz) {
		return JSON.parse(JSON.toJSONString(obj), clazz);
	}
}
