package com.example.meetings.Responses;

import com.example.meetings.model.Meeting;

public class FoundNextMeetingResponse extends Response {

    private static final String FOUND_NEXT_MEETING_MESSAGE = "The next meeting is:";
    private Meeting meeting;

    public FoundNextMeetingResponse(Meeting meeting) {
        super(FOUND_NEXT_MEETING_MESSAGE);
        this.meeting = meeting;
    }

    public Meeting getMeeting() {
        return meeting;
    }

    public void setMeeting(Meeting meeting) {
        this.meeting = meeting;
    }
}
