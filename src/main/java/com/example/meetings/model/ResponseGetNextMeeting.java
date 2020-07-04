package com.example.meetings.model;

public class ResponseGetNextMeeting extends Response {

    private Meeting nextMeeting;

    public ResponseGetNextMeeting(String message, Meeting nextMeeting) {
        super(message);
        this.nextMeeting = nextMeeting;
    }

    public Meeting getNextMeeting() {
        return nextMeeting;
    }

    public void setNextMeeting(Meeting nextMeeting) {
        this.nextMeeting = nextMeeting;
    }
}
