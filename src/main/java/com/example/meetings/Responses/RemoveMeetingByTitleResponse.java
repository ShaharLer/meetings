package com.example.meetings.Responses;

import com.example.meetings.model.Meeting;

import java.util.List;

public class RemoveMeetingByTitleResponse extends Response {

    public static final String DELETED_MEETINGS_BY_TITLE_MESSAGE = "The meetings with title '%s' were deleted successfully";
    private List<Meeting> deletedMeetings;

    public RemoveMeetingByTitleResponse(String meetingsTitle, List<Meeting> deletedMeetings) {
        super(String.format(DELETED_MEETINGS_BY_TITLE_MESSAGE, meetingsTitle));
        this.deletedMeetings = deletedMeetings;
    }

    public List<Meeting> getDeletedMeetings() {
        return deletedMeetings;
    }

    public void setDeletedMeetings(List<Meeting> deletedMeetings) {
        this.deletedMeetings = deletedMeetings;
    }
}
