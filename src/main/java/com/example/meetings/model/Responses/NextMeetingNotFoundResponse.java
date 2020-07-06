package com.example.meetings.model.Responses;

import com.example.meetings.model.Constants.ResponseConstants;

public class NextMeetingNotFoundResponse extends Response {

    public NextMeetingNotFoundResponse() {
        super(ResponseConstants.getNextMeetingNotFoundMessage());
    }
}
