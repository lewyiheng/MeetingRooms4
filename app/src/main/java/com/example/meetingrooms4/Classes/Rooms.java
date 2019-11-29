package com.example.meetingrooms4.Classes;

public class Rooms {

    private String roomName;
    private int capacity;
    private String description;
    private String roomGroup;
    private String location;


    public Rooms(String roomName, int capacity, String description, String roomGroup, String location) {
        this.roomName = roomName;
        this.capacity = capacity;
        this.description = description;
        this.roomGroup = roomGroup;
        this.location = location;
    }


    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getRoomGroup() {
        return roomGroup;
    }

    public void setRoomGroup(String roomGroup) {
        this.roomGroup = roomGroup;
    }

}
