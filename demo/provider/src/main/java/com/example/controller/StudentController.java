package com.example.controller;

import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.db2.model.Student;
import com.example.db2.service.IStudentService;
import com.example.exception.BusinessException;

/**
 * 请求controller
 */
@RestController
@RequestMapping("/api/student")
public class StudentController {

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
	public ResponseEntity<List<Student>> list(@RequestBody Student student) throws Exception {
		return new ResponseEntity<>(studentService.list(student), HttpStatus.OK);
	}

	/**
	 * 新增
	 * 
	 * @param student
	 * @return
	 */
	@PostMapping("/add")
	public ResponseEntity<Student> add(@RequestBody Student student) throws Exception {
		ResponseEntity<Student> responseEntity;
		int code = studentService.add(student);
		if (code > 0) {
			responseEntity = new ResponseEntity<>(student, HttpStatus.OK);
		} else {
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
	public ResponseEntity<Integer> modify(@RequestBody Student student) throws Exception {
		ResponseEntity<Integer> responseEntity;
		int code = studentService.modify(student);
		if (code > 0) {
			responseEntity = new ResponseEntity<>(code, HttpStatus.OK);
		} else {
			responseEntity = new ResponseEntity<>(code, HttpStatus.INTERNAL_SERVER_ERROR);
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
	public ResponseEntity<Integer> del(@RequestBody Student student) throws Exception {
		ResponseEntity<Integer> responseEntity;
		int code = studentService.del(student);
		if (code > 0) {
			responseEntity = new ResponseEntity<>(code, HttpStatus.OK);
		} else {
			responseEntity = new ResponseEntity<>(code, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return responseEntity;
	}

	@PostMapping("/transaction")
	public ResponseEntity<Void> transaction(@RequestBody Student student) throws Exception {
		studentService.transaction(student);
		return new ResponseEntity<>(HttpStatus.OK);
	}

	/**
	 * 批量插入
	 * 
	 * @param param 批量插入数量
	 * @return
	 * @throws Exception
	 */
	@PostMapping("/batch")
	public ResponseEntity<Void> batch(@RequestBody Map<String, Integer> param) throws Exception {
		studentService.batch(param.get("num"));
		return new ResponseEntity<>(HttpStatus.OK);
	}

	/**
	 * 清空表
	 * 
	 * @param tableNames 清空表
	 * @return
	 * @throws BusinessException
	 */
	@PostMapping("/truncate")
	public ResponseEntity<Void> truncate(@RequestBody List<String> tableNames) throws Exception {
		studentService.truncate(tableNames);
		return new ResponseEntity<>(HttpStatus.OK);
	}
}
