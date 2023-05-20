package io.github.sagarvns2003.buildgate.model;

import java.time.ZonedDateTime;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

@AllArgsConstructor
@Data
@Builder
@EqualsAndHashCode
@JsonIgnoreProperties(ignoreUnknown = true)
public class Stage {
	private int order; // index
	private String name; // stage name
	private String command;
	private String description;
	private ZonedDateTime startDate;
	private ZonedDateTime endDate;
	private String runningDuration;
	private String logPath;
	private StageStatus status;
}