package com.babygirl.attendance;

public class AttendanceObject {
    private String email;
    private String date;

    public AttendanceObject(String mail, String given_date) {
        email = mail;
        date = given_date;
    }

    public String getEmail(){
        return email;
    }

    public String getDate(){
        return date;
    }
}
