package com.example.meetings.model.Exceptions;

import com.example.meetings.model.Constants.ExceptionsConstants;

public class CrossDaysException extends MeetingInvalidException {

    public CrossDaysException() {
        super(ExceptionsConstants.getCrossDaysError());
    }
}
