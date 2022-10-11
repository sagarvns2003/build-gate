package com.vidya.buildgate.manager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.vidya.buildgate.model.JobInformation;
import com.vidya.buildgate.model.JobRequest;
import com.vidya.buildgate.service.Job;
import com.vidya.buildgate.service.JobService;

@Component
public class JobManager {

	@Autowired
	private JobService jobService;

	public void enqueueJob(JobRequest jr) {
		this.jobService.enqueueJob(jr, null);
	}

	public JobInformation getAllJobInformation() {
		return this.jobService.getAllJobInformation();
	}

	public Job getJobInformation(String jobId) {
		return this.jobService.getJobInformation(jobId);
	}

}