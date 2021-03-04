package com.example.db1.config;

import javax.sql.DataSource;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.TransactionManager;

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
@PropertySource(value = "classpath:db1/mybatisPlus.properties")
@MapperScan(basePackages = "com.example.db1.dao", sqlSessionTemplateRef = "sqlSessionTemplate")
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

	@Bean("tx1")
	public TransactionManager transactionManager(@Qualifier("db1") DataSource dataSource) {
		return new DataSourceTransactionManager(dataSource);
	}

	@Bean("globalConfig")
	public GlobalConfig globalConfig(@Value("${mybatis.plus.banner:false}") boolean banner,
			@Value("${mybatis.plus.worker.id:1}") long workerId,
			@Value("${mybatis.plus.worker.id:1}") long dataCenterId,
			@Value("${mybatis.plus.global.config.db.config.insert.strategy:IGNORED}") String insertStrategy,
			@Value("${mybatis.plus.global.config.db.config.update.strategy:IGNORED}") String updateStrategy,
			@Value("${mybatis.plus.global.config.db.config.select.strategy:NOT_NULL}") String selectStrategy) {
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

	@Bean("sqlSessionTemplate")
	public SqlSessionTemplate sqlSessionTemplate(@Qualifier("db1") DataSource dataSource,
			@Value("${db1.mybatis.config.location}") String configLocation,
			@Value("${db1.mybatis.mapper.locations}") String mapperLocations,
			@Autowired MybatisPlusInterceptor mybatisPlusInterceptor, @Autowired GlobalConfig globalConfig)
			throws Exception {
		MybatisSqlSessionFactoryBean mybatisSqlSessionFactoryBean = new MybatisSqlSessionFactoryBean();
		mybatisSqlSessionFactoryBean.setDataSource(dataSource);
		mybatisSqlSessionFactoryBean.setConfigLocation(new ClassPathResource(configLocation));
		mybatisSqlSessionFactoryBean.setPlugins(mybatisPlusInterceptor);
		mybatisSqlSessionFactoryBean.setGlobalConfig(globalConfig);
		SqlSessionFactory sqlSessionFactory = mybatisSqlSessionFactoryBean.getObject();
		if (sqlSessionFactory == null) {
			throw new BusinessException("init db error");
		}
		return new SqlSessionTemplate(sqlSessionFactory);
	}

	// 分页插件
	@Bean
	public MybatisPlusInterceptor mybatisPlusInterceptor() {
		MybatisPlusInterceptor mybatisPlusInterceptor = new MybatisPlusInterceptor();
		mybatisPlusInterceptor.addInnerInterceptor(new PaginationInnerInterceptor(DbType.MYSQL));
		return mybatisPlusInterceptor;
	}
}
