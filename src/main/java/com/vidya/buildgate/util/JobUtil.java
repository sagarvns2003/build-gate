package com.vidya.buildgate.util;

import java.time.Duration;

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

}
