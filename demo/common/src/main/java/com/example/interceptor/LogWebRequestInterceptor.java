package com.example.interceptor;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.ui.ModelMap;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.context.request.WebRequestInterceptor;

import com.example.exception.BusinessException;
import com.example.json.JSON;
import com.example.request.wrapper.RequestWrapper;
import com.example.request.wrapper.RequestWrapperFacade;

/**
 * 日志请求参数拦截器
 */
public class LogWebRequestInterceptor implements WebRequestInterceptor {

	private final Logger log = LoggerFactory.getLogger(this.getClass());

	private static final int CAPACITY = 1024;

	@Override
	public void preHandle(WebRequest request) throws Exception {
		ServletWebRequest servletWebRequest = (ServletWebRequest) request;
		HttpServletRequest httpServletRequest = servletWebRequest.getRequest();
		String url = httpServletRequest.getRequestURI();
		if (url.equals("/error")) {
			return;
		}
		HttpMethod method = servletWebRequest.getHttpMethod();
		StringBuilder builder = new StringBuilder(CAPACITY);
		StringBuilder multipartBuilder = new StringBuilder(CAPACITY);
		if (method == HttpMethod.GET || method == HttpMethod.POST) {
			builder.append(getParams(servletWebRequest));
			String contentType = httpServletRequest.getContentType();
			if (contentType.equalsIgnoreCase(MediaType.MULTIPART_FORM_DATA.getType())) {
				multipartBuilder.append(getMultipartFilesInfo(servletWebRequest));
				log.info("url:{},method:{},params:{},multipart-params:{}", url, method, builder, multipartBuilder);
			} else {
				log.info("url:{},method:{},params:{}", url, method, builder);
			}
		} else {
			throw new BusinessException("unsupported " + method + " method");
		}
	}

	@Override
	public void postHandle(WebRequest request, ModelMap model) throws Exception {
		log.debug("postHandle");
	}

	@Override
	public void afterCompletion(WebRequest request, Exception ex) throws Exception {
		ServletWebRequest servletWebRequest = (ServletWebRequest) request;
		Object obj = servletWebRequest.getAttribute("ex", RequestAttributes.SCOPE_REQUEST);
		if (obj == null) {
			return;
		}
		Exception exception = (Exception) obj;
		HttpServletRequest httpServletRequest = servletWebRequest.getRequest();
		String url = httpServletRequest.getRequestURI();
		HttpMethod method = servletWebRequest.getHttpMethod();
		StringBuilder builder = new StringBuilder(CAPACITY);
		StringBuilder multipartBuilder = new StringBuilder(CAPACITY);
		if (method == HttpMethod.GET || method == HttpMethod.POST) {
			builder.append(getParams(servletWebRequest));
			String contentType = httpServletRequest.getContentType();
			if (contentType.equalsIgnoreCase(MediaType.MULTIPART_FORM_DATA.getType())) {
				multipartBuilder.append(getMultipartFilesInfo(servletWebRequest));
				log.error("url:{},method:{},params:{},multipart-params:{}", url, method, builder, multipartBuilder,
						exception);
			} else {
				log.error("url:{},method:{},params:{}", url, method, builder, exception);
			}
		}
	}

	/**
	 * 获取GET/POST方法请求参数
	 * 
	 * @param servletWebRequest 请求
	 * @return 返回请求体
	 * @throws Exception 异常
	 */
	private String getParams(ServletWebRequest servletWebRequest) throws Exception {
		HttpMethod method = servletWebRequest.getHttpMethod();
		HttpServletRequest httpServletRequest = servletWebRequest.getRequest();
		StringBuilder builder = new StringBuilder(CAPACITY);
		if (method == HttpMethod.GET) {
			builder.append(JSON.toJSONString(servletWebRequest.getParameterMap()));
		} else if (method == HttpMethod.POST) {
			RequestWrapperFacade requestWrapperFacade = new RequestWrapperFacade(httpServletRequest);
			RequestWrapper requestWrapper = requestWrapperFacade.getRequestWrapper();
			builder.append(requestWrapper.getRequestBody());
		}
		return builder.toString();
	}

	/**
	 * 获取POST multipart/form-data中上传的文件信息
	 * 
	 * @param servletWebRequest 请求
	 * @return 返回文件信息（文件名-文件大小的key-value对）
	 * @throws Exception 异常
	 */
	private String getMultipartFilesInfo(ServletWebRequest servletWebRequest) throws Exception {
		HttpMethod method = servletWebRequest.getHttpMethod();
		HttpServletRequest httpServletRequest = servletWebRequest.getRequest();
		StringBuilder multipartBuilder = new StringBuilder(CAPACITY);
		if (method == HttpMethod.POST) {
			String contentType = httpServletRequest.getContentType();
			RequestWrapperFacade requestWrapperFacade = new RequestWrapperFacade(httpServletRequest);
			RequestWrapper requestWrapper = requestWrapperFacade.getRequestWrapper();
			if (contentType.equalsIgnoreCase(MediaType.MULTIPART_FORM_DATA.getType())
					&& requestWrapper.getMultipartFileListBody() != null) {
				multipartBuilder.append(requestWrapper.getMultipartFileListBody());
			}
		}
		return multipartBuilder.toString();
	}
}
