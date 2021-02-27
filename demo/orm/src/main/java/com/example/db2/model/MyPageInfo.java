package com.example.db2.model;

import java.io.Serializable;
import java.util.List;

import com.github.pagehelper.ISelect;
import com.github.pagehelper.PageInfo;
import com.github.pagehelper.page.PageMethod;

import lombok.Data;

@Data
public class MyPageInfo<T extends Serializable> {

	private T param;

	private List<T> list;

	private int pageNum = 1;

	private int pageSize = 10;

	/**
	 * 分页方法
	 *
	 * @param myPageInfo 分页参数
	 * @param select     查询接口
	 * @param <E>        类型
	 * @return 分页数据
	 */
	public static <E extends Serializable> MyPageInfo<E> page(MyPageInfo<E> myPageInfo, ISelect select) {
		PageInfo<E> pageInfo = new PageInfo<>();
		pageInfo.setPageNum(myPageInfo.getPageNum());
		pageInfo.setPageSize(myPageInfo.getPageSize());
		pageInfo = PageMethod.startPage(pageInfo.getPageNum(), pageInfo.getPageSize()).doSelectPageInfo(select);
		myPageInfo.setList(pageInfo.getList());
		return myPageInfo;
	}
}
