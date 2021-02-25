package com.example.db2.config;

import java.util.Properties;

import javax.sql.DataSource;

import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.Environment;
import org.springframework.core.env.MapPropertySource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import com.github.pagehelper.PageInterceptor;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

/**
 * 构建mybatis配置
 */
@Configuration
@PropertySource(value = { "classpath:db2/db.properties", "classpath:db2/page-helper.properties" })
public class MybatisConfig {

	private final Logger log = LoggerFactory.getLogger(this.getClass());

	private static final String PAGE_HELP_PREFIX = "db2.page.helper.";

	@Bean("db2")
	public DataSource dataSource(@Value("${db2.driver}") String driver, @Value("${db2.url}") String url,
			@Value("${db2.username}") String username, @Value("${db2.password}") String password) {
		HikariConfig hikariConfig = new HikariConfig();
		hikariConfig.setDriverClassName(driver);
		hikariConfig.setJdbcUrl(url);
		hikariConfig.setUsername(username);
		hikariConfig.setPassword(password);
		hikariConfig.setAutoCommit(false);
		log.info("create db2:{}", url);
		return new HikariDataSource(hikariConfig);
	}

	@Bean("ssf2")
	public SqlSessionFactory sqlSessionFactory(@Qualifier("db2") DataSource dataSource,
			@Value("${db2.mybatis.config.location}") String configLocation,
			@Value("${db2.mybatis.mapper.locations}") String mapperLocations,
			@Qualifier("db2PageHelperPlugin") Interceptor pageHelperPlugin) throws Exception {
		SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
		sqlSessionFactoryBean.setConfigLocation(new ClassPathResource(configLocation));
		sqlSessionFactoryBean.setDataSource(dataSource);
		sqlSessionFactoryBean
				.setMapperLocations(new PathMatchingResourcePatternResolver().getResources(mapperLocations));
		// 添加pageHelper插件，如果使用配置文件，注意plugins在配置文件中的顺序
		sqlSessionFactoryBean.setPlugins(pageHelperPlugin);
		return sqlSessionFactoryBean.getObject();
	}

	@Bean("db2PageHelperPlugin")
	public Interceptor pageHelperPlugin(@Autowired Environment environment) {
		Properties properties = new Properties();
		Interceptor interceptor = new PageInterceptor();
		ConfigurableEnvironment configurableEnvironment = (ConfigurableEnvironment) environment;
		configurableEnvironment.getPropertySources().forEach(propertySource -> {
			if (propertySource instanceof MapPropertySource) {
				MapPropertySource mapPropertySource = (MapPropertySource) propertySource;
				for (String key : mapPropertySource.getPropertyNames()) {
					if (key.startsWith(PAGE_HELP_PREFIX)) {
						String k = key.substring(PAGE_HELP_PREFIX.length(), key.length());
						properties.put(k, mapPropertySource.getProperty(key));
					}
				}
			}
		});
		interceptor.setProperties(properties);
		return interceptor;
	}
}
