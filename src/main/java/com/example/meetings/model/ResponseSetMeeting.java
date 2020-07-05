package com.example.meetings.model;

public class ResponseSetMeeting extends Response {

    private Meeting meeting;

    public ResponseSetMeeting(String message, Meeting meeting) {
        super(message);
        this.meeting = meeting;
    }

    public Meeting getMeeting() {
        return meeting;
    }

    public void setMeeting(Meeting meeting) {
        this.meeting = meeting;
    }
}
