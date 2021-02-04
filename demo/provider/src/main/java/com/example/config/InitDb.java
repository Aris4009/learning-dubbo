package com.example.config;

import javax.annotation.PostConstruct;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * 初始化数据库表
 */
@Component
public class InitDb {

	private final SqlSessionFactory sqlSessionFactory;

	private final Logger log = LoggerFactory.getLogger(this.getClass());

	private static final String TRUNCATE_TEST = "truncate table test";

	private static final String TRUNCATE_USER = "truncate table user";

	public InitDb(SqlSessionFactory sqlSessionFactory) {
		this.sqlSessionFactory = sqlSessionFactory;
	}

	@PostConstruct
	public void init() {
		try (SqlSession session = sqlSessionFactory.openSession()) {
			session.update(TRUNCATE_TEST);
			log.info("{}", TRUNCATE_TEST);

			session.update(TRUNCATE_USER);
			log.info("{}", TRUNCATE_USER);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
	}
}
