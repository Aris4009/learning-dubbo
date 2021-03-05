package com.example.config;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.GsonHttpMessageConverter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.example.interceptor.IStoreLog;
import com.example.interceptor.LogHandlerInterceptor;
import com.example.json.JSON;
import com.example.test_log.service.RequestLogService;

/**
 * web配置
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

	private final RequestLogConfig requestLogConfig;

	private final RequestLogService requestLogService;

	public WebConfig(RequestLogConfig requestLogConfig, RequestLogService requestLogService) {
		this.requestLogConfig = requestLogConfig;
		this.requestLogService = requestLogService;
	}

	@Override
	public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
		GsonHttpMessageConverter converter = new GsonHttpMessageConverter();
		converter.setGson(JSON.gson);
		converter.setDefaultCharset(StandardCharsets.UTF_8);
		converters.add(0, converter);
	}

	@Override
	public void addCorsMappings(CorsRegistry registry) {
		registry.addMapping("/api/**");
	}

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		List<IStoreLog> list = new ArrayList<>();
		list.add(this.requestLogService);
		registry.addInterceptor(new LogHandlerInterceptor(this.requestLogConfig, list));
	}
}
