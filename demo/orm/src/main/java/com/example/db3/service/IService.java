package com.example.db3.service;

import java.util.Map;

import com.example.exception.BusinessException;

public interface IService {

	/**
	 * 批量插入students,classes,student_class
	 *
	 * @param param 希望插入的数量
	 * @throws BusinessException 业务异常
	 */
	void batch(Map<String, Integer> param) throws BusinessException;
}
