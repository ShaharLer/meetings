package com.example.meetings.Exceptions;

import com.example.meetings.model.MeetingConstants;

public class DurationException extends MeetingInvalidException {

    public static final String MEETING_DURATION_NOT_VALID = "Meeting duration must be between %d minutes to %d minutes";

    public DurationException() {
        super(String.format(MEETING_DURATION_NOT_VALID, MeetingConstants.MINIMUM_MEETING_TIME_IN_MINUTES,
                MeetingConstants.MAXIMUM_MEETING_TIME_IN_MINUTES));
    }
}
