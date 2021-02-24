package com.example.db2.service;

import java.util.List;

import com.example.db2.model.Student;
import com.example.exception.BusinessException;

public interface IStudentService {

	/**
	 * 查询
	 * 
	 * @param student 请求参数
	 * @return 返回student列表
	 * @throws BusinessException 业务异常
	 */
	List<Student> list(Student student) throws BusinessException;

	/**
	 * 新增
	 *
	 * @param student 请求参数
	 * @return 返回影响行数
	 * @throws BusinessException 业务异常
	 */
	int add(Student student) throws BusinessException;

	/**
	 * 修改
	 *
	 * @param student 请求参数
	 * @return 返回影响行数
	 * @throws BusinessException 业务异常
	 */
	int modify(Student student) throws BusinessException;

	/**
	 * 删除
	 *
	 * @param student 请求参数
	 * @return 返回影响行数
	 * @throws BusinessException 业务异常
	 */
	int del(Student student) throws BusinessException;

	/**
	 * 测试事务
	 * 
	 * @param student 请求参数
	 * @throws BusinessException 业务异常
	 */
	void transaction(Student student) throws BusinessException;

	/**
	 * 批量插入students,classes,student_class
	 * 
	 * @param num 希望插入的数量
	 * @throws BusinessException 业务异常
	 */
	void batch(int num) throws BusinessException;

	/**
	 * 清空表数据
	 *
	 * @param tableNames 要清空的数据表列表
	 * @throws BusinessException 业务异常
	 */
	void truncate(List<String> tableNames) throws BusinessException;
}
