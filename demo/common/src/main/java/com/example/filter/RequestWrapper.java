package com.example.filter;

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

import com.example.json.JSON;

public class RequestWrapper extends HttpServletRequestWrapper {

	private String requestBody = null;

	private String multipartFileListBody = null;

	private static final int BUFF_SIZE = 4096;

	private static final int CAPACITY = 1024;

	private final Logger log = LoggerFactory.getLogger(this.getClass());

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