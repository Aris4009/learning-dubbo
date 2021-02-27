package com.example.controller;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
	public Response<List<Student>> list(@RequestBody Student student) throws BusinessException {
		return Response.ok(studentService.list(student), request);
	}

	/**
	 * 新增
	 * 
	 * @param student 参数
	 * @return 返回添加的student
	 * @throws BusinessException 业务逻辑异常
	 */
	@PostMapping("/add")
	public Response<Student> add(@RequestBody Student student) throws BusinessException {
		studentService.add(student);
		return Response.ok(student, request);
	}

	/**
	 * 修改
	 * 
	 * @param student 参数
	 * @return 影响行数
	 * @throws BusinessException 业务逻辑异常
	 */
	@PostMapping("/modify")
	public Response<Integer> modify(@RequestBody Student student) throws BusinessException {
		return Response.ok(studentService.modify(student), request);
	}

	/**
	 * 删除
	 * 
	 * @param student 参数
	 * @return 影响行数
	 * @throws BusinessException 业务逻辑异常
	 */
	@PostMapping("/del")
	public Response<Integer> del(@RequestBody Student student) throws BusinessException {
		return Response.ok(studentService.del(student), request);
	}

	/**
	 * 测试事务
	 * 
	 * @param student 参数
	 * @throws BusinessException 业务逻辑异常
	 */
	@PostMapping("/transaction")
	public Response<Void> transaction(@RequestBody Student student) throws BusinessException {
		studentService.transaction(student);
		return Response.ok(request);
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
		return Response.ok(request);
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
		return Response.ok(request);
	}

	/**
	 * 分页查询
	 * 
	 * @param myPageInfo 分页参数
	 * @return 分页数据
	 * @throws BusinessException 业务异常
	 */
	@PostMapping("/select/page")
	public Response<MyPageInfo<Student>> selectPage(@RequestBody MyPageInfo<Student> myPageInfo)
			throws BusinessException {
		return Response.ok(studentService.selectPage(myPageInfo), request);
	}
}
