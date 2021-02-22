package com.example.interceptor;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.ui.ModelMap;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.context.request.WebRequestInterceptor;

import com.example.filter.RequestWrapper;
import com.example.filter.RequestWrapperFacade;
import com.example.json.JSON;

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
		if (method == HttpMethod.GET) {
			builder.append(JSON.toJSONString(servletWebRequest.getParameterMap()));
			log.info("url:{},method:{},params:{}", url, method, builder);
		} else if (method == HttpMethod.POST) {
			String contentType = httpServletRequest.getContentType();
			RequestWrapperFacade requestWrapperFacade = new RequestWrapperFacade(httpServletRequest);
			RequestWrapper requestWrapper = requestWrapperFacade.getRequestWrapper();
			builder.append(requestWrapper.getRequestBody());
			if (contentType.equalsIgnoreCase(MediaType.MULTIPART_FORM_DATA.getType())
					&& requestWrapper.getMultipartFileListBody() != null) {
				multipartBuilder.append(requestWrapper.getMultipartFileListBody());
				log.info("url:{},method:{},params:{},multipart-params:{}", url, method, builder, multipartBuilder);
			} else {
				log.info("url:{},method:{},params:{}", url, method, builder);
			}
		} else {
			throw new Exception("unsupported " + method + " method");
		}
	}

	@Override
	public void postHandle(WebRequest request, ModelMap model) throws Exception {
		log.debug("postHandle");
	}

	@Override
	public void afterCompletion(WebRequest request, Exception ex) throws Exception {
		log.debug("afterCompletion");
	}

}
