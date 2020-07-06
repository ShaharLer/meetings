package com.example.meetings.model.Exceptions;

public class MeetingInvalidException extends RuntimeException {

    // TODO check how can stack trace can be omitted
    public MeetingInvalidException(String message) {
        super(message);
    }
}
