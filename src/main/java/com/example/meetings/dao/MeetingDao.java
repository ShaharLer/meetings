package com.example.meetings.dao;

import com.example.meetings.model.Meeting;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

public interface MeetingDao {

   Meeting setMeeting(Meeting meeting);

   Meeting removeMeetingByStartTime(LocalDateTime fromTime);

   List<Meeting> removeMeetingByTitle(String meetingTitle);

   Meeting getNextMeeting();

   List<Meeting> selectAllMeetings();
}
