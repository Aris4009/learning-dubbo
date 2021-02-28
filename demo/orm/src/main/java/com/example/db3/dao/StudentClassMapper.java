package com.example.db3.dao;

import java.util.List;

import com.example.db2.model.StudentClass;

public interface StudentClassMapper {
	/**
	 * @param cls 参数
	 * @return 查询列表
	 */
	List<StudentClass> list(StudentClass cls);

	/**
	 * @param cls 参数
	 * @return 新增
	 */
	int add(StudentClass cls);

	/**
	 * @param cls 参数
	 * @return 修改
	 */
	int modify(StudentClass cls);

	/**
	 * @param cls 参数
	 * @return 删除
	 */
	int del(StudentClass cls);

	/**
	 * @param list 参数
	 * @return 批量添加
	 */
	int batch(List<StudentClass> list);
}
