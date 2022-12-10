package com.vidya.buildgate.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {

	@Autowired
	private ServerProperties serverProperties;

	public int port() {
		return this.serverProperties.getPort();
	}

	public String appUrl() {
		return "http://localhost:" + this.port();
	}

	public String swaggerUrl() {
		return "http://localhost:" + this.port() + "/swagger-ui/index.html";
	}
}
