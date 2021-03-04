package com.example.db1.service;

import com.example.db1.model.MyPageInfo;
import com.example.db1.model.Test;
import com.example.db1.model.User;
import com.example.exception.BusinessException;

public interface IUserService {

	int[] insert(User user, Test test) throws BusinessException;

	int[] modify(User user, Test test) throws BusinessException;

	int[] delete(User user, Test test) throws BusinessException;

	MyPageInfo<User> page(MyPageInfo<User> page);
}
