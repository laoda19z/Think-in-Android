package com.example.uidemo.familyactivity.beans;

import android.graphics.Bitmap;

public class ParentChildActivities {
    private int activityId;
    private Bitmap activityImg;
    private String activityName;
    private String activityTime;
    private int activityPeoNum;
    private String activityContent;
    private String activityDistrict;
    private String activityPay;

    public int getActivityPeoNum() {
        return activityPeoNum;
    }

    public void setActivityPeoNum(int activityPeoNum) {
        this.activityPeoNum = activityPeoNum;
    }

    public String getActivityContent() {
        return activityContent;
    }

    public void setActivityContent(String activityContent) {
        this.activityContent = activityContent;
    }

    public String getActivityPay() {
        return activityPay;
    }

    public void setActivityPay(String activityPay) {
        this.activityPay = activityPay;
    }

    public Bitmap getActivityImg() {
        return activityImg;
    }

    public void setActivityImg(Bitmap activityImg) {
        this.activityImg = activityImg;
    }

    public int getActivityId() {
        return activityId;
    }

    public void setActivityId(int activityId) {
        this.activityId = activityId;
    }

    public String getActivityName() {
        return activityName;
    }

    public void setActivityName(String activityName) {
        this.activityName = activityName;
    }

    public String getActivityTime() {
        return activityTime;
    }

    public void setActivityTime(String activityTime) {
        this.activityTime = activityTime;
    }

    public String getActivityDistrict() {
        return activityDistrict;
    }

    public void setActivityDistrict(String activityDistrict) {
        this.activityDistrict = activityDistrict;
    }
}
