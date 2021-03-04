package com.example.db1.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.db1.dao.ITestDao;
import com.example.db1.dao.IUserDao;
import com.example.db1.model.MyPageInfo;
import com.example.db1.model.Test;
import com.example.db1.model.User;
import com.example.db1.service.IUserService;
import com.example.exception.BusinessException;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class UserServiceImpl implements IUserService {

	private ITestDao testDao;

	private IUserDao userDao;

	public UserServiceImpl(ITestDao testDao, IUserDao userDao) {
		this.testDao = testDao;
		this.userDao = userDao;
	}

	@Override
	@Transactional(value = "tx1")
	public int[] insert(User user, Test test) throws BusinessException {
		if (user == null && test == null) {
			throw BusinessException.paramsMustBeNotEmptyOrNullError("user", "test");
		}
		int userCode = 0;
		if (user != null) {
			userCode = userDao.insert(user);
		}
		int testCode = 0;
		if (test != null) {
			testCode = testDao.insert(test);
		}
		return new int[] { userCode, testCode };
	}

	@Override
	@Transactional(value = "tx1")
	public int[] modify(User user, Test test) throws BusinessException {
		if (user == null && test == null) {
			throw BusinessException.paramsMustBeNotEmptyOrNullError("user", "test");
		}
		int userCode = 0;
		if (user != null && user.getId() != null) {
			userCode = userDao.updateById(user);
		}
		int testCode = 0;
		if (test != null && test.getId() != null) {
			testCode = testDao.updateById(test);
		}
		return new int[] { userCode, testCode };
	}

	@Override
	@Transactional(value = "tx1")
	public int[] delete(User user, Test test) throws BusinessException {
		if (user == null && test == null) {
			throw BusinessException.paramsMustBeNotEmptyOrNullError("user", "test");
		}
		int userCode = 0;
		if (user != null && user.getId() != null) {
			userCode = userDao.deleteById(user);
		}
		int testCode = 0;
		if (test != null && test.getId() != null) {
			testCode = testDao.deleteById(test);
		}
		return new int[] { userCode, testCode };
	}

	@Override
	public MyPageInfo<User> page(MyPageInfo<User> page) {
		return userDao.page(page);
	}
}
