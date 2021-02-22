package com.example.config;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

import javax.servlet.ReadListener;
import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.Part;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.ui.ModelMap;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.context.request.WebRequestInterceptor;

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
			RequestWrapper requestWrapper = new RequestWrapper(httpServletRequest);
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

	private static class RequestWrapper extends HttpServletRequestWrapper {

		private String requestBody = null;

		private String multipartFileListBody = null;

		private static final int BUFF_SIZE = 4096;

		private static final int CAPACITY = 1024;

		/**
		 * Constructs a request object wrapping the given request.
		 *
		 * @param request The request to wrap
		 * @throws IllegalArgumentException if the request is null
		 */
		public RequestWrapper(HttpServletRequest request) {
			super(request);
		}

		public String getRequestBody() {
			return requestBody;
		}

		public String getMultipartFileListBody() {
			return multipartFileListBody;
		}

		@Override
		public ServletInputStream getInputStream() throws IOException {
			ServletInputStream servletInputStream = this.getRequest().getInputStream();
			byte[] buff = new byte[BUFF_SIZE];
			StringBuilder builder = new StringBuilder(CAPACITY);
			int n = 0;
			while ((n = servletInputStream.read(buff)) != -1) {
				builder.append(new String(buff, 0, n));
			}
			if (builder.length() > 0) {
				this.requestBody = builder.toString();
			}

			ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(this.requestBody.getBytes());
			return new ServletInputStream() {
				@Override
				public boolean isFinished() {
					return false;
				}

				@Override
				public boolean isReady() {
					return true;
				}

				@Override
				public void setReadListener(ReadListener listener) {
					throw new UnsupportedOperationException();
				}

				@Override
				public int read() throws IOException {
					return byteArrayInputStream.read();
				}
			};
		}

		@Override
		public BufferedReader getReader() throws IOException {
			return new BufferedReader(new InputStreamReader(this.getInputStream()));
		}

		@Override
		public Collection<Part> getParts() throws IOException, ServletException {
			Iterator<Part> iterator = super.getParts().iterator();
			List<Map<String, Long>> list = new ArrayList<>();
			while (iterator.hasNext()) {
				Map<String, Long> map = new HashMap<>();
				Part part = iterator.next();
				map.put(part.getSubmittedFileName(), part.getSize());
				list.add(map);
			}
			if (!list.isEmpty()) {
				this.multipartFileListBody = JSON.toJSONString(list);
			}
			return super.getParts();
		}
	}
}
