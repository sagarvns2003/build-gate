package com.vidya.buildgate.conf;

import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;

public class AppContextListener implements ServletContextListener {

	@Override
	public void contextDestroyed(ServletContextEvent sce) {
		// ServletContextListener.super.contextDestroyed(sce);
		
	}

}
