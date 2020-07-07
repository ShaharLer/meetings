package com.example.meetings.Exceptions;

import com.example.meetings.model.MeetingConstants;

public class DailyRequirementsException extends MeetingInvalidException {

    // TODO improve this (write in which manner the inserted meeting prohibits this)
    private static final String MEETING_DAILY_REQUIREMENTS_ERROR =
            "The last meeting of a day must end up to %d hours after the first meeting started";

    public DailyRequirementsException() {
        super(String.format(MEETING_DAILY_REQUIREMENTS_ERROR, MeetingConstants.MAXIMUM_DAILY_MEETINGS_HOURS_DIFF));
    }
}
