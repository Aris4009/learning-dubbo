package com.example.db2.service;

import java.util.List;

import com.example.db2.model.Student;

public interface IStudentService {
	/**
	 * 查询
	 *
	 * @param student
	 * @return
	 */
	List<Student> list(Student student) throws Exception;

	/**
	 * 新增
	 *
	 * @param student
	 * @return
	 */
	int add(Student student) throws Exception;

	/**
	 * 修改
	 *
	 * @param student
	 * @return
	 */
	int modify(Student student) throws Exception;

	/**
	 * 删除
	 *
	 * @param student
	 * @return
	 */
	int del(Student student) throws Exception;

	/**
	 * 测试事务
	 * 
	 * @param student
	 */
	void transaction(Student student) throws Exception;
}
