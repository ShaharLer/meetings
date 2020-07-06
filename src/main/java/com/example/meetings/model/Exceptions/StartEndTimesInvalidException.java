package com.example.meetings.model.Exceptions;

import com.example.meetings.model.Constants.ExceptionsConstants;

public class StartEndTimesInvalidException extends MeetingInvalidException {

    public StartEndTimesInvalidException() {
        super(ExceptionsConstants.getMeetingStartEndTimesInvalidError());
    }
}
