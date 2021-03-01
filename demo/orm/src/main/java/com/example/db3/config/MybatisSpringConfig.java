package com.example.db3.config;

import javax.sql.DataSource;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.TransactionManager;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

/**
 * 构建mybatis配置
 */
@Configuration
@PropertySource(value = { "classpath:db3/db.properties" })
@MapperScan(basePackages = { "com.example.db3.dao" }, sqlSessionFactoryRef = "ssf3")
public class MybatisSpringConfig {

	private final Logger log = LoggerFactory.getLogger(this.getClass());

	@Bean("db3")
	public DataSource dataSource(@Value("${db3.driver}") String driver, @Value("${db3.url}") String url,
			@Value("${db3.username}") String username, @Value("${db3.password}") String password) {
		HikariConfig hikariConfig = new HikariConfig();
		hikariConfig.setDriverClassName(driver);
		hikariConfig.setJdbcUrl(url);
		hikariConfig.setUsername(username);
		hikariConfig.setPassword(password);
		log.info("create db3:{}", url);
		return new HikariDataSource(hikariConfig);
	}

	@Bean("tx")
	public TransactionManager tx(@Qualifier("db3") DataSource dataSource) {
		return new DataSourceTransactionManager(dataSource);
	}

	@Bean("ssf3")
	public SqlSessionFactory sqlSessionFactory(@Qualifier("db3") DataSource dataSource,
			@Value("${db3.mybatis.config.location}") String configLocation,
			@Value("${db3.mybatis.mapper.locations}") String mapperLocations) throws Exception {
		SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
		sqlSessionFactoryBean.setConfigLocation(new ClassPathResource(configLocation));
		sqlSessionFactoryBean.setDataSource(dataSource);
		sqlSessionFactoryBean
				.setMapperLocations(new PathMatchingResourcePatternResolver().getResources(mapperLocations));
		return sqlSessionFactoryBean.getObject();
	}

	/**
	 * 存在多个sqlSessionFactory，需要单独指定。也可以通过注解实现
	 * 
	 * @return
	 */
//	@Bean("mapperScannerConfigurer3")
//	public MapperScannerConfigurer mapperScannerConfigurer() {
//		MapperScannerConfigurer mapperScannerConfigurer = new MapperScannerConfigurer();
//		mapperScannerConfigurer.setBasePackage("com.example.db3.dao");
//		mapperScannerConfigurer.setSqlSessionFactoryBeanName("ssf3");
//		return mapperScannerConfigurer;
//	}
}
