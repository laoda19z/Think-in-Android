package com.example.uidemo.beans;

public class Child {
    private int childId;        //孩子ID
    private int childAge;       //孩子年龄
    private int childGrade;     //孩子年级
    private String childName;   //孩子姓名
    private String childSex;    //孩子性别
    //构造方法（无参、有参）
    public Child() {

    }
    public Child(int childAge, int childGrade, String childName, String childSex) {
        this.childAge = childAge;
        this.childGrade = childGrade;
        this.childName = childName;
        this.childSex = childSex;
    }
    //Getter、Setter方法
    public int getChildId() {
        return childId;
    }
    public void setChildId(int childId) {
        this.childId = childId;
    }
    public int getChildAge() {
        return childAge;
    }
    public void setChildAge(int childAge) {
        this.childAge = childAge;
    }
    public int getChildGrade() {
        return childGrade;
    }
    public void setChildGrade(int childGrade) {
        this.childGrade = childGrade;
    }
    public String getChildName() {
        return childName;
    }
    public void setChildName(String childName) {
        this.childName = childName;
    }
    public String getChildSex() {
        return childSex;
    }
    public void setChildSex(String childSex) {
        this.childSex = childSex;
    }

}
