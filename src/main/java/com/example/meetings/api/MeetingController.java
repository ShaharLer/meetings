package com.example.meetings.api;

import com.example.meetings.model.Exceptions.MeetingInvalidException;
import com.example.meetings.model.Exceptions.MeetingNotFoundByTimeException;
import com.example.meetings.model.Exceptions.MeetingNotFoundByTitleException;
import com.example.meetings.model.Meeting;
import com.example.meetings.model.MeetingConstants;
import com.example.meetings.model.Responses.*;
import com.example.meetings.service.MeetingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;

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
        try {
            Meeting addedMeeting = meetingService.setMeeting(meeting);
            return new SetMeetingResponse(addedMeeting);
        } catch (MeetingInvalidException ex) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ex.getMessage());
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @DeleteMapping(value = "/fromTime/{time}")
    public Response removeMeetingByStartTime(@PathVariable("time") @DateTimeFormat(pattern = MeetingConstants.MEETING_TIME_PATTERN) LocalDateTime fromTime) {
        try {
            return new RemoveMeetingByStartTimeResponse(meetingService.removeMeetingByStartTime(fromTime));
        } catch (MeetingNotFoundByTimeException ex) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ex.getMessage());
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @DeleteMapping(value = "/title/{meetingTitle}")
    public Response removeMeetingByTitle(@PathVariable("meetingTitle") String meetingTitle) {
        try {
            return new RemoveMeetingByTitleResponse(meetingTitle, meetingService.removeMeetingByTitle(meetingTitle));
        } catch (MeetingNotFoundByTitleException ex) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ex.getMessage());
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @GetMapping("/next")
    public Response getNextMeeting() {
        Meeting nextMeeting = meetingService.getNextMeeting();
        if (nextMeeting != null) {
            return new FoundNextMeetingResponse(nextMeeting);
        } else {
            return new NextMeetingNotFoundResponse();
        }
    }

    /*
    @GetMapping("/all")
    public List<Meeting> getAllMeetings() {
        List<Meeting> allMeetings = meetingService.getAllMeetings();
        return allMeetings;
    }

     */
}
