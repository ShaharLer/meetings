package com.example.meetings.model.Responses;

import com.example.meetings.model.Constants.ResponseConstants;
import com.example.meetings.model.Meeting;
import com.example.meetings.model.Responses.Response;

public class FoundNextMeetingResponse extends Response {

    private Meeting meeting;

    public FoundNextMeetingResponse(Meeting meeting) {
        super(ResponseConstants.getFoundNextMeetingMessage());
        this.meeting = meeting;
    }

    public Meeting getMeeting() {
        return meeting;
    }

    public void setMeeting(Meeting meeting) {
        this.meeting = meeting;
    }
}
