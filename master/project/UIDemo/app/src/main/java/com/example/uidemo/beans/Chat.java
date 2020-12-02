package com.example.uidemo.beans;

public class Chat {
    public static final int TYPE_RECEIVED = 0;//接收的数据
    public static final int TYPE_SENT = 1;//发送的数据
    //    对话文本
    private String text;
    //    标示
    private int type;
    private String time;

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }



    public Chat(String text, int type, String time) {
        this.text = text;
        this.type = type;
        this.time=time;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
