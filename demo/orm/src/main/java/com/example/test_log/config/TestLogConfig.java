package com.example.test_log.config;

import javax.sql.DataSource;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.transaction.TransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.core.config.GlobalConfig;
import com.baomidou.mybatisplus.core.incrementer.DefaultIdentifierGenerator;
import com.baomidou.mybatisplus.core.incrementer.IdentifierGenerator;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import com.baomidou.mybatisplus.extension.spring.MybatisSqlSessionFactoryBean;
import com.example.exception.BusinessException;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

/**
 * 构建MybatisPlus配置
 */
@Configuration
@EnableTransactionManagement(proxyTargetClass = true)
@PropertySource(value = "classpath:test_log/mybatisPlus.properties")
@MapperScan(basePackages = "com.example.test_log.dao", sqlSessionTemplateRef = "testLogSqlSessionTemplate")
@EnableAsync(proxyTargetClass = true)
public class TestLogConfig {

	private static final Logger log = LoggerFactory.getLogger(TestLogConfig.class);

	@Bean("testLog")
	public DataSource dataSource(@Value("${test.log.url}") String url, @Value("${test.log.username}") String username,
			@Value("${test.log.password}") String password) {
		HikariConfig hikariConfig = new HikariConfig();
		hikariConfig.setJdbcUrl(url);
		hikariConfig.setUsername(username);
		hikariConfig.setPassword(password);
		log.info("create tet_log:{}", url);
		return new HikariDataSource(hikariConfig);
	}

	@Bean("testLogTx")
	public TransactionManager transactionManager(@Qualifier("testLog") DataSource dataSource) {
		return new DataSourceTransactionManager(dataSource);
	}

	@Bean("testLogGlobalConfig")
	public GlobalConfig globalConfig(@Value("${test.log.mybatis.plus.banner:false}") boolean banner,
			@Value("${test.log.mybatis.plus.worker.id:1}") long workerId,
			@Value("${test.log.mybatis.plus.data.center.id:1}") long dataCenterId,
			@Value("${test.log.mybatis.plus.global.config.db.config.insert.strategy:IGNORED}") String insertStrategy,
			@Value("${test.log.mybatis.plus.global.config.db.config.update.strategy:IGNORED}") String updateStrategy,
			@Value("${test.log.mybatis.plus.global.config.db.config.select.strategy:NOT_NULL}") String selectStrategy) {
		GlobalConfig.DbConfig dbConfig = new GlobalConfig.DbConfig();
		dbConfig.setInsertStrategy(FieldStrategy.valueOf(insertStrategy));
		dbConfig.setUpdateStrategy(FieldStrategy.valueOf(updateStrategy));
		dbConfig.setSelectStrategy(FieldStrategy.valueOf(selectStrategy));
		GlobalConfig globalConfig = new GlobalConfig();
		globalConfig.setBanner(banner);
		IdentifierGenerator identifierGenerator = new DefaultIdentifierGenerator(workerId, dataCenterId);
		globalConfig.setIdentifierGenerator(identifierGenerator);
		globalConfig.setDbConfig(dbConfig);
		return globalConfig;
	}

	@Bean("testLogSqlSessionTemplate")
	public SqlSessionTemplate sqlSessionTemplate(@Qualifier("testLog") DataSource dataSource,
			@Qualifier("testLogMybatisPlusInterceptor") MybatisPlusInterceptor mybatisPlusInterceptor,
			@Qualifier("testLogGlobalConfig") GlobalConfig globalConfig) throws Exception {
		MybatisSqlSessionFactoryBean mybatisSqlSessionFactoryBean = new MybatisSqlSessionFactoryBean();
		mybatisSqlSessionFactoryBean.setDataSource(dataSource);
		mybatisSqlSessionFactoryBean.setPlugins(mybatisPlusInterceptor);
		mybatisSqlSessionFactoryBean.setGlobalConfig(globalConfig);
		SqlSessionFactory sqlSessionFactory = mybatisSqlSessionFactoryBean.getObject();
		if (sqlSessionFactory == null) {
			throw new BusinessException("init db error");
		}
		return new SqlSessionTemplate(sqlSessionFactory);
	}

	// 分页插件
	@Bean("testLogMybatisPlusInterceptor")
	public MybatisPlusInterceptor mybatisPlusInterceptor() {
		MybatisPlusInterceptor mybatisPlusInterceptor = new MybatisPlusInterceptor();
		mybatisPlusInterceptor.addInnerInterceptor(new PaginationInnerInterceptor(DbType.MYSQL));
		return mybatisPlusInterceptor;
	}
}
