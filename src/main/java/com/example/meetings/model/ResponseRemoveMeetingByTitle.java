package com.example.meetings.model;

import java.util.List;

public class ResponseRemoveMeetingByTitle extends Response {

    private List<Meeting> deletedMeetings;

    public ResponseRemoveMeetingByTitle(String message, List<Meeting> deletedMeetings) {
        super(message);
        this.deletedMeetings = deletedMeetings;
    }

    public List<Meeting> getDeletedMeetings() {
        return deletedMeetings;
    }

    public void setDeletedMeetings(List<Meeting> deletedMeetings) {
        this.deletedMeetings = deletedMeetings;
    }
}
