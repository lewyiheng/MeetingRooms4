package com.example.meetingrooms4.Classes;

import java.sql.Timestamp;
import java.util.Date;

public class Bookings {

    private String user_id;
    private String room_id;
    private String start_time;
    private String end_time;
    private String book_date;
    private String book_purpose;
    private String bks_id;

    public Bookings() {

    }

    public Bookings(String user_id, String room_id, String start_time, String end_time, String book_date, String book_purpose, String bks_id) {
        this.user_id = user_id;
        this.room_id = room_id;
        this.start_time = start_time;
        this.end_time = end_time;
        this.book_date = book_date;
        this.book_purpose = book_purpose;
        this.bks_id = bks_id;
    }



    public String getRoom_id() {
        return room_id;
    }

    public void setRoom_id(String room_id) {
        this.room_id = room_id;
    }

    public String getStart_time() {
        return start_time;
    }

    public void setStart_time(String start_time) {
        this.start_time = start_time;
    }

    public String getEnd_time() {
        return end_time;
    }

    public void setEnd_time(String end_time) {
        this.end_time = end_time;
    }

    public String getBook_date() {
        return book_date;
    }

    public void setBook_date(String book_date) {
        this.book_date = book_date;
    }

    public String getBook_purpose() {
        return book_purpose;
    }

    public void setBook_purpose(String book_purpose) {
        this.book_purpose = book_purpose;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getBks_id() {
        return bks_id;
    }

    public void setBks_id(String bks_id) {
        this.bks_id = bks_id;
    }
}