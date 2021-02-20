package com.example.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.db2.model.Student;
import com.example.db2.service.IStudentService;

/**
 * 请求controller
 */
@RestController
@RequestMapping("/api/student")
public class StudentController {

	private Logger log = LoggerFactory.getLogger(this.getClass());

	private IStudentService studentService;

	public StudentController(IStudentService studentService) {
		this.studentService = studentService;
	}

	/**
	 * 查询列表
	 * 
	 * @param student
	 * @return
	 */
	@PostMapping("/list")
	public ResponseEntity<List<Student>> list(@RequestBody Student student) {
		log.info("{}", student);
		ResponseEntity<List<Student>> responseEntity;
		try {
			responseEntity = new ResponseEntity<>(studentService.list(student), HttpStatus.OK);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			responseEntity = new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return responseEntity;
	}

	/**
	 * 新增
	 * 
	 * @param student
	 * @return
	 */
	@PostMapping("/add")
	public ResponseEntity<Student> add(@RequestBody Student student) {
		log.info("{}", student);
		ResponseEntity<Student> responseEntity;
		try {
			int code = studentService.add(student);
			if (code > 0) {
				responseEntity = new ResponseEntity<>(student, HttpStatus.OK);
			} else {
				responseEntity = new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
			}
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			responseEntity = new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return responseEntity;
	}

	/**
	 * 修改
	 * 
	 * @param student
	 * @return
	 */
	@PostMapping("/modify")
	public ResponseEntity<Integer> modify(@RequestBody Student student) {
		log.info("{}", student);
		ResponseEntity<Integer> responseEntity;
		try {
			int code = studentService.modify(student);
			if (code > 0) {
				responseEntity = new ResponseEntity<>(code, HttpStatus.OK);
			} else {
				responseEntity = new ResponseEntity<>(code, HttpStatus.INTERNAL_SERVER_ERROR);
			}
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			responseEntity = new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return responseEntity;
	}

	/**
	 * 删除
	 * 
	 * @param student
	 * @return
	 */
	@PostMapping("/del")
	public ResponseEntity<Integer> del(@RequestBody Student student) {
		log.info("{}", student);
		ResponseEntity<Integer> responseEntity;
		try {
			int code = studentService.del(student);
			if (code > 0) {
				responseEntity = new ResponseEntity<>(code, HttpStatus.OK);
			} else {
				responseEntity = new ResponseEntity<>(code, HttpStatus.INTERNAL_SERVER_ERROR);
			}
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			responseEntity = new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return responseEntity;
	}

	@PostMapping("/transaction")
	public ResponseEntity<Void> transaction(@RequestBody Student student) {
		log.info("{}", student);
		ResponseEntity<Void> responseEntity;
		try {
			studentService.transaction(student);
			responseEntity = new ResponseEntity<>(HttpStatus.OK);
		} catch (Exception e) {
			responseEntity = new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return responseEntity;
	}
}
