package net.onest.entity;

public class ClockRecord {
	private int userId;
	private int sportTime;
	private String markDate;
	private String sportImpressions;
	private String background;
	private String sportType;
	
	@Override
	public String toString() {
		return "ClockRecord [userId=" + userId + ", sportTime=" + sportTime + ", markDate=" + markDate
				+ ", sportImpressions=" + sportImpressions + ", background=" + background + ", sportType=" + sportType
				+ "]";
	}
	
	public ClockRecord(int userId, int sportTime, String markDate, String sportImpressions, String background,
			String sportType) {
		super();
		this.userId = userId;
		this.sportTime = sportTime;
		this.markDate = markDate;
		this.sportImpressions = sportImpressions;
		this.background = background;
		this.sportType = sportType;
	}

	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}
	public int getSportTime() {
		return sportTime;
	}
	public void setSportTime(int sportTime) {
		this.sportTime = sportTime;
	}
	public String getMarkDate() {
		return markDate;
	}
	public void setMarkDate(String markDate) {
		this.markDate = markDate;
	}
	public String getSportImpressions() {
		return sportImpressions;
	}
	public void setSportImpressions(String sportImpressions) {
		this.sportImpressions = sportImpressions;
	}
	public String getBackground() {
		return background;
	}
	public void setBackground(String background) {
		this.background = background;
	}
	public String getSportType() {
		return sportType;
	}
	public void setSportType(String sportType) {
		this.sportType = sportType;
	}
	
	
}
