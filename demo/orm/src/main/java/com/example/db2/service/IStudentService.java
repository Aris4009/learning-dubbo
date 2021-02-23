package com.example.db2.service;

import java.util.List;

import com.example.db2.model.Student;

public interface IStudentService {
	/**
	 * 查询
	 *
	 * @param student 请求参数
	 * @return 查询
	 */
	List<Student> list(Student student) throws Exception;

	/**
	 * 新增
	 *
	 * @param student 请求参数
	 * @return 新增
	 */
	int add(Student student) throws Exception;

	/**
	 * 修改
	 *
	 * @param student 请求参数
	 * @return 修改
	 */
	int modify(Student student) throws Exception;

	/**
	 * 删除
	 *
	 * @param student 请求参数
	 * @return 删除
	 */
	int del(Student student) throws Exception;

	/**
	 * 测试事务
	 * 
	 * @param student 请求参数
	 */
	void transaction(Student student) throws Exception;

	/**
	 * 批量插入students,classes,student_class
	 * 
	 * @param num 希望插入的数量
	 */
	void batch(int num) throws Exception;

	/**
	 * 清空表数据
	 *
	 * @param tableNames 要清空的数据表列表
	 * @return
	 */
	void truncate(List<String> tableNames) throws Exception;
}
