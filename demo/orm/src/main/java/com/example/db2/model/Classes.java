package com.example.db2.model;

import lombok.Data;

@Data
public class Classes {

	public static final String[] CLASSES_NAME = { "语文", "数学", "英语", "音乐", "体育" };

	/**
	 * 主键
	 */
	private Long id;

	/**
	 * 名称
	 */
	private String className;
}
