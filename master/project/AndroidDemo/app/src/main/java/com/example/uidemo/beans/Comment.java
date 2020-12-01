package com.example.uidemo.beans;
public class Comment {
    private int commentId;//评论id
    private int dynamicId;//动态id
    private int publisherId;//评论发布者id
    private int receiverId;//评论接收者id
    private String content;//评论内容

    public int getCommentId() {
        return commentId;
    }
    public void setCommentId(int commentId) {
        this.commentId = commentId;
    }
    public int getDynamicId() {
        return dynamicId;
    }
    public void setDynamicId(int dynamicId) {
        this.dynamicId = dynamicId;
    }
    public int getPublisherId() {
        return publisherId;
    }
    public void setPublisherId(int publisherId) {
        this.publisherId = publisherId;
    }
    public int getReceiverId() {
        return receiverId;
    }
    public void setReceiverId(int receiverId) {
        this.receiverId = receiverId;
    }
    public String getContent() {
        return content;
    }
    public void setContent(String content) {
        this.content = content;
    }



}

//public class Comment {
//    private User mCommentator;//评论者
//    private String mContent;//评论内容
//    private User mReceiver;//评论接收者
//
//    public Comment(User mCommentator, String mContent, User mReceiver) {
//        this.mCommentator = mCommentator;
//        this.mContent = mContent;
//        this.mReceiver = mReceiver;
//    }
//
//    public User getmCommentator() {
//        return mCommentator;
//    }
//
//    public void setmCommentator(User mCommentator) {
//        this.mCommentator = mCommentator;
//    }
//
//    public String getmContent() {
//        return mContent;
//    }
//
//    public void setmContent(String mContent) {
//        this.mContent = mContent;
//    }
//
//    public User getmReceiver() {
//        return mReceiver;
//    }
//
//    public void setmReceiver(User mReceiver) {
//        this.mReceiver = mReceiver;
//    }
//}
