package com.example.uidemo.mark.Entity;

public class MarkPicEntity {
    private int username;
    private String date;
    private int child;

    public MarkPicEntity() {
    }

    public MarkPicEntity(int username, String date, int child) {
        this.username = username;
        this.date = date;
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

    public int getChild() {
        return child;
    }

    public void setChild(int child) {
        this.child = child;
    }

    @Override
    public String toString() {
        return "MarkPicEntity{" +
                "username='" + username + '\'' +
                ", date='" + date + '\'' +
                ", child=" + child +
                '}';
    }
}
