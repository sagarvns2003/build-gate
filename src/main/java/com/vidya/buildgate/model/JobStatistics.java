package com.vidya.buildgate.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
@Builder
public class JobStatistics {

	Integer runningCount;

	Integer queuedCount;

	Integer doneCount;

	Integer cancelledCount;

	Integer failedCount;
}
