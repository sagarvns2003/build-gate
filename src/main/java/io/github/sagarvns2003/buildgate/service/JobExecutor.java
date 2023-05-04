package io.github.sagarvns2003.buildgate.service;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.github.sagarvns2003.buildgate.model.JobStatus;

public class JobExecutor extends ThreadPoolExecutor {

	private static final Logger logger = LoggerFactory.getLogger(JobExecutor.class);

	public JobExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit,
			BlockingQueue<Runnable> workQueue) {
		super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue);
	}

	@Override
	protected void beforeExecute(Thread t, Runnable r) {
		super.beforeExecute(t, r);
		Job job = (Job) r;
		logger.info("Perform beforeExecute() logic for {}, current Status: {}", job.getJobName(), job.getStatus());
		// job.setStatus("Running");
		// logger.info("Perform beforeExecute() logic for {}, new Status: {}",
		// job.getJobName(), job.getStatus());
	}

	@Override
	protected void afterExecute(Runnable r, Throwable t) {
		super.afterExecute(r, t);
		Job job = (Job) r;
		if (t != null) {
			job.setStatus(JobStatus.FAILED);
			logger.info("Perform exception handler logic");
		}
		job.setStatus(JobStatus.DONE);
		logger.info("Perform afterExecute() logic for {}, new Status: {}", job.getJobName(), job.getStatus());
	}
}