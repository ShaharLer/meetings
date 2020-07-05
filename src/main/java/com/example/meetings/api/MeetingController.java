package com.example.meetings.api;

import com.example.meetings.model.*;
import com.example.meetings.service.MeetingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/meetings")
public class MeetingController {

    private final MeetingService meetingService;

    @Autowired
    public MeetingController(MeetingService meetingService) {
        this.meetingService = meetingService;
    }

    @PostMapping
    public Response setMeeting(@RequestBody Meeting meeting) {
        Meeting insertedMeeting = meetingService.setMeeting(meeting);
        String message = insertedMeeting == null ? "Failed to add the following meeting:"
                                                 : "Successfully inserted the following meeting:";
        return new ResponseSetMeeting(message, meeting);
    }

    @DeleteMapping(value = "/fromTime/{time}")
        public Response removeMeetingByStartTime(@PathVariable("time") @DateTimeFormat(pattern="dd-MM-yyyy HH:mm") LocalDateTime fromTime) {
        Meeting deletedMeeting = meetingService.removeMeetingByStartTime(fromTime);
        String message = deletedMeeting == null ? "Failed to delete a meeting that starts at " + fromTime
                                                : "Successfully deleted the following meeting:";
        return new ResponseRemoveMeetingByStartTime(message, deletedMeeting);
    }

    @DeleteMapping(value = "/title/{meetingTitle}")
    public Response removeMeetingByTitle(@PathVariable("meetingTitle") String meetingTitle) {
        List<Meeting> deletedMeeting = meetingService.removeMeetingByTitle(meetingTitle);
        String message = deletedMeeting.isEmpty() ? ("Failed to delete meetings with title: " + meetingTitle)
                                                  : "Successfully deleted the following meetings with title: " + meetingTitle;
        return new ResponseRemoveMeetingByTitle(message, deletedMeeting);
    }

    @GetMapping("/meetings/next")
    public ResponseGetNextMeeting getNextMeeting() {
        Meeting nextMeeting = meetingService.getNextMeeting();
        String message = nextMeeting == null ? "There is no next meeting" : "The next meetings is:";
        return new ResponseGetNextMeeting(message, nextMeeting);
    }

    @GetMapping("/meetings/all")
    public List<Meeting> getAllMeetings() {
        List<Meeting> allMeetings = meetingService.getAllMeetings();
        return allMeetings;
    }
}
