package com.example.meetings.model.Exceptions;

import com.example.meetings.model.Constants.ExceptionsConstants;

public class MeetingNotFoundByTitleException extends RuntimeException {

    public MeetingNotFoundByTitleException(String meetingTitle) {
        super(ExceptionsConstants.getMeetingNotFoundByTitleError(meetingTitle));
    }
}
