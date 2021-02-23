package com.example.db2.model;

import lombok.Data;

@Data
public class StudentClass {

	/**
	 * 主键
	 */
	private Long id;

	/**
	 * student id
	 */
	private Long studentId;

	/**
	 * class id
	 */
	private Long classId;

}
