package com.example.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;

/**
 * dubbo 服务端配置
 */
@Configuration
@ImportResource(value = "classpath:")
public class ProviderConfig {

}
