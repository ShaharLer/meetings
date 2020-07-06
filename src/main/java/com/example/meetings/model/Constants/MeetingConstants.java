package com.example.meetings.model.Constants;

public class MeetingConstants {

    private static final int MINIMUM_MEETING_TIME = 15;
    private static final int MAXIMUM_MEETING_TIME = 120;
    private static final int MAXIMUM_DAILY_MEETINGS_HOURS_DIFF = 10;
    private static final int MAXIMUM_WEEKLY_MEETINGS_HOURS = 40;

    public static int getMinimumMeetingTime() {
        return MINIMUM_MEETING_TIME;
    }

    public static int getMaximumMeetingTime() {
        return MAXIMUM_MEETING_TIME;
    }

    public static int getMaximumDailyMeetingsHoursDiff() {
        return MAXIMUM_DAILY_MEETINGS_HOURS_DIFF;
    }

    public static int getMaximumWeeklyMeetingsHours() {
        return MAXIMUM_WEEKLY_MEETINGS_HOURS;
    }
}
