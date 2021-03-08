package com.example.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.example.db1.model.MyPageInfo;
import com.example.db1.model.Test;
import com.example.db1.model.User;
import com.example.db1.service.IUserService;
import com.example.exception.BusinessException;

@Service
public class ProviderService implements IUserService {

	private final IUserService userService;

	private final Logger log = LoggerFactory.getLogger(this.getClass());

	public ProviderService(@Qualifier("userService") IUserService userService) {
		this.userService = userService;
	}

	@Override
	public int[] insert(User user, Test test) throws BusinessException {
		return userService.insert(user, test);
	}

	@Override
	public int[] modify(User user, Test test) throws BusinessException {
		return userService.modify(user, test);
	}

	@Override
	public int[] delete(User user, Test test) throws BusinessException {
		return userService.delete(user, test);
	}

	@Override
	public MyPageInfo<User> page(MyPageInfo<User> page) {
		log.info("进入dubbo方法");
		return userService.page(page);
	}
}
