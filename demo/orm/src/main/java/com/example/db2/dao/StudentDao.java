package com.example.db2.dao;

import java.util.List;

import com.example.db2.model.Student;

public interface StudentDao {

	/**
	 * 查询
	 * 
	 * @param student
	 * @return
	 */
	List<Student> list(Student student);

	/**
	 * 新增
	 * 
	 * @param student
	 * @return
	 */
	int add(Student student);

	/**
	 * 修改
	 * 
	 * @param student
	 * @return
	 */
	int modify(Student student);

	/**
	 * 删除
	 * 
	 * @param student
	 * @return
	 */
	int del(Student student);
}
