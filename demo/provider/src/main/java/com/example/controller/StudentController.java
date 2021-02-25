package com.example.controller;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.db2.model.MyPage;
import com.example.db2.model.MyPageInfo;
import com.example.db2.model.Student;
import com.example.db2.service.IStudentService;
import com.example.exception.BusinessException;
import com.example.response.entity.Response;

/**
 * 请求controller
 */
@RestController
@RequestMapping("/api/student")
public class StudentController {

	private final IStudentService studentService;

	private final HttpServletRequest request;

	public StudentController(IStudentService studentService, HttpServletRequest request) {
		this.studentService = studentService;
		this.request = request;
	}

	/**
	 * 查询列表
	 * 
	 * @param student 查询参数
	 * @return 返回student列表
	 * @throws BusinessException 业务逻辑异常
	 */
	@PostMapping("/list")
	public ResponseEntity<List<Student>> list(@RequestBody Student student) throws BusinessException {
		return new ResponseEntity<>(studentService.list(student), HttpStatus.OK);
	}

	/**
	 * 新增
	 * 
	 * @param student 参数
	 * @return 返回添加的student
	 * @throws BusinessException 业务逻辑异常
	 */
	@PostMapping("/add")
	public ResponseEntity<Student> add(@RequestBody Student student) throws BusinessException {
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
	 * @param student 参数
	 * @return 影响行数
	 * @throws BusinessException 业务逻辑异常
	 */
	@PostMapping("/modify")
	public ResponseEntity<Integer> modify(@RequestBody Student student) throws BusinessException {
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
	 * @param student 参数
	 * @return 影响行数
	 * @throws BusinessException 业务逻辑异常
	 */
	@PostMapping("/del")
	public ResponseEntity<Integer> del(@RequestBody Student student) throws BusinessException {
		ResponseEntity<Integer> responseEntity;
		int code = studentService.del(student);
		if (code > 0) {
			responseEntity = new ResponseEntity<>(code, HttpStatus.OK);
		} else {
			responseEntity = new ResponseEntity<>(code, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return responseEntity;
	}

	/**
	 * 测试事务
	 * 
	 * @param student 参数
	 * @throws BusinessException 业务逻辑异常
	 */
	@PostMapping("/transaction")
	public ResponseEntity<Void> transaction(@RequestBody Student student) throws BusinessException {
		studentService.transaction(student);
		return new ResponseEntity<>(HttpStatus.OK);
	}

	/**
	 * 批量插入
	 * 
	 * @param param 批量插入数量
	 * @throws BusinessException 业务逻辑异常
	 */
	@PostMapping("/batch")
	public Response<Void> batch(@RequestBody Map<String, Integer> param) throws BusinessException {
		studentService.batch(param);
		return Response.OK(request);
	}

	/**
	 * 清空表
	 * 
	 * @param tableNames 清空表
	 * @throws BusinessException 业务逻辑异常
	 */
	@PostMapping("/truncate")
	public Response<Void> truncate(@RequestBody List<String> tableNames) throws BusinessException {
		studentService.truncate(tableNames);
		return Response.OK(request);
	}

	/**
	 * 分页查询
	 * 
	 * @param param 分页参数
	 * @return 分页数据
	 * @throws BusinessException 业务异常
	 */
	@PostMapping("/select/pageInfo")
	public Response<MyPageInfo<Student>> selectPageInfo(@RequestBody MyPageInfo<Student> param)
			throws BusinessException {
		return Response.OK(studentService.selectPageInfo(param), request);
	}

	/**
	 * 分页查询
	 * 
	 * @param param 分页参数
	 * @return 分页数据
	 * @throws BusinessException 业务异常
	 */
	@PostMapping("/select/page")
	public Response<MyPage<Student>> selectPage(@RequestBody MyPage<Student> param) throws BusinessException {
		return Response.OK(studentService.selectPage(param), request);
	}
}
