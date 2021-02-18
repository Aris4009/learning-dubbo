package com.example.config;

import javax.sql.DataSource;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
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
@MapperScan("com.example.mapper.db1")
public class BeanConfig {

	private static final Logger log = LoggerFactory.getLogger(BeanConfig.class);

	private final Environment env;

	public BeanConfig(Environment env) {
		this.env = env;
	}

	@Bean("db1")
	public DataSource dataSource() {
		HikariConfig hikariConfig = new HikariConfig();
		hikariConfig.setJdbcUrl(env.getProperty("spring.datasource.url"));
		hikariConfig.setUsername(env.getProperty("spring.datasource.username"));
		hikariConfig.setPassword(env.getProperty("spring.datasource.password"));
		return new HikariDataSource(hikariConfig);
	}

	@Bean("ssf1")
	public SqlSessionFactory sqlSessionFactory() throws Exception {
		SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
		sqlSessionFactoryBean.setDataSource(dataSource());
		return sqlSessionFactoryBean.getObject();
	}

	@Bean
	public static BeanPostProcessor initDataSource() {
		return new BeanPostProcessor() {
			@Override
			public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
				if (!bean.getClass().getName().contains("org.springframework")) {
					log.info("{}:{}", bean.getClass().getName(), beanName);
				}
				return bean;
			}
		};
	}
}
