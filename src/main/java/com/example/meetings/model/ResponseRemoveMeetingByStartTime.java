package com.example.meetings.model;

public class ResponseRemoveMeetingByStartTime extends Response {

    private Meeting deletedMeeting;

    public ResponseRemoveMeetingByStartTime(String message, Meeting addedMeeting) {
        super(message);
        this.deletedMeeting = addedMeeting;
    }

    public Meeting getAddedMeeting() {
        return deletedMeeting;
    }

    public void setAddedMeeting(Meeting addedMeeting) {
        this.deletedMeeting = addedMeeting;
    }

}
