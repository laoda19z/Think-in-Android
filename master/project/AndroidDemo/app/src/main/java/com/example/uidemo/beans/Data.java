package com.example.uidemo.beans;

public class Data {
    private int imgid;
    private String text;

    public int getImgid() {
        return imgid;
    }

    public void setImgid(int imgid) {
        this.imgid = imgid;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return "Data{" +
                "imgid=" + imgid +
                ", text='" + text + '\'' +
                '}';
    }

    public Data(int imgid, String text) {
        this.imgid = imgid;
        this.text = text;
    }

    public Data() {
    }
}
