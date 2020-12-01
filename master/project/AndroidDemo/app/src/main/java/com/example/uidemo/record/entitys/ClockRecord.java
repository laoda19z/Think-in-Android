package com.example.uidemo.record.entitys;

public class ClockRecord {
	private int backgroundPicNum;
	private int childId;
	private int userId;
	private float sportTime;
	private String markDate;
	private String sportImpressions;
	private String qrCode;
	private String sportType;
	private String uploadPic;
	private boolean isBackground;
	
	@Override
	public String toString() {
		return "ClockRecord [backgroundPicNum=" + backgroundPicNum + ", childId=" + childId + ", userId=" + userId
				+ ", sportTime=" + sportTime + ", markDate=" + markDate + ", sportImpressions=" + sportImpressions
				+ ", qrCode=" + qrCode + ", sportType=" + sportType + ", uploadPic=" + uploadPic + ", isBackground="
				+ isBackground + "]";
	}
	
	public ClockRecord() {
		super();
	}

	public int getBackgroundPicNum() {
		return backgroundPicNum;
	}
	public void setBackgroundPicNum(int backgroundPicNum) {
		this.backgroundPicNum = backgroundPicNum;
	}
	public int getChildId() {
		return childId;
	}
	public void setChildId(int childId) {
		this.childId = childId;
	}
	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}
	public float getSportTime() {
		return sportTime;
	}
	public void setSportTime(float sportTime) {
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
	public String getQrCode() {
		return qrCode;
	}
	public void setQrCode(String qrCode) {
		this.qrCode = qrCode;
	}
	public String getSportType() {
		return sportType;
	}
	public void setSportType(String sportType) {
		this.sportType = sportType;
	}
	public String getUploadPic() {
		return uploadPic;
	}
	public void setUploadPic(String uploadPic) {
		this.uploadPic = uploadPic;
	}
	public boolean isBackground() {
		return isBackground;
	}
	public void setBackground(boolean isBackground) {
		this.isBackground = isBackground;
	}

	public ClockRecord(int backgroundPicNum, int childId, int userId, float sportTime, String markDate,
                       String sportImpressions, String qrCode, String sportType, boolean isBackground) {
		super();
		this.backgroundPicNum = backgroundPicNum;
		this.childId = childId;
		this.userId = userId;
		this.sportTime = sportTime;
		this.markDate = markDate;
		this.sportImpressions = sportImpressions;
		this.qrCode = qrCode;
		this.sportType = sportType;
		this.isBackground = isBackground;
	}

	public ClockRecord(int childId, int userId, float sportTime, String markDate, String sportImpressions,
                       String qrCode, String sportType, String uploadPic, boolean isBackground) {
		super();
		this.childId = childId;
		this.userId = userId;
		this.sportTime = sportTime;
		this.markDate = markDate;
		this.sportImpressions = sportImpressions;
		this.qrCode = qrCode;
		this.sportType = sportType;
		this.uploadPic = uploadPic;
		this.isBackground = isBackground;
	}
	
}
