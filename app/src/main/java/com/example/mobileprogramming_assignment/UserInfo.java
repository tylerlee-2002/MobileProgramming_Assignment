package com.example.mobileprogramming_assignment;

public class UserInfo {
    private final String uid;
    private final String phoneNumber;
    private final String email;
    private final int completeUntil;
    private String name;
    public UserInfo(String uid, String name, String phoneNumber, String email, int completeUntil) {
        this.uid = uid;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.completeUntil = completeUntil;
    }

    public String getuid() {
        return uid;
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

    public String getEmail() {
        return email;
    }

    public int getCompleteUntil() {
        return completeUntil;
    }
}
