package com.example.uidemo.mark.Entity;

public class MarkDate {
    private String markdate;

    public MarkDate(String markdate) {
        this.markdate = markdate;
    }

    public MarkDate() {
    }

    public String getMarkdate() {
        return markdate;
    }

    public void setMarkdate(String markdate) {
        this.markdate = markdate;
    }

    @Override
    public String toString() {
        return "MarkDate{" +
                "markdate='" + markdate + '\'' +
                '}';
    }
}
