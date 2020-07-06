package com.example.meetings.dao;

import com.example.meetings.model.Meeting;
import com.example.meetings.model.Utils;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@Repository("meetingsDao")
public class MeetingDaoImpl implements MeetingDao {

    private final SortedMap<LocalDate, SortedMap<LocalDateTime, Meeting>> allMeetings = new TreeMap<>();
    private final Map<String, List<LocalDateTime>> titleToMeetingsStartingTimes = new HashMap<>();

    @Override
    public List<Meeting> getWeeklyMeetings(Meeting meeting) {
        LocalDate weekStartDate = Utils.getWeekStartDate(meeting.getFromTime());
        if (allMeetings.containsKey(weekStartDate)) {
            return new ArrayList<>(allMeetings.get(weekStartDate).values());
        }
        return null;
    }

    @Override
    public boolean setMeeting(Meeting meeting) {
        addMeetingToMeetingsMap(meeting);
        addMeetingToTitlesMap(meeting);
        return true;
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
    public Meeting removeMeetingByStartTime(LocalDateTime fromTime) {
        Meeting removedMeeting = removeMeetingFromMeetingsMap(fromTime);
        if (removedMeeting != null) {
            removeMeetingKeyFromTitlesMap(removedMeeting);
        }
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
    public List<Meeting> removeMeetingByTitle(String meetingTitle) {
        List<Meeting> deletedMeetings = new ArrayList<>();
        if (titleToMeetingsStartingTimes.get(meetingTitle) != null) {
            for (LocalDateTime fromTime : titleToMeetingsStartingTimes.get(meetingTitle)) {
                deletedMeetings.add(removeMeetingFromMeetingsMap(fromTime));
            }
            titleToMeetingsStartingTimes.remove(meetingTitle);
        }
        return deletedMeetings;
    }

    @Override
    public Meeting getNextMeeting() {
        LocalDateTime now = LocalDateTime.now();
        List<LocalDate> weeksStartList = new ArrayList<>(allMeetings.keySet());
        int weekStartIndexToSearch = binarySearchOnWeekStarts(weeksStartList, Utils.getWeekStartDate(now));
        Meeting meeting = searchNextMeeting(weeksStartList, weekStartIndexToSearch, now);
        if (meeting == null) {
            meeting = searchNextMeeting(weeksStartList, weekStartIndexToSearch + 1, now);
        }
        return meeting;
    }

    public static int binarySearchOnWeekStarts(List<LocalDate> weeksStartingDays, LocalDate weekStartDate) {
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
        return start;
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

    /*
    @Override
    public List<Meeting> selectAllMeetings() {
        List<Meeting> meetingsList = new ArrayList<>();
        for (LocalDate startWeek : allMeetings.keySet()) {
            meetingsList.addAll(allMeetings.get(startWeek).values());
        }
        return meetingsList;
    }

     */
}
