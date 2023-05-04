package io.github.sagarvns2003.buildgate.model;

public enum StageStatus {

	STARTED("Started"),

	NOT_STARTED("Not_Started"),

	DONE("Done"),

	CANCELLED("Cancelled"),

	FAILED("Failed");

	// Status string
	private String status;

	StageStatus(String status) {
		this.status = status;
	}

	public String getStatusString() {
		return this.status;
	}
}