package com.babygirl.attendance;

import android.content.Intent;

import java.io.Serializable;

public class Course implements Serializable {
    private String DQRC;
    private String course_name;
    private String instructor_name;
    //private String desc;
    private String target_year;

    public String getDQRC() {
        return DQRC;
    }

    public void setDQRC(String DQRC) {
        this.DQRC = DQRC;
    }

    public String getCourse_name() {
        return course_name;
    }

    public void setCourse_name(String course_name) {
        this.course_name = course_name;
    }

    public String getInstructor_name() {
        return instructor_name;
    }

    public void setInstructor_name(String instructor_name) {
        this.instructor_name = instructor_name;
    }

    public String getTarget_year() {
        return target_year;
    }

    public void setTarget_year(String target_year) {
        this.target_year = target_year;
    }

    @Override
    public String toString() {
        return "Course{" +
                "DQRC='" + DQRC + '\'' +
                ", course_name='" + course_name + '\'' +
                ", instructor_name='" + instructor_name + '\'' +
                ", target_year='" + target_year + '\'' +
                '}';
    }

    public Course(String DQRC, String course_name, String instructor_name, String target_year)
    {
        this.DQRC = DQRC;
        this.course_name = course_name;
        this.instructor_name = instructor_name;
        this.target_year = target_year;
    }





}
