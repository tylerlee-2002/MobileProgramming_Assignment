package com.example.mobileprogramming_assignment;

public class UserInfo {
    private final String uid;
    private final String email;
    private final int completeUntil;
    private String name;
    private String gender;
    private String dateOfBirth;

    public UserInfo(String uid, String name, String email, String gender, String dateOfBirth, int completeUntil) {
        this.uid = uid;
        this.name = name;
        this.email = email;
        this.gender = gender;
        this.dateOfBirth = dateOfBirth;
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

    public String getEmail() {
        return email;
    }

    public int getCompleteUntil() {
        return completeUntil;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }
}
