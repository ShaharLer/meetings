package com.example.meetings;

import com.example.meetings.Responses.FoundNextMeetingResponse;
import com.example.meetings.Responses.SetMeetingResponse;
import com.example.meetings.model.Meeting;
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
import java.util.Objects;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Task1Application.class, webEnvironment=SpringBootTest.WebEnvironment.RANDOM_PORT)
public class TestGetNextMeeting {

    private static final String URI = "/meeting";
    private static final String GET_URI = "/next";

    @LocalServerPort
    private int port;

    @Autowired
    TestRestTemplate restTemplate = new TestRestTemplate();

    HttpHeaders headers = new HttpHeaders();

    @Test
    public void testMeetingSetSuccessfully() {
        LocalDateTime fromTime = LocalDateTime.now().plusDays(1);
        Meeting meeting = new Meeting(fromTime, fromTime.plusHours(1), "Meeting 1");
        ResponseEntity<String> response = createPostResponseEntity(new HttpEntity<>(meeting, headers));
        Assert.assertEquals(200, response.getStatusCodeValue());
        Assert.assertTrue(Objects.requireNonNull(response.getBody()).contains(SetMeetingResponse.ADDED_MEETING_MESSAGE));
    }

    @Test
    public void testGetNextMeeting() {
        ResponseEntity<String> response = createGetResponseEntity();
        Assert.assertEquals(200, response.getStatusCodeValue());
        Assert.assertTrue(Objects.requireNonNull(response.getBody()).contains(FoundNextMeetingResponse.FOUND_NEXT_MEETING_MESSAGE));
    }

    private ResponseEntity<String> createPostResponseEntity(HttpEntity<Meeting> entity) {
        return restTemplate.exchange(createPostURLWithPort(), HttpMethod.POST, entity, String.class);
    }

    private ResponseEntity<String> createGetResponseEntity() {
        return restTemplate.exchange(createGetURLWithPort(), HttpMethod.GET, null, String.class);
    }

    private String createPostURLWithPort() {
        return "http://localhost:" + port + URI;
    }

    private String createGetURLWithPort() {
        return "http://localhost:" + port + URI + GET_URI;
    }
}
