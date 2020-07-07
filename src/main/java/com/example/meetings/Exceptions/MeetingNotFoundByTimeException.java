package com.example.meetings.Exceptions;

import com.example.meetings.model.MeetingConstants;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class MeetingNotFoundByTimeException extends RuntimeException {

    public static final String MEETING_NOT_FOUND_BY_FROM_TIME_ERROR = "Cannot find a meeting that starts at: %s";

    public MeetingNotFoundByTimeException(LocalDateTime fromTime) {
        super(String.format(MEETING_NOT_FOUND_BY_FROM_TIME_ERROR,
                fromTime.format(DateTimeFormatter.ofPattern(MeetingConstants.MEETING_TIME_PATTERN))));
    }
}
