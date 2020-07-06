package com.example.meetings.model.Exceptions;

import com.example.meetings.model.MeetingConstants;

public class WeeklyRequirementsException extends MeetingInvalidException {

    // TODO improve this (add how many meetings hours current are)
    private static final String MEETING_WEEKLY_REQUIREMENTS_ERROR = "There cannot be more than %d hours of meetings a week";

    public WeeklyRequirementsException() {
        super(String.format(MEETING_WEEKLY_REQUIREMENTS_ERROR, MeetingConstants.getMaximumWeeklyMeetingsHours()));
    }
}
