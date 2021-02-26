package com.example.db2.model;

import java.io.Serializable;

import com.example.json.JSON;

public class Student implements Serializable {

	private static final long serialVersionUID = 5198598465867698816L;

	/**
	 * 主键
	 */
	private Long id;

	/**
	 * 名称
	 */
	private String name;

	/**
	 * 类型
	 */
	private Integer type;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	@Override
	public String toString() {
		return JSON.toJSONString(this);
	}
}
