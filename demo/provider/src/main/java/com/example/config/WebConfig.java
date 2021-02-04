package com.example.config;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.GsonHttpMessageConverter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.example.json.JSON;

/**
 * web配置
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

	@Override
	public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
		GsonHttpMessageConverter converter = new GsonHttpMessageConverter();
		converter.setGson(JSON.gson);
		converter.setDefaultCharset(StandardCharsets.UTF_8);
		List<MediaType> mediaTypesList = new ArrayList<>();
		mediaTypesList.add(MediaType.APPLICATION_JSON);
		mediaTypesList.add(MediaType.APPLICATION_ATOM_XML);
		mediaTypesList.add(MediaType.APPLICATION_FORM_URLENCODED);
		mediaTypesList.add(MediaType.APPLICATION_OCTET_STREAM);
		mediaTypesList.add(MediaType.APPLICATION_PDF);
		mediaTypesList.add(MediaType.APPLICATION_RSS_XML);
		mediaTypesList.add(MediaType.APPLICATION_XHTML_XML);
		mediaTypesList.add(MediaType.APPLICATION_XML);
		mediaTypesList.add(MediaType.IMAGE_GIF);
		mediaTypesList.add(MediaType.IMAGE_JPEG);
		mediaTypesList.add(MediaType.IMAGE_PNG);
		mediaTypesList.add(MediaType.TEXT_EVENT_STREAM);
		mediaTypesList.add(MediaType.TEXT_HTML);
		mediaTypesList.add(MediaType.TEXT_MARKDOWN);
		mediaTypesList.add(MediaType.TEXT_PLAIN);
		mediaTypesList.add(MediaType.TEXT_XML);
		converter.setSupportedMediaTypes(mediaTypesList);
		converters.add(0, converter);
	}

	@Override
	public void addCorsMappings(CorsRegistry registry) {
		registry.addMapping("/**");
	}
}
