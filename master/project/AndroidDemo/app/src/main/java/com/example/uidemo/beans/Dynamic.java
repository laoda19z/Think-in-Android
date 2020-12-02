package com.example.uidemo.beans;

import java.util.Date;
import java.util.List;

/**
 * 动态的实体类
 */
public class Dynamic {
    private int dynamicId;//动态id
    private int userId;//用户id
    private Date time;//动态发布时间
    private String content;//动态内容
    private String location;//动态发布位置
    private String img;//图片路径
    private List<Comment> comment;//评论
    public int getDynamicId() {
        return dynamicId;
    }
    public void setDynamicId(int dynamicId) {
        this.dynamicId = dynamicId;
    }
    public int getUserId() {
        return userId;
    }
    public void setUserId(int userId) {
        this.userId = userId;
    }
    public Date getTime() {
        return time;
    }
    public void setTime(Date time) {
        this.time = time;
    }
    public String getContent() {
        return content;
    }
    public void setContent(String content) {
        this.content = content;
    }
    public String getLocation() {
        return location;
    }
    public void setLocation(String location) {
        this.location = location;
    }
    public String getImg() {
        return img;
    }
    public void setImg(String img) {
        this.img = img;
    }
    public List<Comment> getComment() {
        return comment;
    }
    public void setComment(List<Comment> comment) {
        this.comment = comment;
    }


}

//public class Dynamic {
//    private int dynamicId;//动态id
//    private String userName;//用户名
//    private String trendTime;//时间
//    private String trendContent;//内容
//    private List<Comment> trendComment;//评论
//    private String filePath;//图片路径
//    private String fileHead;//头像路径
//
//    public Dynamic() {
//    }
//
//    public Dynamic(int userHead, String userName, String trendTime, String trendContent, List<Comment> trendComment, String filePath, String fileHead) {
//        this.dynamicId = userHead;
//        this.userName = userName;
//        this.trendTime = trendTime;
//        this.trendContent = trendContent;
//        this.trendComment = trendComment;
//        this.filePath = filePath;
//        this.fileHead = fileHead;
//    }
//
//    public int getUserHead() {
//        return dynamicId;
//    }
//
//    public void setUserHead(int userHead) {
//        this.dynamicId = userHead;
//    }
//
//    public String getUserName() {
//        return userName;
//    }
//
//    public void setUserName(String userName) {
//        this.userName = userName;
//    }
//
//    public String getTrendTime() {
//        return trendTime;
//    }
//
//    public void setTrendTime(String trendTime) {
//        this.trendTime = trendTime;
//    }
//
//    public String getTrendContent() {
//        return trendContent;
//    }
//
//    public void setTrendContent(String trendContent) {
//        this.trendContent = trendContent;
//    }
//
//    public List<Comment> getTrendComment() {
//        return trendComment;
//    }
//
//    public void setTrendComment(List<Comment> trendComment) {
//        this.trendComment = trendComment;
//    }
//
//    public String getFilePath() {
//        return filePath;
//    }
//
//    public void setFilePath(String filePath) {
//        this.filePath = filePath;
//    }
//
//    public String getFileHead() {
//        return fileHead;
//    }
//
//    public void setFileHead(String fileHead) {
//        this.fileHead = fileHead;
//    }
//
//    @Override
//    public String toString() {
//        return "Dynamic{" +
//                "dynamicId=" + dynamicId +
//                ", userName='" + userName + '\'' +
//                ", trendTime='" + trendTime + '\'' +
//                ", trendContent='" + trendContent + '\'' +
//                ", trendComment=" + trendComment +
//                ", filePath='" + filePath + '\'' +
//                ", fileHead='" + fileHead + '\'' +
//                '}';
//    }
//}
