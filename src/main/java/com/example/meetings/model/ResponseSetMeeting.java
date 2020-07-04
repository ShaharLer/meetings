package com.example.meetings.model;

public class ResponseSetMeeting extends Response {

    private Meeting addedMeeting;

    public ResponseSetMeeting(String message, Meeting addedMeeting) {
        super(message);
        this.addedMeeting = addedMeeting;
    }

    public Meeting getAddedMeeting() {
        return addedMeeting;
    }

    public void setAddedMeeting(Meeting addedMeeting) {
        this.addedMeeting = addedMeeting;
    }
}
