package com.example.meetings.model;

public class ResponseGetNextMeeting extends Response {

    private Meeting meeting;

    public ResponseGetNextMeeting(String message, Meeting meeting) {
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
