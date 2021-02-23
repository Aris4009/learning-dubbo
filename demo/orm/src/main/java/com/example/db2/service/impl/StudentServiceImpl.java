package com.example.db2.service.impl;

import java.util.*;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.example.db2.dao.ClassesDao;
import com.example.db2.dao.StudentClassDao;
import com.example.db2.dao.StudentDao;
import com.example.db2.model.Classes;
import com.example.db2.model.Student;
import com.example.db2.model.StudentClass;
import com.example.db2.service.IStudentService;
import com.example.exception.BusinessException;

@Service
public class StudentServiceImpl implements IStudentService {

	private final SqlSessionFactory sqlSessionFactory;

	public StudentServiceImpl(@Qualifier("ssf2") SqlSessionFactory sqlSessionFactory) {
		this.sqlSessionFactory = sqlSessionFactory;
	}

	private final Logger log = LoggerFactory.getLogger(this.getClass());

	@Override
	public List<Student> list(Student student) throws Exception {
		List<Student> list = null;
		try (SqlSession sqlSession = sqlSessionFactory.openSession()) {
			StudentDao studentDao = sqlSession.getMapper(StudentDao.class);
			list = studentDao.list(student);
		}
		return list;
	}

	@Override
	public int add(Student student) throws Exception {
		int code = -1;
		try (SqlSession sqlSession = sqlSessionFactory.openSession()) {
			StudentDao studentDao = sqlSession.getMapper(StudentDao.class);
			code = studentDao.add(student);
		}
		return code;
	}

	@Override
	public int modify(Student student) throws Exception {
		int code = -1;
		try (SqlSession sqlSession = sqlSessionFactory.openSession()) {
			StudentDao studentDao = sqlSession.getMapper(StudentDao.class);
			code = studentDao.modify(student);
		}
		return code;
	}

	@Override
	public int del(Student student) throws Exception {
		int code = -1;
		try (SqlSession sqlSession = sqlSessionFactory.openSession()) {
			StudentDao studentDao = sqlSession.getMapper(StudentDao.class);
			code = studentDao.del(student);
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
			sqlSession.rollback();
			throw e;
		} finally {
			sqlSession.close();
		}
	}

	@Override
	public void batch(int num) throws Exception {
		if (num < 1 || num > 1000) {
			throw BusinessException.paramsError("num", "必须在1~1000之间");
		}
		SqlSession sqlSession = sqlSessionFactory.openSession();
		sqlSession.commit(false);
		try {
			List<Student> studentList = new ArrayList<>();
			for (int i = 0; i < num; i++) {
				Student student = new Student();
				student.setName(UUID.randomUUID().toString());
				student.setType(new Random().nextInt(10));
				studentList.add(student);
			}
			StudentDao studentDao = sqlSession.getMapper(StudentDao.class);
			int code = studentDao.batch(studentList);
			log.info("插入students:{}", code);

			List<Classes> classesList = new ArrayList<>();
			for (int i = 0; i < Classes.CLASSES_NAME.length; i++) {
				Classes classes = new Classes();
				classes.setClassName(Classes.CLASSES_NAME[i]);
				classesList.add(classes);
			}
			ClassesDao classesDao = sqlSession.getMapper(ClassesDao.class);
			code = classesDao.batch(classesList);
			log.info("插入classes:{}", code);

			List<StudentClass> studentClassList = new ArrayList<>();
			for (Student student : studentList) {
				StudentClass studentClass = new StudentClass();
				studentClass.setStudentId(student.getId());
				studentClass.setClassId(classesList.get(new Random().nextInt(Classes.CLASSES_NAME.length)).getId());
				studentClassList.add(studentClass);
			}
			StudentClassDao studentClassDao = sqlSession.getMapper(StudentClassDao.class);
			code = studentClassDao.batch(studentClassList);
			log.info("插入student_class:{}", code);
			sqlSession.commit();
		} catch (Exception e) {
			sqlSession.rollback();
			throw e;
		} finally {
			sqlSession.close();
		}
	}

	@Override
	public void truncate(List<String> tableNames) throws Exception {
		if (tableNames == null || tableNames.isEmpty()) {
			throw BusinessException.paramsMustBeNotEmptyOrNullError("tableNames");
		}
		try (SqlSession sqlSession = sqlSessionFactory.openSession()) {
			sqlSession.commit(false);
			StudentDao studentDao = sqlSession.getMapper(StudentDao.class);
			tableNames.forEach(table -> {
				Map<String, String> map = new HashMap<>();
				map.put("tableName", table);
				studentDao.truncate(map);
			});
			sqlSession.commit();
		}
	}
}
