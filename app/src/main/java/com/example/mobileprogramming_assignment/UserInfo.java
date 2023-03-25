package com.example.mobileprogramming_assignment;

public class UserInfo {
    private final String uid;
    private final String email;
    private String name;
    private String gender;
    private final String dateOfBirth;
    private boolean isTopic1Done;
    private boolean isTopic2Done;
    private boolean isTopic3Done;
    private boolean isTopic4Done;
    private boolean isExamDone;
    private int mark;

    public UserInfo(String uid, String name, String email, String gender, String dateOfBirth, boolean isTopic1Done, boolean isTopic2Done, boolean isTopic3Done, boolean isTopic4Done, boolean isExamDone, int mark) {
        this.uid = uid;
        this.name = name;
        this.email = email;
        this.gender = gender;
        this.dateOfBirth = dateOfBirth;
        this.isTopic1Done = isTopic1Done;
        this.isTopic2Done = isTopic2Done;
        this.isTopic3Done = isTopic3Done;
        this.isTopic4Done = isTopic4Done;
        this.isExamDone = isExamDone;
        this.mark = mark;
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

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public boolean isTopic1Done() {
        return isTopic1Done;
    }

    public void setTopic1Done(boolean topic1Done) {
        isTopic1Done = topic1Done;
    }

    public boolean isTopic2Done() {
        return isTopic2Done;
    }

    public void setTopic2Done(boolean topic2Done) {
        isTopic2Done = topic2Done;
    }

    public boolean isTopic3Done() {
        return isTopic3Done;
    }

    public void setTopic3Done(boolean topic3Done) {
        isTopic3Done = topic3Done;
    }

    public boolean isTopic4Done() {
        return isTopic4Done;
    }

    public void setTopic4Done(boolean topic4Done) {
        isTopic4Done = topic4Done;
    }

    public boolean isExamDone() {
        return isExamDone;
    }

    public void setExamDone(boolean examDone) {
        isExamDone = examDone;
    }

    public int getMark() {
        return mark;
    }

    public void setMark(int mark) {
        this.mark = mark;
    }
}
