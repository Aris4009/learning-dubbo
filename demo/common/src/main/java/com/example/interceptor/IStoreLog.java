package com.example.interceptor;

/**
 * 记录日志接口，可将日志记录到elasticsearch,mysql等持久化软件中
 */
public interface IStoreLog {
	public void store(RequestLog requestLog);
}
