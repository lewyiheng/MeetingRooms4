package com.example.meetingrooms4.Classes;

public class Rooms {

    private String room_name;
    private int room_capacity;
    private String room_description;
    private String room_group;
    private String room_status;


    public Rooms(String room_name, int room_capacity, String room_description, String room_group, String room_status) {
        this.room_name = room_name;
        this.room_capacity = room_capacity;
        this.room_description = room_description;
        this.room_group = room_group;
        this.room_status = room_status;
    }

    public String getRoom_name() {
        return room_name;
    }

    public void setRoom_name(String room_name) {
        this.room_name = room_name;
    }

    public int getRoom_capacity() {
        return room_capacity;
    }

    public void setRoom_capacity(int room_capacity) {
        this.room_capacity = room_capacity;
    }

    public String getRoom_description() {
        return room_description;
    }

    public void setRoom_description(String room_description) {
        this.room_description = room_description;
    }

    public String getRoom_group() {
        return room_group;
    }

    public void setRoom_group(String room_group) {
        this.room_group = room_group;
    }

    public String getRoom_status() {
        return room_status;
    }

    public void setRoom_status(String room_status) {
        this.room_status = room_status;
    }
}
