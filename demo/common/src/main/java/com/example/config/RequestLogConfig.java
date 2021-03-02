package com.example.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import lombok.Builder;
import lombok.Data;

/**
 * 请求日志配置
 */
@Configuration
@PropertySource(value = "classpath:log-request.properties", ignoreResourceNotFound = true)
public class RequestLogConfig {

	@Bean
	public RequestLog requestLog(@Value("${request.pre:true}") boolean pre,
			@Value("${request.after:false}") boolean after, @Value("${request.error:true}") boolean error) {
		return RequestLog.builder().pre(pre).after(after).error(error).build();
	}

	@Data
	@Builder
	public static class RequestLog {

		private boolean pre;

		private boolean error;

		private boolean after;
	}
}
