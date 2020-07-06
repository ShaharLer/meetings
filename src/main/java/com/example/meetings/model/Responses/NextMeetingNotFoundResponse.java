package com.example.meetings.model.Responses;

public class NextMeetingNotFoundResponse extends Response {

    private static final String NEXT_MEETING_NOT_FOUND_MESSAGE = "There is no meeting coming next";

    public NextMeetingNotFoundResponse() {
        super(NEXT_MEETING_NOT_FOUND_MESSAGE);
    }
}
