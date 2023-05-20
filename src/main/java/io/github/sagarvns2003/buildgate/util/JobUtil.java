package io.github.sagarvns2003.buildgate.util;

import java.time.Duration;
import java.util.Objects;
import java.util.concurrent.BlockingQueue;

import io.github.sagarvns2003.buildgate.model.JobStatus;
import io.github.sagarvns2003.buildgate.service.Job;

public final class JobUtil {

	private JobUtil() {
	}

	public static String prepareRunningDuration(Duration duration) {
		String runningDuration = "-";
		if (duration != null) {
			int hr = duration.toHoursPart();
			int min = duration.toMinutesPart();
			int sec = duration.toSecondsPart();
			if (hr == 0) {
				runningDuration = String.format("%s min, %s sec", min, sec);
			} else {
				runningDuration = String.format("%s:%s:%s", hr, min, sec);
			}
		}
		return runningDuration;
	}

	public static void changeJobStatus(Job job, final JobStatus status) {
		if (Objects.nonNull(job)) {
			job.setStatus(status);
		}
	}

	public static  void addJobToProcessedQueue(BlockingQueue<Runnable> processedJobQueue, Job job) {
		if (Objects.nonNull(job)) {
			processedJobQueue.add(job);
		}
	}
}