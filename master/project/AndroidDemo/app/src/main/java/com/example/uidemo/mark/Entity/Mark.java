package com.example.uidemo.mark.Entity;

public class Mark {
    private int username;
    private String date;
    private int minutes;
    private String sporttype;
    private String impression;
    private int child;

    public Mark() {
    }

    public Mark(int username, String date, int minutes, String sporttype, String impression, int child) {
        this.username = username;
        this.date = date;
        this.minutes = minutes;
        this.sporttype = sporttype;
        this.impression = impression;
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

    public int getChild() {
        return child;
    }

    public void setChild(int child) {
        this.child = child;
    }

    @Override
    public String toString() {
        return "Mark{" +
                "username='" + username + '\'' +
                ", date='" + date + '\'' +
                ", minutes=" + minutes +
                ", sporttype='" + sporttype + '\'' +
                ", impression='" + impression + '\'' +
                ", child=" + child +
                '}';
    }
}
