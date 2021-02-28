package com.example.db3.service.impl;

import java.util.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.db2.model.Classes;
import com.example.db2.model.Student;
import com.example.db2.model.StudentClass;
import com.example.db3.dao.ClassMapper;
import com.example.db3.dao.StudentClassMapper;
import com.example.db3.dao.StudentMapper;
import com.example.db3.service.IService;
import com.example.exception.BusinessException;

@Service
public class ServiceImpl implements IService {

	private final Logger log = LoggerFactory.getLogger(this.getClass());

	private final StudentMapper studentMapper;

	private final ClassMapper classMapper;

	private final StudentClassMapper studentClassMapper;

	public ServiceImpl(StudentMapper studentMapper, ClassMapper classMapper, StudentClassMapper studentClassMapper) {
		this.studentMapper = studentMapper;
		this.classMapper = classMapper;
		this.studentClassMapper = studentClassMapper;
	}

	@Override
	@Transactional
	public void batch(Map<String, Integer> param) throws BusinessException {
		if (param == null || param.get("num") == null) {
			throw BusinessException.paramsMustBeNotEmptyOrNullError("num");
		}
		int num = param.get("num");
		if (num < 1 || num > 1000) {
			throw BusinessException.paramsError("num", "必须在1~1000之间");
		}
		List<Student> studentList = new ArrayList<>();
		for (int i = 0; i < num; i++) {
			Student student = new Student();
			student.setName(UUID.randomUUID().toString());
			student.setType(new Random().nextInt(10));
			studentList.add(student);
		}
		int code = studentMapper.batch(studentList);
		log.info("插入students:{}", code);

		List<Classes> classesList = new ArrayList<>();
		for (int i = 0; i < Classes.CLASSES_NAME.length; i++) {
			Classes classes = new Classes();
			classes.setClassName(Classes.CLASSES_NAME[i]);
			classesList.add(classes);
		}
		code = classMapper.batch(classesList);
		log.info("插入classes:{}", code);

		List<StudentClass> studentClassList = new ArrayList<>();
		for (Student student : studentList) {
			StudentClass studentClass = new StudentClass();
			studentClass.setStudentId(student.getId());
			studentClass.setClassId(classesList.get(new Random().nextInt(Classes.CLASSES_NAME.length)).getId());
			studentClassList.add(studentClass);
		}
		code = studentClassMapper.batch(studentClassList);
		log.info("插入student_class:{}", code);
	}
}
