package com.example.meetings.Responses;

import com.example.meetings.model.Meeting;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.CREATED)
public class SetMeetingResponse extends Response {

    private static final String ADDED_MEETING_MESSAGE = "Meeting was set successfully";
    private Meeting meeting;

    public SetMeetingResponse(Meeting meeting) {
        super(ADDED_MEETING_MESSAGE);
        this.meeting = meeting;
    }

    public Meeting getMeeting() {
        return meeting;
    }

    public void setMeeting(Meeting meeting) {
        this.meeting = meeting;
    }
}
