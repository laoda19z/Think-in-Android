package com.example.uidemo.beans;

import java.util.List;

public class CourseInfo {
    private List<Course> courses;

    public List<Course> getCourses() {
        return courses;
    }

    public void setCourses(List<Course> courses) {
        this.courses = courses;
    }

    public CourseInfo() {}

    @Override
    public String toString() {
        return "CourseInfo{" +
                "courses=" + courses +
                '}';
    }
}
