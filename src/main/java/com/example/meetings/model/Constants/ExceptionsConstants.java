package com.example.meetings.model.Constants;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ExceptionsConstants {

    private static final String MEETING_START_END_TIMES_INVALID_ERROR = "fromTime cannot be after toTime";
    private static final String MEETING_DURATION_NOT_VALID = "Meeting duration must be between %d minutes to %d minutes";
    private static final String MEETING_CROSS_DAYS_ERROR = "A meeting must start and finish on the same day";
    private static final String MEETING_ON_SATURDAY_ERROR = "A meeting cannot be set to Saturday";

    // TODO add the overlapped meeting
    private static final String MEETINGS_OVERLAP_ERROR = "The meeting you are trying to insert overlaps with another meeting";

    // TODO improve this (write in which manner the inserted meeting prohibits this)
    private static final String MEETING_DAILY_REQUIREMENTS_ERROR =
            "The last meeting of a day must end up to %d hours after the first meeting started";

    // TODO improve this (add how many meetings hours current are)
    private static final String MEETING_WEEKLY_REQUIREMENTS_ERROR = "There cannot be more than %d hours of meetings a week";
    private static final String MEETING_NOT_FOUND_BY_FROM_TIME_ERROR = "Cannot find a meeting that starts at: %s";
    private static final String MEETING_NOT_FOUND_BY_TITLE_ERROR = "Cannot find a meeting with the title \"%s\"";




    public static String getMeetingStartEndTimesInvalidError() {
        return MEETING_START_END_TIMES_INVALID_ERROR;
    }

    public static String getMeetingDurationNotValid() {
        return String.format(MEETING_DURATION_NOT_VALID, MeetingConstants.getMinimumMeetingTime(), MeetingConstants.getMaximumMeetingTime());
    }

    public static String getCrossDaysError() {
        return MEETING_CROSS_DAYS_ERROR;
    }

    public static String getMeetingOnSaturdayError() {
        return MEETING_ON_SATURDAY_ERROR;
    }

    public static String getMeetingsOverlapError() {
        return MEETINGS_OVERLAP_ERROR;
    }

    public static String getMeetingDailyRequirementsError() {
        return String.format(MEETING_DAILY_REQUIREMENTS_ERROR, MeetingConstants.getMaximumDailyMeetingsHoursDiff());
    }

    public static String getMeetingWeeklyRequirementsError() {
        return String.format(MEETING_WEEKLY_REQUIREMENTS_ERROR, MeetingConstants.getMaximumWeeklyMeetingsHours());
    }

    public static String getMeetingNotFoundByFromTimeError(LocalDateTime fromTime) {
        // TODO deduplicate the pattern
        return String.format(MEETING_NOT_FOUND_BY_FROM_TIME_ERROR, fromTime.format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm")));
    }

    public static String getMeetingNotFoundByTitleError(String meetingTitle) {
        return String.format(MEETING_NOT_FOUND_BY_TITLE_ERROR, meetingTitle);
    }
}
