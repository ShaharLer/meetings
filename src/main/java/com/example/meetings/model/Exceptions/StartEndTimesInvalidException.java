package com.example.meetings.model.Exceptions;

public class StartEndTimesInvalidException extends MeetingInvalidException {

    private static final String MEETING_START_END_TIMES_INVALID_ERROR = "fromTime cannot be after toTime";

    public StartEndTimesInvalidException() {
        super(MEETING_START_END_TIMES_INVALID_ERROR);
    }
}
