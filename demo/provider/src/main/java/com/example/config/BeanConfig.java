package com.example.config;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

/**
 * bean配置
 */
@Configuration
public class BeanConfig {

	private static final Logger log = LoggerFactory.getLogger(BeanConfig.class);

	private final Environment env;

	public BeanConfig(Environment env) {
		this.env = env;
	}

	@Bean
	public DataSource dataSource() {
		HikariConfig hikariConfig = new HikariConfig();
		hikariConfig.setJdbcUrl(env.getProperty("spring.datasource.hikari.jdbc-url"));
		hikariConfig.setUsername(env.getProperty("spring.datasource.hikari.username"));
		hikariConfig.setPassword(env.getProperty("spring.datasource.hikari.password"));
		return new HikariDataSource(hikariConfig);
	}

	@Bean
	public BeanPostProcessor initDataSource() {
		return new BeanPostProcessor() {
			@Override
			public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
				log.info("{}", beanName);
				return bean;
			}
		};
	}
}
