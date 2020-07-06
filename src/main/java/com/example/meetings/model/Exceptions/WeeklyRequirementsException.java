package com.example.meetings.model.Exceptions;

import com.example.meetings.model.Constants.ExceptionsConstants;

public class WeeklyRequirementsException extends MeetingInvalidException {

    public WeeklyRequirementsException() {
        super(ExceptionsConstants.getMeetingWeeklyRequirementsError());
    }
}
