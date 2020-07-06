package com.example.meetings.model.Exceptions;

public class SaturdayException extends MeetingInvalidException {

    private static final String MEETING_ON_SATURDAY_ERROR = "A meeting cannot be set to Saturday";

    public SaturdayException() {
        super(MEETING_ON_SATURDAY_ERROR);
    }
}
