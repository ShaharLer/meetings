package com.example.meetings.dao;

import com.example.meetings.model.Exceptions.*;
import com.example.meetings.model.Meeting;
import com.example.meetings.model.Utils;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@Repository("meetingsDao")
public class MeetingDataAccessService implements MeetingDao {

    private final SortedMap<LocalDate, SortedMap<LocalDateTime, Meeting>> allMeetings = new TreeMap<>();
    private final Map<String, List<LocalDateTime>> titleToMeetingsStartingTimes = new HashMap<>();

    @Override
    public Meeting setMeeting(Meeting meeting) {
        verifyMeetingIsValid(meeting);
        addMeeting(meeting);
        return meeting;
    }

    private void verifyMeetingIsValid(Meeting meeting) {
        verifySingleMeetingRequirements(meeting);
        verifyScheduleRequirements(meeting);
    }

    private void verifySingleMeetingRequirements(Meeting meeting)
            throws StartEndTimesInvalidException, DurationException, CrossDaysException, SaturdayException {

        // Check that start time is not after end time
        if (!Utils.isStartEndTimesValid(meeting)) {
            throw new StartEndTimesInvalidException();
        }
        //  Requirement (1)
        if (!Utils.isMeetingDurationValid(meeting)) {
            throw new DurationException();
        }
        // Assumption: No cross-days meetings
        if (Utils.isCrossDaysMeeting(meeting)) {
            throw new CrossDaysException();
        }
        // Requirement (3)
        if (Utils.isMeetingOnSaturday(meeting)) {
            throw new SaturdayException();
        }
    }

    private void verifyScheduleRequirements(Meeting newMeeting)
            throws OverlapMeetingsException, DailyRequirementsException, WeeklyRequirementsException {

        LocalDateTime[] sameDayMeetingsRange = {newMeeting.getFromTime(), newMeeting.getToTime()};
        LocalDate weekStartDate = Utils.getWeekStartDate(newMeeting.getFromTime());
        int totalWeekMeetingsInMinutes = Utils.updateTotalWeekMeetingsInMinutes(0, newMeeting);

        if (allMeetings.containsKey(weekStartDate)) {
            List<Meeting> meetingsOfTheWeek = new ArrayList<>(allMeetings.get(weekStartDate).values());
            for (Meeting existedMeeting : meetingsOfTheWeek) { // Run on the weekly meetings
                // Requirement (2): Two meetings cannot overlap
                if (Utils.meetingsOverlap(newMeeting, existedMeeting)) {
                    throw new OverlapMeetingsException();
                }
                Utils.updateMeetingsRangeOfDay(newMeeting, existedMeeting, sameDayMeetingsRange);
                totalWeekMeetingsInMinutes =
                        Utils.updateTotalWeekMeetingsInMinutes(totalWeekMeetingsInMinutes, existedMeeting);
            }
            if (!Utils.isDailyRequirementsValid(newMeeting, sameDayMeetingsRange)) {
                throw new DailyRequirementsException();
            }
            if (!Utils.isWeeklyRequirementsValid(totalWeekMeetingsInMinutes)) {
                throw new WeeklyRequirementsException();
            }
        }
    }

    private void addMeeting(Meeting meeting) {
        addMeetingToMeetingsMap(meeting);
        addMeetingToTitlesMap(meeting);
    }

    private void addMeetingToMeetingsMap(Meeting meeting) {
        LocalDate weekStartDate = Utils.getWeekStartDate(meeting.getFromTime());
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
    public Meeting removeMeetingByStartTime(LocalDateTime fromTime) throws MeetingNotFoundByTimeException {
        Meeting removedMeeting = removeMeetingFromMeetingsMap(fromTime);
        if (removedMeeting == null) {
            throw new MeetingNotFoundByTimeException(fromTime);
        }
        removeMeetingKeyFromTitlesMap(removedMeeting);
        return removedMeeting;
    }

    private Meeting removeMeetingFromMeetingsMap(LocalDateTime fromTime) {
        Meeting deletedMeeting = null;
        LocalDate weekStart = Utils.getWeekStartDate(fromTime);
        if (allMeetings.containsKey(weekStart)) {
            Map<LocalDateTime, Meeting> weekMeetings = allMeetings.get(weekStart);
            deletedMeeting = weekMeetings.remove(fromTime);
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
    public List<Meeting> removeMeetingByTitle(String meetingTitle) throws MeetingNotFoundByTitleException {
        List<Meeting> deletedMeetings = new ArrayList<>();
        if (titleToMeetingsStartingTimes.get(meetingTitle) != null) {
            for (LocalDateTime fromTime : titleToMeetingsStartingTimes.get(meetingTitle)) {
                deletedMeetings.add(removeMeetingFromMeetingsMap(fromTime));
            }
            titleToMeetingsStartingTimes.remove(meetingTitle);
        }
        if (deletedMeetings.isEmpty()) {
            throw new MeetingNotFoundByTitleException(meetingTitle);
        }
        return deletedMeetings;
    }

    @Override
    public Meeting getNextMeeting() {
        LocalDateTime now = LocalDateTime.now();
        List<LocalDate> weeksStartList = new ArrayList<>(allMeetings.keySet());
        int weekStartIndexToSearch = Utils.weeksBinarySearch(weeksStartList, Utils.getWeekStartDate(now));
        Meeting meeting = searchNextMeeting(weeksStartList, weekStartIndexToSearch, now);
        if (meeting == null) {
            meeting = searchNextMeeting(weeksStartList, weekStartIndexToSearch + 1, now);
        }
        return meeting;
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
