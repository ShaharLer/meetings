package com.example.meetings.service;

import com.example.meetings.dao.MeetingDao;
import com.example.meetings.Exceptions.*;
import com.example.meetings.model.Meeting;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class MeetingService {

    private final MeetingDao meetingDao;

    @Autowired
    public MeetingService(@Qualifier("meetingsDao") MeetingDao meetingDao) {
        this.meetingDao = meetingDao;
    }

    public Meeting setMeeting(Meeting meeting) {
        verifyMeetingIsValid(meeting);
        return meetingDao.setMeeting(meeting);
    }

    private void verifyMeetingIsValid(Meeting meeting) {
        verifySingleMeetingRequirements(meeting);
        verifyScheduleRequirements(meeting);
    }

    private void verifySingleMeetingRequirements(Meeting meeting) {
        if (!MeetingValidation.isStartEndTimesValid(meeting)) {
            throw new StartEndTimesInvalidException();
        }
        if (!MeetingValidation.isMeetingDurationValid(meeting)) {
            throw new DurationException();
        }
        if (MeetingValidation.isCrossDaysMeeting(meeting)) {
            throw new CrossDaysException();
        }
        if (MeetingValidation.isMeetingOnSaturday(meeting)) {
            throw new SaturdayException();
        }
    }

    private void verifyScheduleRequirements(Meeting newMeeting) {
        LocalDateTime[] sameDayMeetingsRange = {newMeeting.getFromTime(), newMeeting.getToTime()};
        int totalWeekMeetingsInMinutes = updateTotalWeekMeetingsInMinutes(0, newMeeting);
        List<Meeting> meetingsOnTheWeekOfMeeting = meetingDao.getSameWeekMeetings(newMeeting);
        if (meetingsOnTheWeekOfMeeting == null) {
            return;
        }

        for (Meeting existedMeeting : meetingsOnTheWeekOfMeeting) {
            if (MeetingValidation.meetingsOverlap(newMeeting, existedMeeting)) {
                throw new OverlapMeetingsException();
            }
            updateMeetingsRangeOfDay(newMeeting, existedMeeting, sameDayMeetingsRange);
            totalWeekMeetingsInMinutes = updateTotalWeekMeetingsInMinutes(totalWeekMeetingsInMinutes, existedMeeting);
        }
        if (!MeetingValidation.isDailyRequirementsValid(newMeeting, sameDayMeetingsRange)) {
            throw new DailyRequirementsException();
        }
        if (!MeetingValidation.isWeeklyRequirementsValid(totalWeekMeetingsInMinutes)) {
            throw new WeeklyRequirementsException();
        }
    }

    public static int updateTotalWeekMeetingsInMinutes(int totalWeekMeetingsInMinutes, Meeting meeting) {
        return (totalWeekMeetingsInMinutes + MeetingValidation.getMeetingDurationInMinutes(meeting));
    }

    private void updateMeetingsRangeOfDay(Meeting firstMeeting, Meeting secondMeeting, LocalDateTime[] meetingsRange) {
        if (meetingsOnTheSameDay(firstMeeting, secondMeeting)) {
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

    private boolean meetingsOnTheSameDay(Meeting firstMeeting, Meeting secondMeeting) {
        LocalDate firstMeetingDate = firstMeeting.getFromTime().toLocalDate();
        LocalDate secondMeetingDate = secondMeeting.getFromTime().toLocalDate();
        return firstMeetingDate.equals(secondMeetingDate);
    }

    public Meeting removeMeetingByStartTime(LocalDateTime fromTime) {
        Meeting deletedMeeting = meetingDao.removeMeetingByStartTime(fromTime);
        if (deletedMeeting == null) {
            throw new MeetingNotFoundByTimeException(fromTime);
        }
        return deletedMeeting;
    }

    public List<Meeting> removeMeetingByTitle(String meetingTitle) {
        List<Meeting> deletedMeetings = meetingDao.removeMeetingByTitle(meetingTitle);
        if (deletedMeetings.isEmpty()) {
            throw new MeetingNotFoundByTitleException(meetingTitle);
        }
        return deletedMeetings;
    }

    public Meeting getNextMeeting() {
        return meetingDao.getNextMeeting();
    }
}
