package com.example.meetingrooms4.Classes;

public class User {
    private String username;
    private String password;
    private String role;
    private int mobile_number;
    private int office_number;
    private String name;
    private int employment_status;
    private String group_name;

    public User(String username, String password, String role, int mobile_number, int office_number, String name, int employment_status, String group_name) {
        this.username = username;
        this.password = password;
        this.role = role;
        this.mobile_number = mobile_number;
        this.office_number = office_number;
        this.name = name;
        this.employment_status = employment_status;
        this.group_name = group_name;
    }

    public User(){

    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public int getMobile_number() {
        return mobile_number;
    }

    public void setMobile_number(int mobile_number) {
        this.mobile_number = mobile_number;
    }

    public int getOffice_number() {
        return office_number;
    }

    public void setOffice_number(int office_number) {
        this.office_number = office_number;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getEmployment_status() {
        return employment_status;
    }

    public void setEmployment_status(int employment_status) {
        this.employment_status = employment_status;
    }

    public String getGroup_name() {
        return group_name;
    }

    public void setGroup_name(String group_name) {
        this.group_name = group_name;
    }
}
