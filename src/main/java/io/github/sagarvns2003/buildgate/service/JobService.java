package io.github.sagarvns2003.buildgate.service;

import java.time.Duration;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.BlockingQueue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import io.github.sagarvns2003.buildgate.model.JobInformation;
import io.github.sagarvns2003.buildgate.model.JobRequest;
import io.github.sagarvns2003.buildgate.model.JobStatistics;
import io.github.sagarvns2003.buildgate.model.JobStatus;
import io.github.sagarvns2003.buildgate.model.Stage;
import io.github.sagarvns2003.buildgate.model.StageStatus;
import io.github.sagarvns2003.buildgate.model.WorkFlow;
import io.github.sagarvns2003.buildgate.util.JobUtil;
import jakarta.annotation.PostConstruct;

@Service
public class JobService {

	private static final Logger logger = LoggerFactory.getLogger(JobService.class);

	@Autowired
	@Qualifier("jobQueue")
	private BlockingQueue<Runnable> jobQueue;

	@Autowired
	@Qualifier("runningJobQueue")
	private BlockingQueue<Runnable> runningJobQueue;

	@Autowired
	@Qualifier("jobExecutor")
	private JobExecutor jobExecutor;

	@Autowired
	private SimpMessagingTemplate template;

	@PostConstruct
	public void init() {
		int totalThreadsStarted = this.jobExecutor.prestartAllCoreThreads();
		logger.info("Total [{}] threads started to execute the jobs.",
				totalThreadsStarted /* this.jobExecutor.getPoolSize() */);
	}

	@Scheduled(initialDelay = 100, fixedRate = 2000)
	public void broadcastJobInformation() {
		JobInformation jobInformation = this.prepareJobInformation();
		template.convertAndSend("/topic/message", jobInformation);
	}

	public JobInformation getAllJobInformation() {
		return this.prepareJobInformation();
	}

	private JobInformation prepareJobInformation() {

		// Get waiting jobs
		List<Job> remainingQueuedJobs = this.jobQueue.parallelStream().map(this::toJob).toList();

		// This will contain Running, Done, Failed, Cancelled jobs etc...
		List<Job> otherStatusJobs = this.runningJobQueue.parallelStream().map(this::toJob).toList();
		logger.info("All running/active jobs: {}", this.jobExecutor.getQueue().size());

		// All status jobs
		List<Job> allStatusJobs = new ArrayList<>(otherStatusJobs);
		allStatusJobs.addAll(remainingQueuedJobs);

		// Prepare display order
		this.prepareDisplayOrder(allStatusJobs);

		// Preparing stats
		JobStatistics stats = new JobStatistics(0, 0, 0, 0, 0);
		if (!allStatusJobs.isEmpty()) {
			allStatusJobs.forEach(job -> {
				if (job.getStatus() == JobStatus.RUNNING) {
					stats.setRunningCount(stats.getRunningCount() + 1);
				} else if (job.getStatus() == JobStatus.QUEUED) {
					stats.setQueuedCount(stats.getQueuedCount() + 1);
				} else if (job.getStatus() == JobStatus.DONE) {
					stats.setDoneCount(stats.getDoneCount() + 1);
				} else if (job.getStatus() == JobStatus.CANCELLED) {
					stats.setCancelledCount(stats.getCancelledCount() + 1);
				} else if (job.getStatus() == JobStatus.FAILED) {
					stats.setFailedCount(stats.getFailedCount() + 1);
				}
			});
		}

		return JobInformation.builder().statistics(stats).jobInformation(allStatusJobs).build();
	}

	private void prepareDisplayOrder(List<Job> allStatusJobs) {
		Comparator<Job> statusOrderComparator = Comparator.comparing(Job::getStatus, (jobStatus1, jobStatus2) -> {
			return jobStatus1.getOrder() - jobStatus2.getOrder();
		});
		/*
		 * Comparator<Job> dateComparator = Comparator.comparing(Job::getSubmittedDate,
		 * (job1, job2) -> { return job1.compareTo(job2); });
		 */

		Collections.sort(allStatusJobs, statusOrderComparator/* .thenComparing(dateComparator) */);
	}

	public Job getJobInformation(String jobId) {
		Job jobInfo = this.jobQueue.parallelStream().map(this::toJob).filter(job -> job.getJobId().equals(jobId))
				.findFirst().orElse(null);
		if (null == jobInfo) {
			jobInfo = this.runningJobQueue.parallelStream().map(this::toJob).filter(job -> job.getJobId().equals(jobId))
					.findFirst().orElse(null);
		}
		return jobInfo;
	}

	public void deleteJob(JobRequest jr) {
		Job jobInfo = getJobInformation(jr.getJobId());
		if (jobInfo != null && jobInfo.getStatus() != JobStatus.RUNNING) {
			if (this.jobExecutor.remove(jobInfo)) {
				// Job removed from the queue
				logger.info("Job removed from the queue");
			} else {
				// Unable to remove from the queue
				logger.info("Unable to remove from the queue");
			}
		}
	}

	public void cancelJob(JobRequest jr) {
		Job jobInfo = getJobInformation(jr.getJobId());
		if (jobInfo != null) {
			if (this.jobExecutor.remove(jobInfo)) {
				jobInfo.setStatus(JobStatus.CANCELLED);
				this.runningJobQueue.offer(jobInfo);
				logger.info("Status changed to:{} for the job id:{} and name:{}", jobInfo.getStatus().name(),
						jobInfo.getJobId(), jobInfo.getJobName());
			} else {
				// Unable to remove from the queue
				logger.info("Unable to cancel the job having job id:{} and name:{} from the queue.", jobInfo.getJobId(),
						jobInfo.getJobName());
			}
		} else {
			logger.info("Unable to find the job having id:{}", jr.getJobId());
		}
	}

	public void enqueueJob(JobRequest jr, WorkFlow workFlow) {

		if (null == workFlow) {
			// Set default workflow
			Stage stage1 = Stage.builder().order(1).name("Checkout").command("git clone " + jr.getGitRepoUrl())
					.description("Cloning the git repository.").status(StageStatus.NOT_STARTED).build();
			Stage stage2 = Stage.builder().order(2).name("Build").command("mvn clean install")
					.description("Building the artifect.").status(StageStatus.NOT_STARTED).build();
			Stage stage3 = Stage.builder().order(3).name("Release").command("mvn releaser:release")
					.description("Releasing the artifect to the maven central repository.")
					.status(StageStatus.NOT_STARTED).build();
			workFlow = WorkFlow.builder().stages(List.of(stage1, stage2, stage3)).build();
		}

		Job job = Job.builder().runningJobQueue(this.runningJobQueue)
				.jobId(UUID.randomUUID().toString().replaceAll("-", "")).jobName(jr.getJobName())
				.gitRepoUrl(jr.getGitRepoUrl()).branch(jr.getBranch()).arguments(jr.getArguments()).workFlow(workFlow)
				.submitDate(ZonedDateTime.now()).startDate(null).endDate(null).runningDuration("-")
				.status(JobStatus.QUEUED).build();

		// if (!this.jobQueue.contains(job) && !this.runningJobQueue.contains(job)) {
		if (this.jobQueue.offer(job)) {
			logger.info("This job:{} having id:{} is queued successfully.", job.getJobName(), job.getJobId());
		} else {
			logger.warn("Unable to queue this job:{} having id:{}", job.getJobName(), job.getJobId());
		}

		// } else {
		// logger.warn("This job: {} is already in the queue.", job.getJobName());
		// }

	}

	private Job toJob(Runnable rJob) {
		Job job = (Job) rJob;

		Duration duration = null;
		if (job.getStatus() == JobStatus.RUNNING) {
			duration = Duration.between(job.getStartDate(), ZonedDateTime.now());
		} else if (job.getStatus() == JobStatus.DONE || job.getStatus() == JobStatus.CANCELLED
				|| job.getStatus() == JobStatus.FAILED) {
			if (null != job.getStartDate() && null != job.getEndDate()) {
				duration = Duration.between(job.getStartDate(), job.getEndDate());
			}
		}
		String runningDuration = JobUtil.prepareRunningDuration(duration);

		job = Job.builder().jobId(job.getJobId()).jobName(job.getJobName()).gitRepoUrl(job.getGitRepoUrl())
				.branch(job.getBranch()).arguments(job.getArguments()).workFlow(job.getWorkFlow())
				.submitDate(job.getSubmitDate()).startDate(job.getStartDate()).endDate(job.getEndDate())
				.runningDuration(runningDuration).status(job.getStatus()).build();

		return job;
	}
}