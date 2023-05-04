package io.github.sagarvns2003.buildgate.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import io.github.sagarvns2003.buildgate.service.Job;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@AllArgsConstructor
@Data
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class JobInformation {
	private JobStatistics statistics;
	private List<Job> jobInformation;
}