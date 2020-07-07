package com.example.meetings.Exceptions;

public class CrossDaysException extends MeetingInvalidException {

    public static final String MEETING_CROSS_DAYS_ERROR = "A meeting must start and finish on the same day";

    public CrossDaysException() {
        super(MEETING_CROSS_DAYS_ERROR);
    }
}
