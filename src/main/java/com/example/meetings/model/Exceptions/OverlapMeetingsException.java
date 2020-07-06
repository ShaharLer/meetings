package com.example.meetings.model.Exceptions;

public class OverlapMeetingsException extends MeetingInvalidException {

    // TODO add the overlapped meeting
    private static final String MEETINGS_OVERLAP_ERROR = "The meeting you are trying to insert overlaps with another meeting";

    public OverlapMeetingsException() {
        super(MEETINGS_OVERLAP_ERROR);
    }
}
