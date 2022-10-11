package com.vidya.buildgate.service;

import java.time.Duration;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.concurrent.BlockingQueue;

import org.apache.commons.lang3.SystemUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.vidya.buildgate.model.JobStatus;
import com.vidya.buildgate.model.Stage;
import com.vidya.buildgate.model.StageStatus;
import com.vidya.buildgate.model.WorkFlow;
import com.vidya.buildgate.util.JobUtil;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

//@Component
@AllArgsConstructor
@Getter
@Setter
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Job implements Runnable {

	private static final Logger logger = LoggerFactory.getLogger(Job.class);

	private final String defaultBuildFile = SystemUtils.IS_OS_UNIX ? "build.sh"
			: SystemUtils.IS_OS_WINDOWS ? "build.bat" : "";

	@JsonIgnore
	private BlockingQueue<Runnable> runningJobQueue;

	// @EqualsAndHashCode.Include
	private String jobId;

	@EqualsAndHashCode.Include
	private String jobName;

	@EqualsAndHashCode.Include
	private String gitRepoUrl;

	@EqualsAndHashCode.Include
	private String branch;

	private String arguments;

	// @JsonIgnore
	private WorkFlow workFlow;

	private ZonedDateTime submitDate;
	private ZonedDateTime startDate;
	private ZonedDateTime endDate;
	private String runningDuration;
	private JobStatus status;

	@Override
	public void run() {
		this.startDate = ZonedDateTime.now();
		this.changeStatus(JobStatus.RUNNING);
		try {
			// this.setStatus("Running");
			logger.info("Executing: {}, status: {}", jobName, status);

			List<Stage> stages = workFlow.getStages();

			// Sorting base on stage order
			// Collections.sort(stages, Comparator.comparingInt(Stage::getOrder));

			for (Stage stage : stages) {
				stage.setStatus(StageStatus.STARTED);
				ZonedDateTime startDateTime = ZonedDateTime.now();
				stage.setStartDate(startDateTime);
				stage.setLogPath("/tmp/"+this.jobName+"/"+this.jobId+"/stage_"+stage.getName());

				Thread.sleep(15000);

				stage.setEndDate(ZonedDateTime.now());
				stage.setStatus(StageStatus.DONE);

				// Prepare running duration
				Duration duration = Duration.between(stage.getStartDate(), stage.getEndDate());
				String runningDuration = JobUtil.prepareRunningDuration(duration);
				stage.setRunningDuration(runningDuration);
			}

			// logger.info("Execution: {} done.", jobName);
			// this.status = "Done";

		} catch (InterruptedException e) {
			this.changeStatus(JobStatus.FAILED);
			e.printStackTrace();
		} finally {
			this.endDate = ZonedDateTime.now();
		}
	}

	private void changeStatus(JobStatus jobStatus) {
		this.status = jobStatus;

		logger.info("Job status changed to: {}", this.status.name());

		if (!this.runningJobQueue.contains(this)) {
			logger.info("Job: {} added to running queue.");
			this.runningJobQueue.add(this);
		}
	}

}