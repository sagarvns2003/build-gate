package io.github.sagarvns2003.buildgate.model;

import java.util.List;


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
public class WorkFlow {
	private WorkFlowType workFlowType;
	private List<Stage> stages;
}