package com.example.db2.model;

import java.io.Serializable;

import com.github.pagehelper.ISelect;
import com.github.pagehelper.Page;

/**
 * Page分页
 * 
 * @param <T>
 */
public class MyPage<T extends Serializable> extends Page<T> {

	private static final long serialVersionUID = 2043774083692089541L;

	private T param;

	public MyPage() {
		this.setPageNum(1);
		this.pageSize(10);
	}

	public T getParam() {
		return param;
	}

	public void setParam(T param) {
		this.param = param;
	}

	public <E extends Serializable> MyPage<E> doSelectPage(MyPage<E> myPage, ISelect select) {
		Page<E> page = super.doSelectPage(select);
		if (page.getResult() == null || page.getResult().isEmpty()) {
			return myPage;
		}
		myPage.getResult().addAll(page.getResult());
		return myPage;
	}
}
