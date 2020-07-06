package com.example.meetings.model.Constants;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ResponseConstants {

    private static final String ADDED_MEETING_MESSAGE = "Meeting was set successfully";
    private static final String DELETED_MEETING_BY_FROM_TIME_MESSAGE = "The meeting that starts at (%s) was deleted successfully";
    private static final String DELETED_MEETINGS_BY_TITLE_MESSAGE = "The meetings with title \"%s\" were deleted successfully";
    private static final String FOUND_NEXT_MEETING_MESSAGE = "The next meeting is:";
    private static final String NEXT_MEETING_NOT_FOUND_MESSAGE = "There is no meeting coming next";

    public static String getAddedMeetingMessage() {
        return ADDED_MEETING_MESSAGE;
    }

    public static String getDeletedMeetingByFromTimeMessage(LocalDateTime fromTime) {
        // TODO deduplicate the pattern
        return String.format(DELETED_MEETING_BY_FROM_TIME_MESSAGE, fromTime.format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm")));
    }

    public static String getDeletedMeetingsByTitleMessage(String meetingTitle) {
        return String.format(DELETED_MEETINGS_BY_TITLE_MESSAGE, meetingTitle);
    }

    public static String getFoundNextMeetingMessage() {
        return FOUND_NEXT_MEETING_MESSAGE;
    }

    public static String getNextMeetingNotFoundMessage() {
        return NEXT_MEETING_NOT_FOUND_MESSAGE;
    }
}
