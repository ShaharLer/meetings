package com.example.meetings.service;

import com.example.meetings.model.Meeting;
import com.example.meetings.model.MeetingConstants;

import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalDateTime;

public class MeetingValidation {

    static boolean isStartEndTimesValid(Meeting meeting) {
        LocalDateTime fromTime = meeting.getFromTime();
        LocalDateTime toTime = meeting.getToTime();
        return !(fromTime.isAfter(toTime));
    }

    static boolean isMeetingDurationValid(Meeting meeting) {
        int meetingDurationInMinutes = getMeetingDurationInMinutes(meeting);
        return (meetingDurationInMinutes >= MeetingConstants.MINIMUM_MEETING_TIME_IN_MINUTES &&
                meetingDurationInMinutes <= MeetingConstants.MAXIMUM_MEETING_TIME_IN_MINUTES);
    }

    static int getMeetingDurationInMinutes(Meeting meeting) {
        return (int) Duration.between(meeting.getFromTime(), meeting.getToTime()).toMinutes();
    }

    static boolean isCrossDaysMeeting(Meeting meeting) {
        LocalDateTime fromTime = meeting.getFromTime();
        LocalDateTime toTime = meeting.getToTime();
        return (fromTime.getDayOfWeek() != toTime.getDayOfWeek());
    }

    static boolean isMeetingOnSaturday(Meeting meeting) {
        return (meeting.getFromTime().getDayOfWeek() == DayOfWeek.SATURDAY);
    }

    static boolean meetingsOverlap(Meeting newMeeting, Meeting existedMeeting) {
        return (newMeeting.getFromTime().isBefore(existedMeeting.getToTime()) &&
                existedMeeting.getFromTime().isBefore(newMeeting.getToTime()));
    }

    static boolean isDailyRequirementsValid(Meeting meeting, LocalDateTime[] sameDayMeetingsRange) {
        int diffFromFirstMeetingStart = (int) Math.abs(Duration.between(meeting.getToTime(), sameDayMeetingsRange[0]).toMinutes());
        int diffFromLastMeetingEnd = (int) Math.abs(Duration.between(meeting.getFromTime(), sameDayMeetingsRange[1]).toMinutes());
        int maximumDailyMeetingsMinutes = MeetingConstants.MAXIMUM_DAILY_MEETINGS_HOURS_DIFF * 60;
        return ((diffFromFirstMeetingStart <= maximumDailyMeetingsMinutes) && (diffFromLastMeetingEnd <= maximumDailyMeetingsMinutes));
    }

    static boolean isWeeklyRequirementsValid(int totalWeekMeetingsHours) {
        return (totalWeekMeetingsHours <= MeetingConstants.MAXIMUM_WEEKLY_MEETINGS_HOURS * 60);
    }
}
