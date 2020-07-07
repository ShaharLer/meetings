package com.example.meetings;

import com.example.meetings.Exceptions.CrossDaysException;
import com.example.meetings.Exceptions.DurationException;
import com.example.meetings.Exceptions.SaturdayException;
import com.example.meetings.Exceptions.StartEndTimesInvalidException;
import com.example.meetings.Responses.SetMeetingResponse;
import com.example.meetings.model.Meeting;
import com.example.meetings.model.MeetingConstants;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.Objects;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Task1Application.class, webEnvironment=SpringBootTest.WebEnvironment.RANDOM_PORT)
public class TestSetMeeting {

    private static final String URI = "/meeting";

    @LocalServerPort
    private int port;

    @Autowired
    TestRestTemplate restTemplate = new TestRestTemplate();

    HttpHeaders headers = new HttpHeaders();

    @Test
    public void testFromTimeAfterToTime() {
        LocalDateTime fromTime = LocalDateTime.of(2020, Month.JULY, 6, 13, 0, 0);
        LocalDateTime toTime = LocalDateTime.of(2020, Month.JULY, 6, 12, 0, 0);
        Meeting meeting = new Meeting(fromTime, toTime, "Meeting 1");
        ResponseEntity<String> response = createPostResponseEntity(new HttpEntity<>(meeting, headers));
        Assert.assertEquals(400, response.getStatusCodeValue());
        Assert.assertTrue(Objects.requireNonNull(response.getBody()).contains(
                StartEndTimesInvalidException.MEETING_START_END_TIMES_INVALID_ERROR));
    }

    @Test
    public void testMinimumMeetingDuration() {
        LocalDateTime fromTime = LocalDateTime.of(2020, Month.JULY, 6, 13, 0, 0);
        LocalDateTime toTime = LocalDateTime.of(2020, Month.JULY, 6, 13, 10, 0);
        Meeting meeting = new Meeting(fromTime, toTime, "Meeting 1");
        ResponseEntity<String> response = createPostResponseEntity(new HttpEntity<>(meeting, headers));
        Assert.assertEquals(400, response.getStatusCodeValue());
        Assert.assertTrue(Objects.requireNonNull(response.getBody()).contains(String.format(
                DurationException.MEETING_DURATION_NOT_VALID, MeetingConstants.MINIMUM_MEETING_TIME_IN_MINUTES,
                MeetingConstants.MAXIMUM_MEETING_TIME_IN_MINUTES)));
    }

    @Test
    public void testMaximumMeetingDuration() {
        LocalDateTime fromTime = LocalDateTime.of(2020, Month.JULY, 6, 13, 0, 0);
        LocalDateTime toTime = LocalDateTime.of(2020, Month.JULY, 6, 15, 10, 0);
        Meeting meeting = new Meeting(fromTime, toTime, "Meeting 1");
        ResponseEntity<String> response = createPostResponseEntity(new HttpEntity<>(meeting, headers));
        Assert.assertEquals(400, response.getStatusCodeValue());
        Assert.assertTrue(Objects.requireNonNull(response.getBody()).contains(String.format(
                DurationException.MEETING_DURATION_NOT_VALID, MeetingConstants.MINIMUM_MEETING_TIME_IN_MINUTES,
                        MeetingConstants.MAXIMUM_MEETING_TIME_IN_MINUTES)));
    }

    @Test
    public void testCrossDaysMeeting() {
        LocalDateTime fromTime = LocalDateTime.of(2020, Month.JULY, 6, 23, 30, 0);
        LocalDateTime toTime = LocalDateTime.of(2020, Month.JULY, 7, 0, 30, 0);
        Meeting meeting = new Meeting(fromTime, toTime, "Meeting 1");
        ResponseEntity<String> response = createPostResponseEntity(new HttpEntity<>(meeting, headers));
        Assert.assertEquals(400, response.getStatusCodeValue());
        Assert.assertTrue(Objects.requireNonNull(response.getBody()).contains(
                CrossDaysException.MEETING_CROSS_DAYS_ERROR));
    }

    @Test
    public void testMeetingOnSaturday() {
        LocalDateTime fromTime = LocalDateTime.of(2020, Month.JULY, 4, 8, 30, 0);
        LocalDateTime toTime = LocalDateTime.of(2020, Month.JULY, 4, 9, 30, 0);
        Meeting meeting = new Meeting(fromTime, toTime, "Meeting 1");
        ResponseEntity<String> response = createPostResponseEntity(new HttpEntity<>(meeting, headers));
        Assert.assertEquals(400, response.getStatusCodeValue());
        Assert.assertTrue(Objects.requireNonNull(response.getBody()).contains(
                SaturdayException.MEETING_ON_SATURDAY_ERROR));
    }

    @Test
    public void testMeetingSetSuccessfully() {
        LocalDateTime fromTime = LocalDateTime.of(2020, Month.JULY, 5, 8, 30, 0);
        LocalDateTime toTime = LocalDateTime.of(2020, Month.JULY, 5, 9, 30, 0);
        Meeting meeting = new Meeting(fromTime, toTime, "Meeting 1");
        ResponseEntity<String> response = createPostResponseEntity(new HttpEntity<>(meeting, headers));
        Assert.assertEquals(200, response.getStatusCodeValue());
        Assert.assertTrue(Objects.requireNonNull(response.getBody()).contains(
                SetMeetingResponse.ADDED_MEETING_MESSAGE));
    }

    private ResponseEntity<String> createPostResponseEntity(HttpEntity<Meeting> entity) {
        return restTemplate.exchange(createURLWithPort(), HttpMethod.POST, entity, String.class);
    }

    private String createURLWithPort() {
        return "http://localhost:" + port + URI;
    }
}
