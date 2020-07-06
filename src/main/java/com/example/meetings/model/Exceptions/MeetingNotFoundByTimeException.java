package com.example.meetings.model.Exceptions;

import com.example.meetings.model.Constants.ExceptionsConstants;

import java.time.LocalDateTime;

public class MeetingNotFoundByTimeException extends RuntimeException {

    public MeetingNotFoundByTimeException(LocalDateTime fromTime) {
        super(ExceptionsConstants.getMeetingNotFoundByFromTimeError(fromTime));
    }
}
