package com.example.meetings.model.Responses;

import com.example.meetings.model.Constants.ResponseConstants;
import com.example.meetings.model.Meeting;

public class SetMeetingResponse extends Response {

    private Meeting meeting;

    public SetMeetingResponse(Meeting meeting) {
        super(ResponseConstants.getAddedMeetingMessage());
        this.meeting = meeting;
    }

    public Meeting getMeeting() {
        return meeting;
    }

    public void setMeeting(Meeting meeting) {
        this.meeting = meeting;
    }
}
