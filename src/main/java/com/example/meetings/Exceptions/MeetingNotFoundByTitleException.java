package com.example.meetings.Exceptions;

public class MeetingNotFoundByTitleException extends RuntimeException {

    private static final String MEETING_NOT_FOUND_BY_TITLE_ERROR = "Cannot find a meeting with the title: %s";

    public MeetingNotFoundByTitleException(String meetingTitle) {
        super(String.format(MEETING_NOT_FOUND_BY_TITLE_ERROR, meetingTitle));
    }
}
