package com.example.meetings.model.Exceptions;

import com.example.meetings.model.Constants.ExceptionsConstants;

public class DailyRequirementsException extends MeetingInvalidException {

    public DailyRequirementsException() {
        super(ExceptionsConstants.getMeetingDailyRequirementsError());
    }
}
