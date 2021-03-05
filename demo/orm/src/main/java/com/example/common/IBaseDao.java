package com.example.common;

import java.io.Serializable;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.db1.model.MyPageInfo;

public interface IBaseDao<T extends Serializable> extends BaseMapper<T> {

	IPage<T> page(Page<T> page, T param);

	/**
	 * 分页方法
	 *
	 * @param myPageInfo 分页参数
	 * @return 分页数据
	 */
	default MyPageInfo<T> page(MyPageInfo<T> myPageInfo) {
		Page<T> page = new Page<>();
		page.setCurrent(myPageInfo.getPageNum());
		page.setSize(myPageInfo.getPageSize());
		page.setOptimizeCountSql(false);
		page = selectPage(page, new QueryWrapper<T>(myPageInfo.getParam()));
		myPageInfo.setList(page.getRecords());
		myPageInfo.setTotal(page.getTotal());
		myPageInfo.setPages(page.getPages());

		return myPageInfo;
	}
}
