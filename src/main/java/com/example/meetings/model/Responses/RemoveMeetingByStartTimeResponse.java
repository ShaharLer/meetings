package com.example.meetings.model.Responses;

import com.example.meetings.model.Constants.ResponseConstants;
import com.example.meetings.model.Meeting;

public class RemoveMeetingByStartTimeResponse extends Response {

    private Meeting deletedMeeting;

    public RemoveMeetingByStartTimeResponse(Meeting deletedMeeting) {
        super(ResponseConstants.getDeletedMeetingByFromTimeMessage(deletedMeeting.getFromTime()));
        this.deletedMeeting = deletedMeeting;
    }

    public Meeting getDeletedMeeting() {
        return deletedMeeting;
    }

    public void setDeletedMeeting(Meeting addedMeeting) {
        this.deletedMeeting = addedMeeting;
    }

}
