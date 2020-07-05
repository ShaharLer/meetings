package com.example.meetings.model;

public class ResponseRemoveMeetingByStartTime extends Response {

    private Meeting meeting;

    public ResponseRemoveMeetingByStartTime(String message, Meeting addedMeeting) {
        super(message);
        this.meeting = addedMeeting;
    }

    public Meeting getMeeting() {
        return meeting;
    }

    public void setMeeting(Meeting addedMeeting) {
        this.meeting = addedMeeting;
    }

}
