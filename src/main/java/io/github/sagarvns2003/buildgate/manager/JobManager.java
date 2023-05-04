package io.github.sagarvns2003.buildgate.manager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import io.github.sagarvns2003.buildgate.model.JobInformation;
import io.github.sagarvns2003.buildgate.model.JobRequest;
import io.github.sagarvns2003.buildgate.service.Job;
import io.github.sagarvns2003.buildgate.service.JobService;

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