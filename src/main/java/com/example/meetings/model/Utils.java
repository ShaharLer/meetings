package com.example.meetings.model;

import com.example.meetings.model.Constants.MeetingConstants;

import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.TemporalAdjusters;
import java.util.List;

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

    public static boolean meetingsOverlap(Meeting newMeeting, Meeting existedMeeting) {
        return (newMeeting.getFromTime().isBefore(existedMeeting.getToTime()) &&
                existedMeeting.getFromTime().isBefore(newMeeting.getToTime()));
    }

    public static void updateMeetingsRangeOfDay(Meeting firstMeeting, Meeting secondMeeting, LocalDateTime[] meetingsRange) {
        if (meetingOnTheSameDay(firstMeeting, secondMeeting)) {
            LocalDateTime fromTime = secondMeeting.getFromTime();
            LocalDateTime toTime = secondMeeting.getToTime();

            if (fromTime.isBefore(meetingsRange[0])) {
                meetingsRange[0] = fromTime;
            }
            if (toTime.isAfter(meetingsRange[1])) {
                meetingsRange[1] = toTime;
            }
        }
    }

    private static boolean meetingOnTheSameDay(Meeting firstMeeting, Meeting secondMeeting) {
        LocalDate firstMeetingDate = firstMeeting.getFromTime().toLocalDate();
        LocalDate secondMeetingDate = secondMeeting.getFromTime().toLocalDate();
        return firstMeetingDate.equals(secondMeetingDate);
    }

    public static boolean isDailyRequirementsValid(Meeting meeting, LocalDateTime[] sameDayMeetingsRange) {
        // Requirement (6): The last meeting on the same day must end up to 10 hours after the first meeting started
        int diffFromFirstMeetingStart = (int) Math.abs(Duration.between(meeting.getToTime(), sameDayMeetingsRange[0]).toMinutes());
        int diffFromLastMeetingEnd = (int) Math.abs(Duration.between(meeting.getFromTime(), sameDayMeetingsRange[1]).toMinutes());
        int maximumDailyMeetingsMinutes = MeetingConstants.getMaximumDailyMeetingsHoursDiff() * 60;
        return ((diffFromFirstMeetingStart <= maximumDailyMeetingsMinutes) && (diffFromLastMeetingEnd <= maximumDailyMeetingsMinutes));
    }

    public static boolean isWeeklyRequirementsValid(int totalWeekMeetingsHours) {
        // Requirement (5)
        return (totalWeekMeetingsHours <= MeetingConstants.getMaximumWeeklyMeetingsHours() * 60);
    }

    public static int weeksBinarySearch(List<LocalDate> weeksStartingDays, LocalDate weekStartDate) {
        int start = 0;
        int end = weeksStartingDays.size() - 1;
        while (start <= end) {
            int middle = start + ((end - start) / 2);
            if (weekStartDate.isEqual(weeksStartingDays.get(middle))) {
                return middle;
            } else if (weekStartDate.isAfter(weeksStartingDays.get(middle))) {
                start = middle + 1;
            } else {
                end = middle - 1;
            }
        }
        return start; // returns the next week with a meeting (out of bounds if no future meetings exist)
    }

}
