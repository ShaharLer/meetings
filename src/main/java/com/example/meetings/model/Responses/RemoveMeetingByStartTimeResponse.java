package com.example.meetings.model.Responses;

import com.example.meetings.model.MeetingConstants;
import com.example.meetings.model.Meeting;

import java.time.format.DateTimeFormatter;

public class RemoveMeetingByStartTimeResponse extends Response {

    private static final String DELETED_MEETING_BY_FROM_TIME_MESSAGE = "The meeting that starts at (%s) was deleted successfully";
    private Meeting deletedMeeting;

    public RemoveMeetingByStartTimeResponse(Meeting deletedMeeting) {
        super(String.format(DELETED_MEETING_BY_FROM_TIME_MESSAGE,
                deletedMeeting.getFromTime().format(DateTimeFormatter.ofPattern(MeetingConstants.getMeetingTimePattern()))));
        this.deletedMeeting = deletedMeeting;
    }

    public Meeting getDeletedMeeting() {
        return deletedMeeting;
    }

    public void setDeletedMeeting(Meeting addedMeeting) {
        this.deletedMeeting = addedMeeting;
    }

}
