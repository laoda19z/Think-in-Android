package com.example.uidemo.beans;

public class Course {
    private int courseId;
    private String courseName;
    private String courseContent;
    private String courseTime;
    private int coursePeoNum;
    private String  courseType;

    public Course() {}

    public int getCourseId() {
        return courseId;
    }

    public void setCourseId(int courseId) {
        this.courseId = courseId;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getCourseContent() {
        return courseContent;
    }

    public void setCourseContent(String courseContent) {
        this.courseContent = courseContent;
    }

    public String getCourseTime() {
        return courseTime;
    }

    public void setCourseTime(String courseTime) {
        this.courseTime = courseTime;
    }

    public int getCoursePeoNum() {
        return coursePeoNum;
    }

    public void setCoursePeoNum(int coursePeoNum) {
        this.coursePeoNum = coursePeoNum;
    }

    public String getCourseType() {
        return courseType;
    }

    public void setCourseType(String courseType) {
        this.courseType = courseType;
    }

    @Override
    public String toString() {
        return "Course{" +
                "courseId=" + courseId +
                ", courseName=" + courseName +
                ", courseContent=" + courseContent +
                ", courseTime=" + courseTime +
                ", coursePeoNum=" + coursePeoNum +
                ", courseType=" + courseType +
                '}';
    }
}
