package com.example.meetings.Exceptions;

public class StartEndTimesInvalidException extends MeetingInvalidException {

    public static final String MEETING_START_END_TIMES_INVALID_ERROR = "fromTime cannot be after toTime";

    public StartEndTimesInvalidException() {
        super(MEETING_START_END_TIMES_INVALID_ERROR);
    }
}
