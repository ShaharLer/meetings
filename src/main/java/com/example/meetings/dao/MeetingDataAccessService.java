package com.example.meetings.dao;

import com.example.meetings.model.Meeting;
import org.springframework.stereotype.Repository;

import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.TemporalAdjusters;
import java.util.*;

@Repository("meetingsDao")
public class MeetingDataAccessService implements MeetingDao {

    private static final int MINIMUM_MEETING_TIME = 15; // TODO check how to use static folder
    private static final int MAXIMUM_MEETING_TIME = 120; // TODO check how to use static folder
    private static final int MAXIMUM_DAILY_MEETINGS_HOURS_DIFF = 10; // TODO check how to use static folder
    private static final int MAXIMUM_WEEKLY_MEETINGS_HOURS = 40; // TODO check how to use static folder
    private final SortedMap<LocalDate, SortedMap<LocalDateTime, Meeting>> allMeetings = new TreeMap<>();
    private final Map<String, List<LocalDateTime>> titleToMeetingsStartingTimes = new HashMap<>();

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
        LocalDateTime fromTime = meeting.getFromTime();
        LocalDateTime toTime = meeting.getToTime();
        int meetingDurationInMinutes = getMeetingDurationInMinutes(meeting);

        // Check that start time is not after end time
        if (fromTime.isAfter(toTime)) {
            return false;
        }
        //  Requirement (1): A meeting cannot last for more than 2 hours, and for less than 15 minutes
        if (meetingDurationInMinutes > MAXIMUM_MEETING_TIME || meetingDurationInMinutes < MINIMUM_MEETING_TIME) {
            return false;
        }
        // Assumption: No cross-days meetings
        if (fromTime.getDayOfWeek() != toTime.getDayOfWeek()) {
            return false;
        }
        // Requirement (3): No meetings on Saturdays
        if (fromTime.getDayOfWeek() == DayOfWeek.SATURDAY) {
            return false;
        }
        return true;
    }

    private boolean isScheduleRequirementsValid(Meeting newMeeting) {
        LocalDateTime[] sameDayMeetingsRange = {newMeeting.getFromTime(), newMeeting.getToTime()};
        LocalDate weekStartDate = getWeekStartDate(newMeeting.getFromTime());
        int totalWeekMeetingsInMinutes = updateTotalWeekMeetingsInMinutes(0, newMeeting);

        if (allMeetings.containsKey(weekStartDate)) {
            List<Meeting> meetingsOfTheWeek = new ArrayList<>(allMeetings.get(weekStartDate).values());
            for (Meeting existedMeeting : meetingsOfTheWeek) { // Run on the weekly meetings
                // Requirement (2): Two meetings cannot overlap
                if (meetingsOverlap(newMeeting, existedMeeting)) {
                    return false;
                }
                updateSameDayMeetingsRange(newMeeting, existedMeeting, sameDayMeetingsRange);
                totalWeekMeetingsInMinutes = updateTotalWeekMeetingsInMinutes(totalWeekMeetingsInMinutes, existedMeeting);
            }
            if (!isDailyRequirementsValid(newMeeting, sameDayMeetingsRange)) {
                return false;
            }
            if (!isWeeklyRequirementsValid(totalWeekMeetingsInMinutes)) {
                return false;
            }
        }
        return true;
    }

    private LocalDate getWeekStartDate(LocalDateTime meetingStart) {
        LocalDate weekStartDate = meetingStart.toLocalDate().with(TemporalAdjusters.previousOrSame(DayOfWeek.SUNDAY));
        return weekStartDate;
    }

    private int updateTotalWeekMeetingsInMinutes(int totalWeekMeetingsInMinutes, Meeting meeting) {
        return (totalWeekMeetingsInMinutes + getMeetingDurationInMinutes(meeting));
    }

    private int getMeetingDurationInMinutes(Meeting meeting) {
        return (int)Duration.between(meeting.getFromTime(), meeting.getToTime()).toMinutes();
    }

    private boolean meetingsOverlap(Meeting newMeeting, Meeting existedMeeting) {
        return (newMeeting.getFromTime().isBefore(existedMeeting.getToTime()) &&
                existedMeeting.getFromTime().isBefore(newMeeting.getToTime()));
    }

    private void updateSameDayMeetingsRange(Meeting newMeeting, Meeting existedMeeting, LocalDateTime[] sameDayMeetingsRange) {
        LocalDate newMeetingDate = newMeeting.getFromTime().toLocalDate();
        LocalDate existedMeetingDate = existedMeeting.getFromTime().toLocalDate();

        if (newMeetingDate.equals(existedMeetingDate)) {
            LocalDateTime fromTime = existedMeeting.getFromTime();
            LocalDateTime toTime = existedMeeting.getToTime();

            if (fromTime.isBefore(sameDayMeetingsRange[0])) {
                sameDayMeetingsRange[0] = fromTime;
            }
            if (toTime.isAfter(sameDayMeetingsRange[1])) {
                sameDayMeetingsRange[1] = toTime;
            }
        }
    }

    private boolean isDailyRequirementsValid(Meeting meeting, LocalDateTime[] sameDayMeetingsRange) {
        // Requirement (6): The last meeting on the same day must end up to 10 hours after the first meeting started
        int diffFromFirstMeetingStart = (int) Math.abs(Duration.between(meeting.getToTime(), sameDayMeetingsRange[0]).toMinutes());
        int diffFromLastMeetingEnd = (int) Math.abs(Duration.between(meeting.getFromTime(), sameDayMeetingsRange[1]).toMinutes());
        int maximumDailyMeetingsMinutes = MAXIMUM_DAILY_MEETINGS_HOURS_DIFF * 60;
        return ((diffFromFirstMeetingStart <= maximumDailyMeetingsMinutes) &&
                (diffFromLastMeetingEnd <= maximumDailyMeetingsMinutes));
    }

    private boolean isWeeklyRequirementsValid(int totalWeekMeetingsHours) {
        // Requirement (5): There are up to 40 working hours a week (Sunday to Friday)
        return (totalWeekMeetingsHours <= MAXIMUM_WEEKLY_MEETINGS_HOURS * 60);
    }

    private void addMeeting(Meeting meeting) {
        addMeetingToMeetingsMap(meeting);
        addMeetingToTitlesMap(meeting);
    }

    private void addMeetingToMeetingsMap(Meeting meeting) {
        LocalDate weekStartDate = getWeekStartDate(meeting.getFromTime());
        if (!allMeetings.containsKey(weekStartDate)) {
            allMeetings.put(weekStartDate, new TreeMap<>());
        }
        allMeetings.get(weekStartDate).put(meeting.getFromTime(), meeting);
    }

    private void addMeetingToTitlesMap(Meeting meeting) {
        String title = meeting.getMeetingTitle();
        if (!titleToMeetingsStartingTimes.containsKey(title)) {
            titleToMeetingsStartingTimes.put(title, new ArrayList<>());
        }
        titleToMeetingsStartingTimes.get(title).add(meeting.getFromTime());
    }

    @Override
    public Meeting removeMeetingByStartTime(LocalDateTime fromTime) {
        Meeting meetingToRemove = removeMeetingFromMeetingsMap(fromTime);
        removeMeetingKeyFromTitlesMap(meetingToRemove);
        return meetingToRemove;
    }

    @Override
    public List<Meeting> removeMeetingByTitle(String meetingTitle) {
        List<Meeting> deletedMeetings = new ArrayList<>();
        if (titleToMeetingsStartingTimes.get(meetingTitle) != null) {
            for (LocalDateTime fromTime : titleToMeetingsStartingTimes.get(meetingTitle)) {
                Meeting deletedMeeting = removeMeetingFromMeetingsMap(fromTime);
                deletedMeetings.add(deletedMeeting);
            }
            titleToMeetingsStartingTimes.remove(meetingTitle);
        }
        return deletedMeetings;
    }

    private Meeting removeMeetingFromMeetingsMap(LocalDateTime fromTime) {
        Meeting deletedMeeting = null;
        LocalDate weekStart = getWeekStartDate(fromTime);
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
        if (meeting != null) {
            String title = meeting.getMeetingTitle();
            titleToMeetingsStartingTimes.get(title).removeIf(fromTime -> fromTime.isEqual(meeting.getFromTime()));
            if (titleToMeetingsStartingTimes.get(title).isEmpty()) {
                titleToMeetingsStartingTimes.remove(title);
            }
        }
    }

    @Override
    public Meeting getNextMeeting() {
        LocalDateTime now = LocalDateTime.now();
        List<LocalDate> weeksStartList = new ArrayList<>(allMeetings.keySet());
        int weekStartIndexToSearch = weeksBinarySearch(weeksStartList, getWeekStartDate(now));
        Meeting meeting = searchNextMeeting(weeksStartList, weekStartIndexToSearch, now);
        if (meeting == null) {
            meeting = searchNextMeeting(weeksStartList, weekStartIndexToSearch + 1, now);
        }
        return meeting;
    }

    private int weeksBinarySearch(List<LocalDate> weeksStartingDays, LocalDate weekStartDate) {
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

    private Meeting searchNextMeeting(List<LocalDate> weeksStartList, int index, LocalDateTime now) {
        if (index < weeksStartList.size()) {
            LocalDate startDateOfWeek = weeksStartList.get(index);
            List<Meeting> weekMeetings = new ArrayList<>(allMeetings.get(startDateOfWeek).values());
            for (Meeting meeting : weekMeetings) {
                if (meeting.getFromTime().isAfter(now)) {
                    return meeting;
                }
            }
        }
        return null;
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
