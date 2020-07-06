package com.example.meetings.model.Exceptions;

import com.example.meetings.model.MeetingConstants;

public class DurationException extends MeetingInvalidException {

    private static final String MEETING_DURATION_NOT_VALID = "Meeting duration must be between %d minutes to %d minutes";

    public DurationException() {
        super(String.format(MEETING_DURATION_NOT_VALID, MeetingConstants.getMinimumMeetingTime(),
                MeetingConstants.getMaximumMeetingTime()));
    }
}
