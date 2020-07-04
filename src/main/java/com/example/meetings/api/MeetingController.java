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
    @ResponseBody
    public Response setMeeting(@RequestBody Meeting meeting) {
//        return meetingService.setMeeting(meeting);
        Meeting insertedMeeting = meetingService.setMeeting(meeting);
        String message = insertedMeeting == null ? "Failed to add the meeting" : "insertion succeeded";
        return new ResponseSetMeeting(message, meeting);
    }

    /*
    @PostMapping
    public void setMeeting2(@RequestParam("fromTime") @DateTimeFormat(pattern="dd-MM-yyyy HH:mm") Date fromTime,
                           @RequestParam("toTime") @DateTimeFormat(pattern="dd-MM-yyyy HH:mm") Date toTime,
                           @RequestParam("meetingTitle") String meetingTitle) {
        meetingService.setMeeting(fromTime, toTime, meetingTitle);
    }
     */

//    public void removeMeetingByStartTime(@PathVariable("fromTime") @DateTimeFormat(pattern="dd-MM-yyyy,HH:mm") Date fromTime) {
//    @DeleteMapping public void removeMeetingByStartTime(@RequestBody Meeting meeting) {

//    @DeleteMapping("/meetings/{fromTime}")
//    @DeleteMapping(value = "fromTime")
//    public void removeMeetingByStartTime(@RequestParam("fromTime") @DateTimeFormat(pattern="dd.MM.yyyy HH:mm") LocalDateTime fromTime) {
    @DeleteMapping(value = "/fromTime")
    public Response removeMeetingByStartTime(@RequestParam("fromTime") @DateTimeFormat(pattern="dd.MM.yyyy HH:mm") LocalDateTime fromTime) {
        Meeting deletedMeeting = meetingService.removeMeetingByStartTime(fromTime);
        String message = deletedMeeting == null ? "Failed to delete meeting" : "deletion succeeded";
        return new ResponseRemoveMeetingByStartTime(message, deletedMeeting);
    }

    @DeleteMapping(value = "/title")
    public Response removeMeetingByTitle(@RequestParam("meetingTitle") String meetingTitle) {
        List<Meeting> deletedMeeting = meetingService.removeMeetingByTitle(meetingTitle);
        String message = deletedMeeting == null ? ("Failed to delete meetings with title" + meetingTitle) : "deletion succeeded";
        return new ResponseRemoveMeetingByTitle(message, deletedMeeting);
    }

    @GetMapping
    public List<Meeting> getAllMeetings() {
        return meetingService.getAllMeetings();
    }

    @PutMapping
    public ResponseGetNextMeeting getNextMeeting() {
        Meeting nextMeeting = meetingService.getNextMeeting();
        String message = nextMeeting == null ? "There is no next meeting" : "The next meetings is:";
        return new ResponseGetNextMeeting(message, nextMeeting);
    }
}
