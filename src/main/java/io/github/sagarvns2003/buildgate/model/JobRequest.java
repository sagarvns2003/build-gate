package io.github.sagarvns2003.buildgate.model;

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
public class JobRequest {

	private String jobId;
	private String jobName;
	private String gitRepoUrl;
	private String branch;
	private String arguments;
	
}