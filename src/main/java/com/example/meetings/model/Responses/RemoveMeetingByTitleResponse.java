package com.example.meetings.model.Responses;

import com.example.meetings.model.Constants.ResponseConstants;
import com.example.meetings.model.Meeting;

import java.util.List;

public class RemoveMeetingByTitleResponse extends Response {

    private List<Meeting> deletedMeetings;

    public RemoveMeetingByTitleResponse(String meetingsTitle, List<Meeting> deletedMeetings) {
        super(ResponseConstants.getDeletedMeetingsByTitleMessage(meetingsTitle));
        this.deletedMeetings = deletedMeetings;
    }

    public List<Meeting> getDeletedMeetings() {
        return deletedMeetings;
    }

    public void setDeletedMeetings(List<Meeting> deletedMeetings) {
        this.deletedMeetings = deletedMeetings;
    }
}
