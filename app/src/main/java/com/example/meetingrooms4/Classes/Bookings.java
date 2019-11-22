package com.example.meetingrooms4.Classes;

public class Bookings {

    private String user;
    private String room;
    private String startTime;
    private String endTIme;
    private String date;
    private String desc;
    private String status;

    public Bookings(String user, String room, String startTime, String endTIme, String date, String desc,String status) {
        this.user = user;
        this.room = room;
        this.startTime = startTime;
        this.endTIme = endTIme;
        this.date = date;
        this.desc = desc;
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getRoom() {
        return room;
    }

    public void setRoom(String room) {
        this.room = room;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTIme() {
        return endTIme;
    }

    public void setEndTIme(String endTIme) {
        this.endTIme = endTIme;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Bookings() {

    }
}
