package com.example.uidemo.mark.Entity;

public class ImageEntity {
    private String imagename;
    private String json;

    public ImageEntity() {
    }

    public ImageEntity(String imagename, String json) {
        this.imagename = imagename;
        this.json = json;
    }

    public String getImagename() {
        return imagename;
    }

    public void setImagename(String imagename) {
        this.imagename = imagename;
    }

    public String getJson() {
        return json;
    }

    public void setJson(String json) {
        this.json = json;
    }

    @Override
    public String toString() {
        return "ImageEntity{" +
                "imagename='" + imagename + '\'' +
                ", json='" + json + '\'' +
                '}';
    }
}
