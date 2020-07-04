package com.example.meetings.model;

public class Response {

    private String message;
//    private Meeting meeting;

    public Response(String message) {

    }

    /*
    public Response(Meeting meeting) {
        this.meeting = meeting;
    }

    public Response(String description, Meeting meeting) {
        this.message = description;
        this.meeting = meeting;
    }

     */

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    /*
    public Meeting getMeeting() {
        return meeting;
    }

    public void setMeeting(Meeting meeting) {
        this.meeting = meeting;
    }

     */
}
