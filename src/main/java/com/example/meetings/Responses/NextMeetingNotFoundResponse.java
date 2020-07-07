package com.example.meetings.Responses;

public class NextMeetingNotFoundResponse extends Response {

    public static final String NEXT_MEETING_NOT_FOUND_MESSAGE = "There is no meeting coming next";

    public NextMeetingNotFoundResponse() {
        super(NEXT_MEETING_NOT_FOUND_MESSAGE);
    }
}
