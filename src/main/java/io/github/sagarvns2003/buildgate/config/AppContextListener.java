package io.github.sagarvns2003.buildgate.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;

@Configuration
public class AppContextListener implements ServletContextListener {

	private static final Logger logger = LoggerFactory.getLogger(AppContextListener.class);

	@Autowired
	private AppConfig appConfig;

	@Override
	public void contextInitialized(ServletContextEvent sce) {
		logger.info("Server: {} ", sce.getServletContext().getServerInfo());
		logger.info("Context Path: {}", sce.getServletContext().getContextPath());
	}

	@Override
	public void contextDestroyed(ServletContextEvent sce) {
		// ServletContextListener.super.contextDestroyed(sce);
	}

}
