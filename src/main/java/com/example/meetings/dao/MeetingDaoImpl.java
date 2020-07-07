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

    /**
     * Looks in the allMeetings map for the meetings that occur in the same week
     * as the given meeting.
     *
     * @param meeting A meeting object
     * @return a list with the meetings that occur in the same week as the given meeting
     * @throws DbException if fails to read from the allMeetings map
     */
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

    /**
     * Adds the given meeting to the DB.
     * It does that by first inserting the meeting to the allMeetings map.
     * Then it inserts the start time (fromTime) of the meeting to the map
     * that maps meeting titles to meetings start times.
     *
     * @param meeting The meeting object to insert to the DB
     * @return The meeting object that was inserted to the DB
     * @throws DbException if fails ot read or write from the DB
     */
    @Override
    public Meeting setMeeting(Meeting meeting) {
        addMeetingToMeetingsMap(meeting);
        addMeetingToTitlesMap(meeting);
        return meeting;
    }

    /**
     * Adds the meeting object to the allMeetings map.
     * If there is no meeting in the same week, creates a new key for the
     * start of the meeting week.
     *
     * @param meeting The new meeting object to insert
     * @throws DbException if fails to read or write from the allMeetings map
     */
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

    /**
     * Adds the start time of the meeting object to the
     * titleToMeetingsStartingTimes map.
     * If there is no meeting with the same title, creates a new key for the
     * title of the meeting.
     *
     * @param meeting The new meeting object to insert
     * @throws DbException if fails ot read or write from the titleToMeetingsStartingTimes map
     */
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

    /**
     * Deletes the meeting in the memory that starts at the given date and time.
     * It does so by calling the helper functions that delete the meeting from
     * the allMeetings map, and the meeting start time from the
     * titleToMeetingsStartingTimes map.
     *
     * @param fromTime The start date of the meeting to delete from the memory
     * @return The meeting object that was deleted from the memory
     */
    @Override
    public Meeting removeMeetingByStartTime(LocalDateTime fromTime) {
        Meeting removedMeeting = removeMeetingFromMeetingsMap(fromTime);
        if (removedMeeting != null) {
            removeMeetingKeyFromTitlesMap(removedMeeting);
        }
        return removedMeeting;
    }

    /**
     * Looks for a meeting in the allMeetings map that start at the given fromTime,
     * and if finds it - deletes it from the map.
     * If after deletion, that are no more meetings in that week, deleted the
     * key that represents the start of the meeting week.
     *
     * @param fromTime The start date of the meeting to delete from the memory
     * @return The meeting object that was deleted from the memory
     * @throws DbException if fails ot read or write from the titleToMeetingsStartingTimes map
     */
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

    /**
     * Deletes from titleToMeetingsStartingTimes the start times of given meeting.
     * If after deletion, that are no more meetings with the same title in memory,
     * deletes the meeting title key.
     *
     * @param meeting The new meeting object to delete
     * @throws DbException if fails ot read or write from the allMeetings map
     */
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

    /**
     * Deletes the key (and all it values) with the given meeting title from the
     * titleToMeetingsStartingTimes map.
     * Every meeting that has this meeting title, is delete also from the akkMeetings map,
     * with a call to a helper function.
     *
     * @param meetingTitle The title of the meeting to delete form the DB
     * @return A list of all the meetings with the given title, that were deleted from
     *         the titleToMeetingsStartingTimes map.
     * @throws DbException if fails ot read or write from the titleToMeetingsStartingTimes map
     */
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

    /**
     * Looks for the next upcoming meeting.
     * It does so by first calling a helper function that finds the start of the relevant week:
     * Either the current week, if there are meetings this week, or the closest start of week with
     * meetings in the future.
     *
     * @return The next upcoming meeting
     * @throws DbException if fails ot read the allMeetings map
     */
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

    /**
     * The method executes a binary search on the given list.
     * It looks for the given weekStartDate. If cannot find it, returns the index of the date which
     * is the closest in the future (i.e. bigger than weekStartDate).
     * If all the dates are smaller than weekStartDate, the returned value will eventually be the list size.
     *
     * @param weeksStartList A list with the start dates of weeks with meetings in the memory
     * @param weekStartDate The start date to search for in the given list
     * @return The index of the weekStartDate in the list (or the index with the start date which is
     *         the closest in the future).
     * @throws NullListException if the list is empty
     * @throws InvalidListIndexException if an error occurs during the search in the list
     */
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

    /**
     * Iterates over the meetings of the relevant week that have the next meeting.
     * When a meeting that occurs after now is found - returns it.
     *
     * @param weeksStartList A list with the start dates of the weeks that have meetings in the memory
     * @param index The index to look for in the given list
     * @param now The current date and time
     * @return The next meeting compare to now
     * @throws NullListException if the list is empty
     * @throws InvalidListIndexException if an the given index is negative
     * @throws DbException if fails ot read the allMeetings map
     */
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

    /**
     * Given a date, returns the start of the week of this date.
     *
     * @param date A date
     * @return The start of the week of the given date
     */
    private LocalDate getWeekStartDate(LocalDateTime date) {
        LocalDate weekStartDate = date.toLocalDate().with(TemporalAdjusters.previousOrSame(DayOfWeek.SUNDAY));
        return weekStartDate;
    }
}
