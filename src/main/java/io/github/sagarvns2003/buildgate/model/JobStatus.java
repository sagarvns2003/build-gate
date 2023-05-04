package io.github.sagarvns2003.buildgate.model;

public enum JobStatus {

	RUNNING(1, "Running"),

	QUEUED(2, "Queued"),

	DONE(3, "Done"),

	CANCELLED(4, "Cancelled"),

	FAILED(5, "Failed");

	// Display order
	private int order;

	// Status string
	private String status;

	JobStatus(int order, String status) {
		this.order = order;
		this.status = status;
	}

	public int getOrder() {
		return this.order;
	}

	public String getStatusString() {
		return this.status;
	}
}