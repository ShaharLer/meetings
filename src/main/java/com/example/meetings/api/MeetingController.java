package com.example.meetings.api;

import com.example.meetings.Responses.*;
import com.example.meetings.model.Meeting;
import com.example.meetings.model.MeetingConstants;
import com.example.meetings.service.MeetingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/meeting")
public class MeetingController {

    private final MeetingService meetingService;

    @Autowired
    public MeetingController(MeetingService meetingService) {
        this.meetingService = meetingService;
    }

    @PostMapping
    public Response setMeeting(@RequestBody Meeting meeting) {
        Meeting addedMeeting = meetingService.setMeeting(meeting);
        return new SetMeetingResponse(addedMeeting);
    }

    @DeleteMapping(value = "/fromTime/{time}")
    public Response removeMeetingByStartTime(@PathVariable("time") @DateTimeFormat(pattern = MeetingConstants.MEETING_TIME_PATTERN) LocalDateTime fromTime) {
        Meeting deletedMeeting = meetingService.removeMeetingByStartTime(fromTime);
        return new RemoveMeetingByStartTimeResponse(deletedMeeting);
    }

    @DeleteMapping(value = "/title/{meetingTitle}")
    public Response removeMeetingByTitle(@PathVariable("meetingTitle") String meetingTitle) {
        List<Meeting> deletedMeetings = meetingService.removeMeetingByTitle(meetingTitle);
        return new RemoveMeetingByTitleResponse(meetingTitle, deletedMeetings);
    }

    @GetMapping("/next")
    public Response getNextMeeting() {
        Meeting nextMeeting = meetingService.getNextMeeting();
        if (nextMeeting == null) {
            return new NextMeetingNotFoundResponse();
        }
        return new FoundNextMeetingResponse(nextMeeting);
    }
}
