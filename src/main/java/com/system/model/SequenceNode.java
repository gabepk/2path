package com.system.model;

public class SequenceNode {
	private String submissionID;
	private String organismName;
	private String submitedSequenceName;

	private String uniprotIdForSubmitedSequence;
	private String uniprotAnnotationForSubmitedSequence;
	private String submitedSequence;

	private String ec;

	public String getSubmissionID() {
		return submissionID;
	}

	public String getOrganismName() {
		return organismName;
	}

	public void setOrganismName(String organismName) {
		this.organismName = organismName;
	}

	public void setSubmissionID(String submissionID) {
		this.submissionID = submissionID;
	}

	public String getSubmitedSequenceName() {
		return submitedSequenceName;
	}

	public void setSubmitedSequenceName(String submitedSequenceName) {
		this.submitedSequenceName = submitedSequenceName;
	}

	public String getUniprotIdForSubmitedSequence() {
		return uniprotIdForSubmitedSequence;
	}

	public void setUniprotIdForSubmitedSequence(
			String uniprotIdForSubmitedSequence) {
		this.uniprotIdForSubmitedSequence = uniprotIdForSubmitedSequence;
	}

	public String getUniprotAnnotationForSubmitedSequence() {
		return uniprotAnnotationForSubmitedSequence;
	}

	public void setUniprotAnnotationForSubmitedSequence(
			String uniprotAnnotationForSubmitedSequence) {
		this.uniprotAnnotationForSubmitedSequence = uniprotAnnotationForSubmitedSequence;
	}

	public String getSubmitedSequence() {
		return submitedSequence;
	}

	public void setSubmitedSequence(String submitedSequence) {
		this.submitedSequence = submitedSequence;
	}

	public String getEc() {
		return ec;
	}

	public void setEc(String ec) {
		this.ec = ec;
	}

}
