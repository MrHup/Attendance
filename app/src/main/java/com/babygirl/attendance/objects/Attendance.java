package com.babygirl.attendance.objects;

public class Attendance {
    private String date;
    private String user_mail;

    public  Attendance() {}

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getUser_mail() {
        return user_mail;
    }

    public void setUser_mail(String user_mail) {
        this.user_mail = user_mail;
    }

    public Attendance(String date, String user_mail){
        this.date = date;
        this.user_mail = user_mail;
    }
}
