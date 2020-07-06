package com.example.meetings.model.Exceptions;

import com.example.meetings.model.Constants.ExceptionsConstants;

public class SaturdayException extends MeetingInvalidException {

    public SaturdayException() {
        super(ExceptionsConstants.getMeetingOnSaturdayError());
    }
}
