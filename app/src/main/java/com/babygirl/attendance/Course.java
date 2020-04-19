package com.babygirl.attendance;

import android.content.Intent;

public class Course {
    private String DQRC;
    private String name;
    private String desc;
    private int target_year;

    public Course(String DQRC, String name, int target_year, String desc)
    {
        this.DQRC = DQRC;
        this.name = name;
        this.target_year = target_year;
        this.desc = desc;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getDQRC() {
        return DQRC;
    }

    public void setDQRC(String DQRC) {
        this.DQRC = DQRC;
    }


    public int getTarget_year() {
        return target_year;
    }

    public void setTarget_year(int target_year) {
        this.target_year = target_year;
    }



}
