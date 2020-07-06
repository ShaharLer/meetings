package com.example.meetings.model.Exceptions;

import com.example.meetings.model.Constants.ExceptionsConstants;

public class OverlapMeetingsException extends MeetingInvalidException {

    public OverlapMeetingsException() {
        super(ExceptionsConstants.getMeetingsOverlapError());
    }
}
