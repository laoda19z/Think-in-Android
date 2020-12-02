package com.example.uidemo.mark.Entity;

public class NeedSearchDate {
    private String username;
    private String year;
    private String month;
    private int child;

    public NeedSearchDate() {
    }

    public NeedSearchDate(String username, String year, String month, int child) {
        this.username = username;
        this.year = year;
        this.month = month;
        this.child = child;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public int getChild() {
        return child;
    }

    public void setChild(int child) {
        this.child = child;
    }

    @Override
    public String toString() {
        return "NeedSearchDate{" +
                "username='" + username + '\'' +
                ", year='" + year + '\'' +
                ", month='" + month + '\'' +
                ", child=" + child +
                '}';
    }
}
