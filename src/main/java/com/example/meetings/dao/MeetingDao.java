package com.example.meetings.dao;

import com.example.meetings.Exceptions.DbException;
import com.example.meetings.Exceptions.VariableInvalidException;
import com.example.meetings.model.Meeting;

import java.time.LocalDateTime;
import java.util.List;

public interface MeetingDao {

   /**
    * Returns a list with the meetings that occur in the same week
    * as the given meeting.
    * If there are no meetings that satisfy this, returns null;
    *
    * @param meeting A meeting object
    * @return a list with the meetings that occur in the same week
    * as the given meeting
    * @throws DbException if fails to read from the DB
    */
   List<Meeting> getSameWeekMeetings(Meeting meeting);

   /**
    * Adds the given meeting to the DB.
    * Returns the inserted meeting object if succeeded, otherwise returns null.
    *
    * @param meeting The meeting object to insert to the DB
    * @return The meeting object that was inserted to the DB
    * @throws DbException if fails ot read or write from the DB
    */
   Meeting setMeeting(Meeting meeting);

   /**
    * Looks for a meeting in the DB that starts at the given date
    * and time. If such a meeting is found, deletes it from the DB.
    * If such a meeting is not found, returns null.
    *
    * @param fromTime The start date of the meeting to delete from the DB
    * @return The deleted meeting from the DB
    * @throws DbException if fails ot read or write from the DB
    */
   Meeting removeMeetingByStartTime(LocalDateTime fromTime);

   /**
    * Looks for all the meetings in the DB that have the given title.
    * As meeting title is not unique, all the meetings that have this
    * title will be deleted.
    *
    * @param meetingTitle The title of the meeting to delete form the DB
    * @return A list of all the meetings with the given title, that were
    *         deleted from the DB.
    * @throws DbException if fails ot read or write from the DB
    */
   List<Meeting> removeMeetingByTitle(String meetingTitle);

   /**
    * Returns the next upcoming meeting.
    * If there is no upcoming meeting, returns null.
    *
    * @return The next upcoming meeting
    * @throws DbException if fails ot read or write from the DB
    * @throws VariableInvalidException if reads a null object from the DB
    */
   Meeting getNextMeeting();
}
