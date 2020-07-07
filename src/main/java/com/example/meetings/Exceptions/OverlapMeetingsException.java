package com.example.meetings.Exceptions;

public class OverlapMeetingsException extends MeetingInvalidException {

    public static final String MEETINGS_OVERLAP_ERROR = "The meeting you are trying to insert overlaps with another meeting";

    public OverlapMeetingsException() {
        super(MEETINGS_OVERLAP_ERROR);
    }
}
