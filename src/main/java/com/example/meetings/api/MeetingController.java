package com.example.meetings.api;

import com.example.meetings.model.Meeting;
import com.example.meetings.model.Response;
import com.example.meetings.service.MeetingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
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
        return new Response(message, meeting);
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
    @DeleteMapping
    public Response removeMeetingByStartTime(@RequestParam("fromTime") @DateTimeFormat(pattern="dd.MM.yyyy HH:mm") LocalDateTime fromTime) {
        Meeting deletedMeeting = meetingService.removeMeetingByStartTime(fromTime);
        String message = deletedMeeting == null ? "Failed to delete meeting" : "deletion succeeded";
        return new Response(message, deletedMeeting);
    }

    @DeleteMapping(value = "/title")
    public void removeMeetingByTitle(@RequestParam("meetingTitle") String meetingTitle) {
        meetingService.removeMeetingByTitle(meetingTitle);
    }

    @GetMapping
    public List<Meeting> getAllMeetings() {
        return meetingService.getAllMeetings();
    }

    @PutMapping
    public Meeting getNextMeeting() {
        return meetingService.getNextMeeting();
    }
}
