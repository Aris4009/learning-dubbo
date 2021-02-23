package com.example.db1.config;

import javax.sql.DataSource;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

/**
 * 构建MybatisPlus配置
 */
@Configuration
@MapperScan("db1/mapper/*.xml")
@PropertySource(value = "classpath:db1/mybatisPlus.properties")
public class MybatisPlusConfig {

	private static final Logger log = LoggerFactory.getLogger(MybatisPlusConfig.class);

	@Bean("db1")
	public DataSource dataSource(@Value("${db1.url}") String url, @Value("${db1.username}") String username,
			@Value("${db1.password}") String password) {
		HikariConfig hikariConfig = new HikariConfig();
		hikariConfig.setJdbcUrl(url);
		hikariConfig.setUsername(username);
		hikariConfig.setPassword(password);
		log.info("create db1:{}", url);
		return new HikariDataSource(hikariConfig);
	}

	@Bean("ssf1")
	public SqlSessionFactory sqlSessionFactory(@Qualifier("db1") DataSource dataSource) throws Exception {
		SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
		sqlSessionFactoryBean.setDataSource(dataSource);
		return sqlSessionFactoryBean.getObject();
	}

	@Bean
	public static BeanPostProcessor initDataSource(@Autowired Environment env) {
		return new BeanPostProcessor() {
			@Override
			public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
//				log.info("{}:{}", bean.getClass().getName(), beanName);
				return bean;
			}
		};
	}
}
