package com.example.uidemo.mark.Entity;

public class TotalMark {
    private int username;
    private String date;
    private int minutes;
    private String sporttype;
    private String impression;
    private String picname;
    private int child;

    public TotalMark() {
    }

    public TotalMark(int username, String date, int minutes, String sporttype, String impression, String picname, int child) {
        this.username = username;
        this.date = date;
        this.minutes = minutes;
        this.sporttype = sporttype;
        this.impression = impression;
        this.picname = picname;
        this.child = child;
    }

    public int getUsername() {
        return username;
    }

    public void setUsername(int username) {
        this.username = username;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getMinutes() {
        return minutes;
    }

    public void setMinutes(int minutes) {
        this.minutes = minutes;
    }

    public String getSporttype() {
        return sporttype;
    }

    public void setSporttype(String sporttype) {
        this.sporttype = sporttype;
    }

    public String getImpression() {
        return impression;
    }

    public void setImpression(String impression) {
        this.impression = impression;
    }

    public String getPicname() {
        return picname;
    }

    public void setPicname(String picname) {
        this.picname = picname;
    }

    public int getChild() {
        return child;
    }

    public void setChild(int child) {
        this.child = child;
    }

    @Override
    public String toString() {
        return "TotalMark{" +
                "username='" + username + '\'' +
                ", date='" + date + '\'' +
                ", minutes=" + minutes +
                ", sporttype='" + sporttype + '\'' +
                ", impression='" + impression + '\'' +
                ", picname='" + picname + '\'' +
                ", child=" + child +
                '}';
    }
}
