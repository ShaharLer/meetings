package com.example.meetings.dao;

import com.example.meetings.model.Meeting;
import org.springframework.stereotype.Repository;

import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.TemporalAdjusters;
import java.time.temporal.WeekFields;
import java.util.*;

@Repository("meetingDao")
public class MeetingDataAccessService implements MeetingDao {

    private final static int MINIMUM_MEETING_TIME = 15; // TODO check how to use static folder
    private final static int MAXIMUM_MEETING_TIME = 120; // TODO check how to use static folder
    private final static int MAXIMUM_DAILY_MEETING_HOURS_DIFF = 10; // TODO check how to use static folder
    private final static int MAXIMUM_WEEKLY_MEETING_HOURS = 40; // TODO check how to use static folder

    private static final SortedMap<LocalDate, SortedMap<LocalDateTime, Meeting>> allMeetings = new TreeMap<>();
    private static final Map<String, List<LocalDateTime>> titleToStartTime = new HashMap<>();

//    private static final SortedMap<LocalDateTime, Meeting> meetings = new TreeMap<>();

    @Override
    public Meeting setMeeting(Meeting meeting) {
        if (!isMeetingValid(meeting)) {
            return null;
        }

        addMeeting(meeting);
        return meeting;
    }

    private boolean isMeetingValid(Meeting meeting) {
        if (!isSingleMeetingRequirementsValid(meeting)) {
            return false;
        }

        if (!isScheduleRequirementsValid(meeting)) {
            return false;
        }

        return true;
    }

    private boolean isSingleMeetingRequirementsValid(Meeting meeting) {
        LocalDateTime fromTime = meeting.getFromTime(), toTime = meeting.getToTime();
        long meetingDuration = Duration.between(fromTime, toTime).toMinutes();

        if (fromTime.isAfter(toTime)) { // Check that start time is not after end time
            return false;
        }

        if (fromTime.getDayOfWeek() != toTime.getDayOfWeek()) { // Assumption: No cross-days meetings
            return false;
        }

        if (fromTime.getDayOfWeek() == DayOfWeek.SATURDAY) { // Requirement (3): No meetings on Saturdays
            return false;
        }

        //  Requirement (1): A meeting cannot last for more than 2 hours, and for less than 15 minutes
        if (meetingDuration > MAXIMUM_MEETING_TIME || meetingDuration < MINIMUM_MEETING_TIME) {
            return false;
        }

        return true;
    }

    private boolean isScheduleRequirementsValid(Meeting meeting) {
        LocalDateTime fromTime = meeting.getFromTime();
        LocalDateTime toTime = meeting.getToTime();
        LocalDateTime firstMeetingStart = fromTime;
        LocalDateTime lastMeetingEnd = toTime;
        LocalDate meetingDate = fromTime.toLocalDate();
        LocalDate weekStart = getWeekStart(fromTime);
        long totalWeekMeetingsHours = Duration.between(fromTime, toTime).toMinutes();

        if (!allMeetings.containsKey(weekStart)) {
            return true;
        }

        List<Meeting> meetingOfTheWeek = new ArrayList<>(allMeetings.get(weekStart).values());
        for (Meeting comparedMeeting : meetingOfTheWeek) { // Run on the weekly meetings
            LocalDateTime comparedMeetingFromTime = comparedMeeting.getFromTime();
            LocalDateTime comparedMeetingToTime = comparedMeeting.getToTime();
            LocalDate comparedMeetingDate = comparedMeetingFromTime.toLocalDate();

            // Requirement (2): Two meetings cannot overlap
            if (fromTime.isBefore(comparedMeetingToTime) && comparedMeetingFromTime.isBefore(toTime)) {
                return false;
            }

            if (meetingDate.isEqual(comparedMeetingDate)) { // Meeting at the same day
                if (comparedMeetingFromTime.isBefore(firstMeetingStart)) {
                    firstMeetingStart = comparedMeetingFromTime;
                } else if (comparedMeetingToTime.isAfter(lastMeetingEnd)) {
                    lastMeetingEnd = comparedMeetingToTime;
                }
            }

            totalWeekMeetingsHours += Duration.between(comparedMeetingFromTime, comparedMeetingToTime).toMinutes();
        }

        // Requirement (6): The last meeting on the same day must end up to 10 hours after the first meeting started
        if (!isDailySchedulingValid(meeting, firstMeetingStart, lastMeetingEnd)) {
            return false;
        }

        if (!isWeeklySchedulingValid(totalWeekMeetingsHours)) {
            return false;
        }

        return true;
    }

    private boolean isDailySchedulingValid(Meeting meeting, LocalDateTime firstMeetingStart, LocalDateTime lastMeetingEnd) {
        // Requirement (6): The last meeting on the same day must end up to 10 hours after the first meeting started
        LocalDateTime fromTime = meeting.getFromTime();
        LocalDateTime toTime = meeting.getToTime();
        long diffFromFirstMeeting = Math.abs(Duration.between(firstMeetingStart, toTime).toMinutes());
        long diffFromLastMeeting = Math.abs(Duration.between(fromTime, lastMeetingEnd).toMinutes());
        long maximumDailyMeetingsTime = MAXIMUM_DAILY_MEETING_HOURS_DIFF * 60;
        return ((diffFromFirstMeeting <= maximumDailyMeetingsTime) && (diffFromLastMeeting <= maximumDailyMeetingsTime));
    }

    private boolean isWeeklySchedulingValid(long totalWeekMeetingsHours) {
        // Requirement (5): There are up to 40 working hours a week (Sunday to Friday)
        return (totalWeekMeetingsHours <= MAXIMUM_WEEKLY_MEETING_HOURS * 60);
    }

    private void addMeeting(Meeting meeting) {
        addMeetingToMeetingsMap(meeting);
        addMeetingToTitlesMap(meeting);
    }

    private void addMeetingToMeetingsMap(Meeting meeting) {
        LocalDate weekStartDate = getWeekStart(meeting.getFromTime());
        if (!allMeetings.containsKey(weekStartDate)) {
            allMeetings.put(weekStartDate, new TreeMap<>());
        }
        allMeetings.get(weekStartDate).put(meeting.getFromTime(), meeting);
    }

    private void addMeetingToTitlesMap(Meeting meeting) {
        String title = meeting.getMeetingTitle();
        if (!titleToStartTime.containsKey(title)) {
            titleToStartTime.put(title, new ArrayList<>());
        }
        titleToStartTime.get(title).add(meeting.getFromTime());
    }

    @Override
    public Meeting removeMeetingByStartTime(LocalDateTime fromTime) {
        Meeting meeting = removeMeetingFromMeetingsMap(fromTime);
        removeMeetingKeyFromTitlesMap(meeting);
        return meeting;
    }

    @Override
    public List<Meeting> removeMeetingByTitle(String meetingTitle) {
        List<Meeting> deletedMeetings = new ArrayList<>();
        if (titleToStartTime.get(meetingTitle) != null) {
            for (LocalDateTime fromTime : titleToStartTime.get(meetingTitle)) {
                deletedMeetings.add(removeMeetingFromMeetingsMap(fromTime));
            }
            titleToStartTime.remove(meetingTitle);
        }
        return deletedMeetings;
    }

    private Meeting removeMeetingFromMeetingsMap(LocalDateTime fromTime) {
        Meeting deletedMeeting = null;
        LocalDate weekStart = getWeekStart(fromTime);
        Map<LocalDateTime, Meeting> weekMeetings = allMeetings.get(weekStart);

        if (weekMeetings != null) {
            deletedMeeting = weekMeetings.get(fromTime);
            weekMeetings.remove(fromTime);
            if (weekMeetings.isEmpty()) {
                allMeetings.remove(weekStart);
            }
        }
        return deletedMeeting;
    }

    private void removeMeetingKeyFromTitlesMap(Meeting meeting) {
        if (meeting == null) {
            return;
        }

        String title = meeting.getMeetingTitle();
        titleToStartTime.get(title).removeIf(fromTime -> fromTime.isEqual(meeting.getFromTime()));
        if (titleToStartTime.get(title).isEmpty()) {
            titleToStartTime.remove(title);
        }
    }

    @Override
    public Meeting getNextMeeting() {
        LocalDateTime now = LocalDateTime.now();
        List<LocalDate> weeksStartList = new ArrayList<>(allMeetings.keySet());
        int weekIndexToSearch = weeksBinarySearch(weeksStartList, getWeekStart(now));
        Meeting meeting = searchNextMeeting(weeksStartList, weekIndexToSearch, now);
        if (meeting == null) {
            meeting = searchNextMeeting(weeksStartList, weekIndexToSearch + 1, now);
        }

        return meeting;
    }

    private int weeksBinarySearch(List<LocalDate> weeksStartingDays, LocalDate weekStart) {
        int start = 0;
        int end = weeksStartingDays.size() - 1;
        while (start <= end) {
            int middle = start + ((end - start) / 2);
            if (weekStart.isEqual(weeksStartingDays.get(middle))) {
                return middle;
            } else if (weekStart.isAfter(weeksStartingDays.get(middle))) {
                start = middle + 1;
            } else {
                end = middle - 1;
            }
        }
        return start;
    }

    private Meeting searchNextMeeting(List<LocalDate> weeksStartList, int index, LocalDateTime now) {
        if (index >= weeksStartList.size()) {
            return null;
        }

        LocalDate startOfWeek = weeksStartList.get(index);
        List<Meeting> weekMeetings = new ArrayList<>(allMeetings.get(startOfWeek).values());
        for (Meeting meeting : weekMeetings) {
            if (meeting.getFromTime().isAfter(now)) {
                return meeting;
            }
        }

        return null;
    }

    private LocalDate getWeekStart(LocalDateTime meetingStartDate) {
        LocalDate meetingDay = meetingStartDate.toLocalDate();
        DayOfWeek firstDayOfWeek = WeekFields.of(Locale.getDefault()).getFirstDayOfWeek();
        return meetingDay.with(TemporalAdjusters.previousOrSame(firstDayOfWeek)); // start of week
    }

    @Override
    public List<Meeting> selectAllMeetings() {
        List<Meeting> meetingsList = new ArrayList<>();
        for (LocalDate startWeek : allMeetings.keySet()) {
            meetingsList.addAll(allMeetings.get(startWeek).values());
        }
        return meetingsList;
    }
}
