package net.onest.entity;

/**
 * 测评报告类
 * @author m1380
 *
 */
public class Report {
	private int reportId;  //测评报告ID
	private int childId;             //孩子ID
	private int bodyScore;           //身形得分
	private int upScore;             //上肢得分
	private int downScore;           //下肢得分
	private int overallScore;        //综合得分
	public Report() {
		
	}
	public Report(int reportId, int childId, int bodyScore, int upScore, int downScore,
			int overallScore) {
		super();
		this.reportId = reportId;
		this.childId = childId;
		this.bodyScore = bodyScore;
		this.upScore = upScore;
		this.downScore = downScore;
		this.overallScore = overallScore;
	}
	public int getReportId() {
		return reportId;
	}
	public void setReportId(int reportId) {
		this.reportId = reportId;
	}
	public int getChildId() {
		return childId;
	}
	public void setChildId(int childId) {
		this.childId = childId;
	}
	public int getBodyScore() {
		return bodyScore;
	}
	public void setBodyScore(int bodyScore) {
		this.bodyScore = bodyScore;
	}
	public int getUpScore() {
		return upScore;
	}
	public void setUpScore(int upScore) {
		this.upScore = upScore;
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
	@Override
	public String toString() {
		return "Report [reportId=" + reportId + ", childId=" + childId + ", bodyScore=" + bodyScore + ", upScore="
				+ upScore + ", downScore=" + downScore + ", overallScore=" + overallScore + "]";
	}
	
}
