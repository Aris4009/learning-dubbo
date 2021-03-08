package com.example.db1.model;

import java.io.Serializable;
import java.util.List;

import lombok.Data;

@Data
public class MyPageInfo<T extends Serializable> implements Serializable {

	private static final long serialVersionUID = 7183896354085675654L;

	private transient T param;

	private List<T> list;

	private long pageNum = 1;

	private long pageSize = 10;

	private long total = 0;

	private long pages = 0;

	public long getPageSize() {
		if (this.pageSize > 100) {
			return 100;
		} else {
			return this.pageSize;
		}
	}
}
