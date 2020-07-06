package com.example.meetings.service;

import com.example.meetings.dao.MeetingDao;
import com.example.meetings.model.Exceptions.*;
import com.example.meetings.model.Meeting;
import com.example.meetings.model.MeetingConstants;
import com.example.meetings.model.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.time.Duration;
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
        if (meetingDao.setMeeting(meeting)) {
            return meeting;
        };
        return null;
    }

    private void verifyMeetingIsValid(Meeting meeting) {
        verifySingleMeetingRequirements(meeting);
        verifyScheduleRequirements(meeting);
    }

    private void verifySingleMeetingRequirements(Meeting meeting) {
        if (!Utils.isStartEndTimesValid(meeting)) {
            throw new StartEndTimesInvalidException();
        }
        if (!Utils.isMeetingDurationValid(meeting)) {
            throw new DurationException();
        }
        if (Utils.isCrossDaysMeeting(meeting)) {
            throw new CrossDaysException();
        }
        if (Utils.isMeetingOnSaturday(meeting)) {
            throw new SaturdayException();
        }
    }

    private void verifyScheduleRequirements(Meeting newMeeting) {
        LocalDateTime[] sameDayMeetingsRange = {newMeeting.getFromTime(), newMeeting.getToTime()};
        int totalWeekMeetingsInMinutes = Utils.updateTotalWeekMeetingsInMinutes(0, newMeeting);
        List<Meeting> meetingsOfTheWeek = meetingDao.getWeeklyMeetings(newMeeting);
        if (meetingsOfTheWeek == null) {
            return;
        }

        for (Meeting existedMeeting : meetingsOfTheWeek) {
            if (meetingsOverlap(newMeeting, existedMeeting)) {
                throw new OverlapMeetingsException();
            }
            updateMeetingsRangeOfDay(newMeeting, existedMeeting, sameDayMeetingsRange);
            totalWeekMeetingsInMinutes =
                    Utils.updateTotalWeekMeetingsInMinutes(totalWeekMeetingsInMinutes, existedMeeting);
        }
        if (!isDailyRequirementsValid(newMeeting, sameDayMeetingsRange)) {
            throw new DailyRequirementsException();
        }
        if (!isWeeklyRequirementsValid(totalWeekMeetingsInMinutes)) {
            throw new WeeklyRequirementsException();
        }
    }

    private boolean meetingsOverlap(Meeting newMeeting, Meeting existedMeeting) {
        return (newMeeting.getFromTime().isBefore(existedMeeting.getToTime()) &&
                existedMeeting.getFromTime().isBefore(newMeeting.getToTime()));
    }

    private void updateMeetingsRangeOfDay(Meeting firstMeeting, Meeting secondMeeting, LocalDateTime[] meetingsRange) {
        if (Utils.meetingsOnTheSameDay(firstMeeting, secondMeeting)) {
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

    private boolean isDailyRequirementsValid(Meeting meeting, LocalDateTime[] sameDayMeetingsRange) {
        int diffFromFirstMeetingStart = (int) Math.abs(Duration.between(meeting.getToTime(), sameDayMeetingsRange[0]).toMinutes());
        int diffFromLastMeetingEnd = (int) Math.abs(Duration.between(meeting.getFromTime(), sameDayMeetingsRange[1]).toMinutes());
        int maximumDailyMeetingsMinutes = MeetingConstants.getMaximumDailyMeetingsHoursDiff() * 60;
        return ((diffFromFirstMeetingStart <= maximumDailyMeetingsMinutes) && (diffFromLastMeetingEnd <= maximumDailyMeetingsMinutes));
    }

    private boolean isWeeklyRequirementsValid(int totalWeekMeetingsHours) {
        return (totalWeekMeetingsHours <= MeetingConstants.getMaximumWeeklyMeetingsHours() * 60);
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

    /*
    public List<Meeting> getAllMeetings() {
        return meetingDao.selectAllMeetings();
    }

     */
}
