package com.example.db2.service;

import java.util.List;

import com.example.db2.model.Student;
import com.example.exception.BusinessException;

public interface IStudentService {
	/**
	 * 查询
	 *
	 * @param student 请求参数
	 * @return 查询
	 */
	List<Student> list(Student student) throws BusinessException, BusinessException;

	/**
	 * 新增
	 *
	 * @param student 请求参数
	 * @return 新增
	 */
	int add(Student student) throws BusinessException;

	/**
	 * 修改
	 *
	 * @param student 请求参数
	 * @return 修改
	 */
	int modify(Student student) throws BusinessException;

	/**
	 * 删除
	 *
	 * @param student 请求参数
	 * @return 删除
	 */
	int del(Student student) throws BusinessException;

	/**
	 * 测试事务
	 * 
	 * @param student 请求参数
	 */
	void transaction(Student student) throws BusinessException;
}
