package com.example.meetings;

import com.example.meetings.Responses.RemoveMeetingByStartTimeResponse;
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
import java.time.format.DateTimeFormatter;
import java.util.Objects;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Task1Application.class, webEnvironment=SpringBootTest.WebEnvironment.RANDOM_PORT)
public class TestRemoveMeetingByFromTime {

    private static final String URI = "/meeting";
    private static final String DELETE_URI = "/fromTime/";

    @LocalServerPort
    private int port;

    @Autowired
    TestRestTemplate restTemplate = new TestRestTemplate();

    HttpHeaders headers = new HttpHeaders();

    @Test
    public void testMeetingSetSuccessfully() {
        LocalDateTime fromTime = LocalDateTime.of(2020, Month.JULY, 5, 8, 30, 0);
        LocalDateTime toTime = LocalDateTime.of(2020, Month.JULY, 5, 9, 30, 0);
        Meeting meeting = new Meeting(fromTime, toTime, "Meeting 1");
        ResponseEntity<String> response = createPostResponseEntity(new HttpEntity<>(meeting, headers));
        Assert.assertEquals(200, response.getStatusCodeValue());
        Assert.assertTrue(Objects.requireNonNull(response.getBody()).contains(SetMeetingResponse.ADDED_MEETING_MESSAGE));
    }

    @Test
    public void testMeetingRemovedByFromTimeSuccess() {
        LocalDateTime fromTime = LocalDateTime.of(2020, Month.JULY, 5, 8, 30, 0);
        String fromTimeAsString = fromTime.format(DateTimeFormatter.ofPattern(MeetingConstants.MEETING_TIME_PATTERN));
        ResponseEntity<String> response = createDeleteResponseEntity(fromTimeAsString);
        Assert.assertEquals(200, response.getStatusCodeValue());
        Assert.assertTrue(Objects.requireNonNull(response.getBody()).contains(
                String.format(RemoveMeetingByStartTimeResponse.DELETED_MEETING_BY_FROM_TIME_MESSAGE, fromTimeAsString)));
    }

    private ResponseEntity<String> createPostResponseEntity(HttpEntity<Meeting> entity) {
        return restTemplate.exchange(createPostURLWithPort(), HttpMethod.POST, entity, String.class);
    }

    private ResponseEntity<String> createDeleteResponseEntity(String fromTime) {
        return restTemplate.exchange(createDeleteURLWithPort(fromTime), HttpMethod.DELETE,null, String.class);
    }

    private String createPostURLWithPort() {
        return "http://localhost:" + port + URI;
    }

    private String createDeleteURLWithPort(String parameter) {
        return "http://localhost:" + port + URI + DELETE_URI + parameter;
    }
}
