package com.example.meetings.model.Exceptions;

import com.example.meetings.model.Constants.ExceptionsConstants;

public class DurationException extends MeetingInvalidException {

    public DurationException() {
        super(ExceptionsConstants.getMeetingDurationNotValid());
    }
}
