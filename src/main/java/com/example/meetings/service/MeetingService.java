package com.example.meetings.service;

import com.example.meetings.dao.MeetingDao;
import com.example.meetings.model.Meeting;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class MeetingService {

    private final MeetingDao meetingDao;

    @Autowired
    public MeetingService(@Qualifier("meetingDao") MeetingDao meetingDao) {
        this.meetingDao = meetingDao;
    }

    public Meeting setMeeting(Meeting meeting) {
        return meetingDao.setMeeting(meeting);
    }

    public Meeting removeMeetingByStartTime(LocalDateTime fromTime) {
        Meeting deletedMeeting = meetingDao.removeMeetingByStartTime(fromTime);
        return deletedMeeting;
    }

    public List<Meeting> removeMeetingByTitle(String meetingTitle) {
        List<Meeting> deletedMeetings = meetingDao.removeMeetingByTitle(meetingTitle);
        return deletedMeetings;
    }

    public List<Meeting> getAllMeetings() {
        return meetingDao.selectAllMeetings();
    }

    public Meeting getNextMeeting() {
        Meeting meeting = meetingDao.getNextMeeting();
        return meeting;
    }
}
