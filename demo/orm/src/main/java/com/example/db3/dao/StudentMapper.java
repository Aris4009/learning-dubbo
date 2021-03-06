package com.example.db3.dao;

import java.util.List;
import java.util.Map;

import com.example.db2.model.Student;

public interface StudentMapper {

	/**
	 * @param student 参数
	 * @return 查询
	 */
	List<Student> list(Student student);

	/**
	 * @param student 参数
	 * @return 新增
	 */
	int add(Student student);

	/**
	 * @param student 参数
	 * @return 修改
	 */
	int modify(Student student);

	/**
	 * @param student 参数
	 * @return 删除
	 */
	int del(Student student);

	/**
	 * @param list 参数
	 * @return 批量插入
	 */
	int batch(List<Student> list);

	/**
	 * 清空表数据
	 * 
	 * @param map 要清空的数据表
	 * @return
	 */
	int truncate(Map<String, String> map);

	List<Student> selectPage(Student student);
}
