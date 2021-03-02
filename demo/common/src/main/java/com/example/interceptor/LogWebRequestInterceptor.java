package com.example.interceptor;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.ui.ModelMap;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.context.request.WebRequestInterceptor;

import com.example.config.RequestLogConfig;
import com.example.exception.BusinessException;
import com.example.json.JSON;
import com.example.request.wrapper.RequestWrapper;
import com.example.request.wrapper.RequestWrapperFacade;
import com.example.request.wrapper.UnWrapHttpServletRequestWrapper;

/**
 * 日志请求参数拦截器
 */
public class LogWebRequestInterceptor implements WebRequestInterceptor {

	private final RequestLogConfig.RequestLog requestLog;

	public LogWebRequestInterceptor(RequestLogConfig.RequestLog requestLog) {
		this.requestLog = requestLog;
	}

	private final Logger log = LoggerFactory.getLogger(this.getClass());

	private static final int CAPACITY = 1024;

	@Override
	public void preHandle(WebRequest request) throws Exception {
		ServletWebRequest servletWebRequest = (ServletWebRequest) request;
		HttpMethod method = servletWebRequest.getHttpMethod();
		if (method != HttpMethod.GET && method != HttpMethod.POST) {
			throw new BusinessException("unsupported " + method + " method");
		}

		HttpServletRequest httpServletRequest = UnWrapHttpServletRequestWrapper.unwrap(servletWebRequest.getRequest());
		String requestId = UUID.randomUUID().toString();
		httpServletRequest.setAttribute("request-id", requestId);
		String url = httpServletRequest.getRequestURI();
		httpServletRequest.setAttribute("url", url);
		if (url.equals("/error")) {
			return;
		}
		StringBuilder builder = new StringBuilder(CAPACITY);
		StringBuilder multipartBuilder = new StringBuilder(CAPACITY);
		// 解析参数
		try {
			builder.append(getParams(httpServletRequest, method));
			String contentType = httpServletRequest.getContentType();
			if (contentType != null && contentType.contains(MediaType.MULTIPART_FORM_DATA.getType())) {
				multipartBuilder.append(getMultipartFilesInfo(httpServletRequest, method));
				if (requestLog.isPre()) {
					log.info("request-id:{},url:{},method:{},params:{},multipart-params:{}", requestId, url, method,
							builder, multipartBuilder);
				}
			} else {
				if (requestLog.isPre()) {
					log.info("request-id:{},url:{},method:{},params:{}", requestId, url, method, builder);
				}
			}
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			// 封装预处理错误，由于此处发生异常，导致afterCompletion方法无法执行而采取的补救措施
			Map<String, Object> preHandleEx = new LinkedHashMap<>();
			preHandleEx.put("request-id", requestId);
			preHandleEx.put("url", url);
			preHandleEx.put("method", method);
			preHandleEx.put("exception", e);
			httpServletRequest.setAttribute("preHandle", preHandleEx);
			throw new BusinessException("parse param error");
		}
	}

	@Override
	public void postHandle(WebRequest request, ModelMap model) throws Exception {
		log.debug("postHandle");
	}

	@Override
	public void afterCompletion(WebRequest request, Exception ex) throws Exception {
		ServletWebRequest servletWebRequest = (ServletWebRequest) request;
		HttpMethod method = servletWebRequest.getHttpMethod();
		if (method != HttpMethod.GET && method != HttpMethod.POST) {
			return;
		}

		HttpServletRequest httpServletRequest = UnWrapHttpServletRequestWrapper.unwrap(servletWebRequest.getRequest());
		String requestId = (String) httpServletRequest.getAttribute("request-id");
		String url = httpServletRequest.getRequestURI();
		StringBuilder builder = new StringBuilder(CAPACITY);
		builder.append(getParams(httpServletRequest, method));
		StringBuilder multipartBuilder = new StringBuilder(CAPACITY);
		String contentType = httpServletRequest.getContentType();
		Object obj = httpServletRequest.getAttribute("ex");
		Exception exception = (Exception) obj;
		if (contentType != null && contentType.equalsIgnoreCase(MediaType.MULTIPART_FORM_DATA.getType())) {
			multipartBuilder.append(getMultipartFilesInfo(httpServletRequest, method));
			if (obj != null && requestLog.isError()) {
				log.error("request-id:{},url:{},method:{},params:{},multipart-params:{}", requestId, url, method,
						builder, multipartBuilder, exception);
			} else if (requestLog.isAfter()) {
				log.info("request-id:{},url:{},method:{},params:{},multipart-params:{}", requestId, url, method,
						builder, multipartBuilder);
			}
		} else {
			if (obj != null && requestLog.isError()) {
				log.error("request-id:{},url:{},method:{},params:{}", requestId, url, method, builder, exception);
			} else if (requestLog.isAfter()) {
				log.info("request-id:{},url:{},method:{},params:{}", requestId, url, method, builder);
			}
		}
	}

	/**
	 * 获取GET/POST方法请求参数
	 * 
	 * @param httpServletRequest 原始请求，没有被HttpServletRequestWrapper包装
	 * @param method             HttpMethod
	 * @return 返回请求体
	 * @throws BusinessException 异常
	 */
	private String getParams(HttpServletRequest httpServletRequest, HttpMethod method) throws BusinessException {
		try {
			StringBuilder builder = new StringBuilder(CAPACITY);
			if (method == HttpMethod.GET) {
				builder.append(JSON.toJSONString(httpServletRequest.getParameterMap()));
			} else if (method == HttpMethod.POST) {
				RequestWrapperFacade requestWrapperFacade = new RequestWrapperFacade(httpServletRequest);
				RequestWrapper requestWrapper = requestWrapperFacade.getRequestWrapper();
				builder.append(requestWrapper.getRequestBody());
			}
			return builder.toString();
		} catch (Exception e) {
			throw new BusinessException(e);
		}
	}

	/**
	 * 获取POST multipart/form-data中上传的文件信息
	 * 
	 * @param httpServletRequest 原始请求，没有被HttpServletRequestWrapper包装
	 * @param method             HttpMethod
	 * @return 返回文件信息（文件名-文件大小的key-value对）
	 * @throws BusinessException 异常
	 */
	private String getMultipartFilesInfo(HttpServletRequest httpServletRequest, HttpMethod method)
			throws BusinessException {
		try {
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
		} catch (Exception e) {
			throw new BusinessException(e);
		}
	}
}
