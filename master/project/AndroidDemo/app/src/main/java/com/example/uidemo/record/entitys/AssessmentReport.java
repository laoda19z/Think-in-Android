package com.example.uidemo.record.entitys;
public class AssessmentReport {
	private int bodyScore;
	private int childId;
	private int downScore;
	private int overallScore;
	private int upScore;
	private int assessmentReportId;
	private String time;
	@Override
	public String toString() {
		return "AssessmentReport [bodyScore=" + bodyScore + ", childId=" + childId + ", downScore=" + downScore
				+ ", overallScore=" + overallScore + ", upScore=" + upScore + ", assessmentReportId="
				+ assessmentReportId + "]";
	}
	
	public AssessmentReport() {
		super();
	}

	
	

	public AssessmentReport(int bodyScore, int childId, int downScore, int overallScore, int upScore,
			int assessmentReportId, String time) {
		super();
		this.bodyScore = bodyScore;
		this.childId = childId;
		this.downScore = downScore;
		this.overallScore = overallScore;
		this.upScore = upScore;
		this.assessmentReportId = assessmentReportId;
		this.time = time;
	}

	public int getBodyScore() {
		return bodyScore;
	}
	public void setBodyScore(int bodyScore) {
		this.bodyScore = bodyScore;
	}
	public int getChildId() {
		return childId;
	}
	public void setChildId(int childId) {
		this.childId = childId;
	}
	public int getDownScore() {
		return downScore;
	}
	public void setDownScore(int downScore) {
		this.downScore = downScore;
	}
	public int getOverallScore() {
		return overallScore;
	}
	public void setOverallScore(int overallScore) {
		this.overallScore = overallScore;
	}
	public int getUpScore() {
		return upScore;
	}
	public void setUpScore(int upScore) {
		this.upScore = upScore;
	}
	public int getAssessmentReportId() {
		return assessmentReportId;
	}
	public void setAssessmentReportId(int assessmentReportId) {
		this.assessmentReportId = assessmentReportId;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}
	
}
