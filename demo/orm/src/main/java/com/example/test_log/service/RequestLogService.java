package com.example.test_log.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.db1.model.MyPageInfo;
import com.example.interceptor.IStoreLog;
import com.example.interceptor.RequestLog;
import com.example.test_log.dao.IRequestLogDao;

/**
 * 将请求日志记录到数据库
 */
@Service
public class RequestLogService implements IStoreLog {

	private final IRequestLogDao requestLogDao;

	private final Logger log = LoggerFactory.getLogger(this.getClass());

	public RequestLogService(IRequestLogDao requestLogDao) {
		this.requestLogDao = requestLogDao;
	}

	@Override
	@Async
	@Transactional("testLogTx")
	public void store(RequestLog requestLog) {
		try {
			requestLogDao.insert(requestLog);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
	}

	public MyPageInfo<RequestLog> page(MyPageInfo<RequestLog> page) {
		return requestLogDao.page(page);
	}
}
