package com.example.meetings.model;

import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.TemporalAdjusters;

public class Utils {

    public static boolean isStartEndTimesValid(Meeting meeting) {
        LocalDateTime fromTime = meeting.getFromTime();
        LocalDateTime toTime = meeting.getToTime();
        return !(fromTime.isAfter(toTime));
    }

    public static boolean isMeetingDurationValid(Meeting meeting) {
        int meetingDurationInMinutes = getMeetingDurationInMinutes(meeting);
        return (meetingDurationInMinutes >= MeetingConstants.getMinimumMeetingTime() &&
                meetingDurationInMinutes <= MeetingConstants.getMaximumMeetingTime());
    }

    public static boolean isCrossDaysMeeting(Meeting meeting) {
        LocalDateTime fromTime = meeting.getFromTime();
        LocalDateTime toTime = meeting.getToTime();
        return (fromTime.getDayOfWeek() != toTime.getDayOfWeek());
    }

    public static boolean isMeetingOnSaturday(Meeting meeting) {
        return (meeting.getFromTime().getDayOfWeek() == DayOfWeek.SATURDAY);
    }

    public static LocalDate getWeekStartDate(LocalDateTime date) {
        LocalDate weekStartDate = date.toLocalDate().with(TemporalAdjusters.previousOrSame(DayOfWeek.SUNDAY));
        return weekStartDate;
    }

    public static int updateTotalWeekMeetingsInMinutes(int totalWeekMeetingsInMinutes, Meeting meeting) {
        return (totalWeekMeetingsInMinutes + getMeetingDurationInMinutes(meeting));
    }

    private static int getMeetingDurationInMinutes(Meeting meeting) {
        return (int) Duration.between(meeting.getFromTime(), meeting.getToTime()).toMinutes();
    }

    public static boolean meetingsOnTheSameDay(Meeting firstMeeting, Meeting secondMeeting) {
        LocalDate firstMeetingDate = firstMeeting.getFromTime().toLocalDate();
        LocalDate secondMeetingDate = secondMeeting.getFromTime().toLocalDate();
        return firstMeetingDate.equals(secondMeetingDate);
    }
}
