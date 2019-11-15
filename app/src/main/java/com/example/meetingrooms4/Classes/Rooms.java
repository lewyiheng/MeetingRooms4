package com.example.meetingrooms4.Classes;

public class Rooms {

    private String roomName;
    private String roomDesc;

    public Rooms(String roomName, String roomDesc) {
        this.roomName = roomName;
        this.roomDesc = roomDesc;
    }

    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    public String getRoomDesc() {
        return roomDesc;
    }

    public void setRoomDesc(String roomDesc) {
        this.roomDesc = roomDesc;
    }
}
