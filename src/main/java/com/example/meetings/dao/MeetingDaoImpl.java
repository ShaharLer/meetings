package com.example.meetings.dao;

import com.example.meetings.Exceptions.DbException;
import com.example.meetings.Exceptions.InvalidListIndexException;
import com.example.meetings.Exceptions.NullListException;
import com.example.meetings.model.Meeting;
import org.springframework.stereotype.Repository;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.TemporalAdjusters;
import java.util.*;

@Repository("meetingsDao")
public class MeetingDaoImpl implements MeetingDao {

    private final SortedMap<LocalDate, SortedMap<LocalDateTime, Meeting>> allMeetings = new TreeMap<>();
    private final Map<String, List<LocalDateTime>> titleToMeetingsStartingTimes = new HashMap<>();

    @Override
    public List<Meeting> getSameWeekMeetings(Meeting meeting) {
        LocalDate weekStartDate = getWeekStartDate(meeting.getFromTime());
        if (allMeetings.containsKey(weekStartDate)) {
            try {
                return new ArrayList<>(allMeetings.get(weekStartDate).values());
            } catch (Exception ex) {
                throw new DbException();
            }
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
        LocalDate weekStartDate = getWeekStartDate(meeting.getFromTime());
        try {
            if (!allMeetings.containsKey(weekStartDate)) {
                allMeetings.put(weekStartDate, new TreeMap<>());
            }
            allMeetings.get(weekStartDate).put(meeting.getFromTime(), meeting);
        } catch (Exception ex) {
            throw new DbException();
        }
    }

    private void addMeetingToTitlesMap(Meeting meeting) {
        String title = meeting.getMeetingTitle();
        try {
            if (!titleToMeetingsStartingTimes.containsKey(title)) {
                titleToMeetingsStartingTimes.put(title, new ArrayList<>());
            }
            titleToMeetingsStartingTimes.get(title).add(meeting.getFromTime());
        } catch (Exception ex) {
            throw new DbException();
        }
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
        LocalDate weekStart = getWeekStartDate(fromTime);
        try {
            if (allMeetings.containsKey(weekStart)) {
                Map<LocalDateTime, Meeting> weekMeetings = allMeetings.get(weekStart);
                deletedMeeting = weekMeetings.remove(fromTime);
                if (weekMeetings.isEmpty()) {
                    allMeetings.remove(weekStart);
                }
            }
            return deletedMeeting;
        } catch (Exception ex) {
            throw new DbException();
        }
    }

    private void removeMeetingKeyFromTitlesMap(Meeting meeting) {
        if (meeting != null) {
            try {
                String title = meeting.getMeetingTitle();
                titleToMeetingsStartingTimes.get(title).removeIf(fromTime -> fromTime.isEqual(meeting.getFromTime()));
                if (titleToMeetingsStartingTimes.get(title).isEmpty()) {
                    titleToMeetingsStartingTimes.remove(title);
                }
            } catch (Exception ex) {
                throw new DbException();
            }
        }
    }

    @Override
    public List<Meeting> removeMeetingByTitle(String meetingTitle) {
        List<Meeting> deletedMeetings = new ArrayList<>();
        try {
            if (titleToMeetingsStartingTimes.get(meetingTitle) != null) {
                for (LocalDateTime fromTime : titleToMeetingsStartingTimes.get(meetingTitle)) {
                    deletedMeetings.add(removeMeetingFromMeetingsMap(fromTime));
                }
                titleToMeetingsStartingTimes.remove(meetingTitle);
            }
        } catch (Exception ex) {
            throw new DbException();
        }
        return deletedMeetings;
    }

    @Override
    public Meeting getNextMeeting() {
        LocalDateTime now = LocalDateTime.now();
        List<LocalDate> weeksStartList;
        try {
           weeksStartList = new ArrayList<>(allMeetings.keySet());
        } catch (Exception ex) {
            throw new DbException();
        }
        int weekStartIndexToSearch = binarySearchOnWeekStarts(weeksStartList, getWeekStartDate(now));
        Meeting meeting = searchNextMeeting(weeksStartList, weekStartIndexToSearch, now);
        if (meeting == null) {
            meeting = searchNextMeeting(weeksStartList, weekStartIndexToSearch + 1, now);
        }
        return meeting;
    }

    private int binarySearchOnWeekStarts(List<LocalDate> weeksStartList, LocalDate weekStartDate) {
        if (weeksStartList == null) {
            throw new NullListException();
        }
        try {
            int start = 0;
            int end = weeksStartList.size() - 1;
            while (start <= end) {
                int middle = start + ((end - start) / 2);
                if (weekStartDate.isEqual(weeksStartList.get(middle))) {
                    return middle;
                } else if (weekStartDate.isAfter(weeksStartList.get(middle))) {
                    start = middle + 1;
                } else {
                    end = middle - 1;
                }
            }
            return start;
        } catch (Exception ex) {
            throw new InvalidListIndexException();
        }
    }

    private Meeting searchNextMeeting(List<LocalDate> weeksStartList, int index, LocalDateTime now) {
        if (weeksStartList == null) {
            throw new NullListException();
        }
        if (index < 0) {
            throw new InvalidListIndexException();
        }
        if (index >= weeksStartList.size()) {
            return null;
        }
        try {
            LocalDate startDateOfWeek = weeksStartList.get(index);
            List<Meeting> weekMeetings = new ArrayList<>(allMeetings.get(startDateOfWeek).values());
            for (Meeting meeting : weekMeetings) {
                if (meeting.getFromTime().isAfter(now)) {
                    return meeting;
                }
            }
            return null;
        } catch (Exception ex) {
            throw new DbException();
        }
    }

    private LocalDate getWeekStartDate(LocalDateTime date) {
        LocalDate weekStartDate = date.toLocalDate().with(TemporalAdjusters.previousOrSame(DayOfWeek.SUNDAY));
        return weekStartDate;
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
