package com.example.mobileprogramming_assignment;

public class UserInfo {

    public UserInfo(String uid, String name, String phoneNumber, String email, int completeUntil) {
        this.uid = uid;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.completeUntil = completeUntil;
    }

    private String uid;
    private String name;
    private String phoneNumber;
    private String email;

    private int completeUntil;

    public String getuid() {
        return uid;
    }

    public void setuid(String uid) {
        this.uid = uid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getCompleteUntil() {
        return completeUntil;
    }

    public void setCompleteUntil(int completeUntil) {
        this.completeUntil = completeUntil;
    }
}
