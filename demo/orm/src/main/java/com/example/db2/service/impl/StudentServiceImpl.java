package com.example.db2.service.impl;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.example.db2.dao.StudentDao;
import com.example.db2.model.Student;
import com.example.db2.service.IStudentService;

@Service
public class StudentServiceImpl implements IStudentService {

	private SqlSessionFactory sqlSessionFactory;

	public StudentServiceImpl(@Qualifier("ssf2") SqlSessionFactory sqlSessionFactory) {
		this.sqlSessionFactory = sqlSessionFactory;
	}

	private Logger log = LoggerFactory.getLogger(this.getClass());

	@Override
	public List<Student> list(Student student) throws Exception {
		List<Student> list = null;
		try (SqlSession sqlSession = sqlSessionFactory.openSession()) {
			StudentDao studentDao = sqlSession.getMapper(StudentDao.class);
			list = studentDao.list(student);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			throw new Exception(e);
		}
		return list;
	}

	@Override
	public int add(Student student) throws Exception {
		int code = -1;
		try (SqlSession sqlSession = sqlSessionFactory.openSession()) {
			StudentDao studentDao = sqlSession.getMapper(StudentDao.class);
			code = studentDao.add(student);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			throw new Exception(e);
		}
		return code;
	}

	@Override
	public int modify(Student student) throws Exception {
		int code = -1;
		try (SqlSession sqlSession = sqlSessionFactory.openSession()) {
			StudentDao studentDao = sqlSession.getMapper(StudentDao.class);
			code = studentDao.modify(student);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			throw new Exception(e);
		}
		return code;
	}

	@Override
	public int del(Student student) throws Exception {
		int code = -1;
		try (SqlSession sqlSession = sqlSessionFactory.openSession()) {
			StudentDao studentDao = sqlSession.getMapper(StudentDao.class);
			code = studentDao.del(student);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			throw new Exception(e);
		}
		return code;
	}

	@Override
	public void transaction(Student student) throws Exception {
		int code = -1;
		SqlSession sqlSession = sqlSessionFactory.openSession();
		sqlSession.commit(false);
		try {
			StudentDao studentDao = sqlSession.getMapper(StudentDao.class);
			code = studentDao.add(student);
			log.info("执行插入:{}", code);
			code = studentDao.modify(student);
			log.info("执行修改:{}", code);
			sqlSession.commit();
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			sqlSession.rollback();
			throw new Exception(e);
		} finally {
			sqlSession.close();
		}
	}
}
