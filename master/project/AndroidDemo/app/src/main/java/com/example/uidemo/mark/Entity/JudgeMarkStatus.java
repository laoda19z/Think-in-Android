package com.example.uidemo.mark.Entity;

public class JudgeMarkStatus {
    private int username;
    private String year;
    private String month;
    private String day;
    private int child;

    public JudgeMarkStatus() {
    }

    public JudgeMarkStatus(int username, String year, String month, String day, int child) {
        this.username = username;
        this.year = year;
        this.month = month;
        this.day = day;
        this.child = child;
    }

    public int getUsername() {
        return username;
    }

    public void setUsername(int username) {
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

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public int getChild() {
        return child;
    }

    public void setChild(int child) {
        this.child = child;
    }
}
