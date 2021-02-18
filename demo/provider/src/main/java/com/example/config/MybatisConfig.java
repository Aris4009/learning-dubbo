package com.example.config;

import javax.sql.DataSource;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

/**
 * 从xml文件中构建mybatis配置
 */
@Configuration
@PropertySource(value = "classpath:mybatis/config/my.properties")
@MapperScan("com.example.mapper.db2")
public class MybatisConfig {

	private Logger log = LoggerFactory.getLogger(this.getClass());

	@Value("${url}")
	private String url;

	@Value("${username}")
	private String username;

	@Value("${password}")
	private String password;

	@Bean("db2")
	public DataSource dataSource() {
		HikariConfig hikariConfig = new HikariConfig();
		hikariConfig.setJdbcUrl(url);
		hikariConfig.setUsername(username);
		hikariConfig.setPassword(password);
		return new HikariDataSource(hikariConfig);
	}

	@Bean("ssf2")
	public SqlSessionFactory sqlSessionFactory() throws Exception {
		SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
		sqlSessionFactoryBean.setDataSource(dataSource());
		return sqlSessionFactoryBean.getObject();
	}
}
