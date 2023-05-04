package io.github.sagarvns2003.buildgate.config;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.github.sagarvns2003.buildgate.service.JobExecutor;

@Configuration
public class JobExecuterConfig {

	@Bean(name = "jobQueue")
	BlockingQueue<Runnable> jobQueue() {
		BlockingQueue<Runnable> jobQueue = new LinkedBlockingQueue<Runnable>();
		return jobQueue;
	}

	@Bean(name = "runningJobQueue")
	BlockingQueue<Runnable> runningJobQueue() {
		BlockingQueue<Runnable> runningJobQueue = new LinkedBlockingQueue<Runnable>();
		return runningJobQueue;
	}
	
	@Bean(name = "jobExecutor", destroyMethod = "shutdown")
	JobExecutor jobExecutor(@Autowired BlockingQueue<Runnable> jobQueue) {
		return new JobExecutor(10, 20, 5, TimeUnit.SECONDS, jobQueue);
	}

}