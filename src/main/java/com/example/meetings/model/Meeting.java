package com.example.meetings.model;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;

public class Meeting {

    @JsonFormat(pattern = MeetingConstants.MEETING_TIME_PATTERN)
    private LocalDateTime fromTime;
    @JsonFormat(pattern = MeetingConstants.MEETING_TIME_PATTERN)
    private LocalDateTime toTime;
    private String meetingTitle;

    public Meeting() {
    }

    public Meeting(LocalDateTime fromTime, LocalDateTime toTime, String meetingTitle) {
        this.fromTime = fromTime;
        this.toTime = toTime;
        this.meetingTitle = meetingTitle;
    }

    public LocalDateTime getFromTime() {
        return fromTime;
    }

    public void setFromTime(LocalDateTime fromTime) {
        this.fromTime = fromTime;
    }

    public LocalDateTime getToTime() {
        return toTime;
    }

    public void setToTime(LocalDateTime toTime) {
        this.toTime = toTime;
    }

    public String getMeetingTitle() {
        return meetingTitle;
    }

    public void setMeetingTitle(String meetingTitle) {
        this.meetingTitle = meetingTitle;
    }
}
