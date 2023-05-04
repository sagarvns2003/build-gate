package com.vidya.buildgate;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Bean;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.vidya.buildgate.config.AppConfig;

@SpringBootApplication
@EnableScheduling
public class BuildgateApplication {

	private static final Logger logger = LoggerFactory.getLogger(BuildgateApplication.class);

	@Autowired
	private AppConfig appConfig;

	public static void main(String[] args) {
		SpringApplication.run(BuildgateApplication.class, args);
	}

	@EventListener(ApplicationReadyEvent.class)
	public void afterStartup() {
		logger.info("App {}-{} using JVM: {}", this.appConfig.appName(), this.appConfig.appVersion(),
				this.appConfig.appJvmVersion());
		logger.info("Swagger url: {}", this.appConfig.swaggerUrl());
		logger.info("Server started at port: {} with url: {}", this.appConfig.port(), this.appConfig.appUrl());
	}

	@Bean
	WebMvcConfigurer corsConfigurer() {
		return new WebMvcConfigurer() {
			@Override
			public void addCorsMappings(CorsRegistry registry) {
				registry.addMapping("/**").allowCredentials(true).allowedOrigins("http://localhost:3000"); // allowedOriginPatterns("*").allowedMethods("*");
				// .allowedOrigins("http://localhost:3000");
			}
		};
	}
}