package com.example.meetings.Exceptions;

import com.example.meetings.model.MeetingConstants;

public class WeeklyRequirementsException extends MeetingInvalidException {

    public static final String MEETING_WEEKLY_REQUIREMENTS_ERROR = "There cannot be more than %d hours of meetings a week";

    public WeeklyRequirementsException() {
        super(String.format(MEETING_WEEKLY_REQUIREMENTS_ERROR, MeetingConstants.MAXIMUM_WEEKLY_MEETINGS_HOURS));
    }
}
