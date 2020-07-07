package com.example.meetings;

import com.example.meetings.Responses.RemoveMeetingByTitleResponse;
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
import java.time.Month;
import java.util.Objects;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Task1Application.class, webEnvironment=SpringBootTest.WebEnvironment.RANDOM_PORT)
public class TestRemoveMeetingByTitle {

    private static final String URI = "/meeting";
    private static final String DELETE_URI = "/title/";
    private static final String TITLE = "Meeting 1";

    @LocalServerPort
    private int port;

    @Autowired
    TestRestTemplate restTemplate = new TestRestTemplate();

    HttpHeaders headers = new HttpHeaders();

    @Test
    public void testMeetingSetSuccessfully() {
        LocalDateTime fromTime = LocalDateTime.of(2020, Month.JULY, 5, 8, 30, 0);
        LocalDateTime toTime = LocalDateTime.of(2020, Month.JULY, 5, 9, 30, 0);
        Meeting meeting = new Meeting(fromTime, toTime, TITLE);
        ResponseEntity<String> response = createPostResponseEntity(new HttpEntity<>(meeting, headers));
        Assert.assertEquals(200, response.getStatusCodeValue());
        Assert.assertTrue(Objects.requireNonNull(response.getBody()).contains(SetMeetingResponse.ADDED_MEETING_MESSAGE));
    }

    @Test
    public void testMeetingRemovedByTitleSuccess() {
        ResponseEntity<String> response = createDeleteResponseEntity();
        Assert.assertEquals(200, response.getStatusCodeValue());
        Assert.assertTrue(Objects.requireNonNull(response.getBody()).contains(
                String.format(RemoveMeetingByTitleResponse.DELETED_MEETINGS_BY_TITLE_MESSAGE, TITLE)));
    }

    private ResponseEntity<String> createPostResponseEntity(HttpEntity<Meeting> entity) {
        return restTemplate.exchange(createPostURLWithPort(), HttpMethod.POST, entity, String.class);
    }

    private ResponseEntity<String> createDeleteResponseEntity() {
        return restTemplate.exchange(createDeleteURLWithPort(), HttpMethod.DELETE,null, String.class);
    }

    private String createPostURLWithPort() {
        return "http://localhost:" + port + URI;
    }

    private String createDeleteURLWithPort() {
        return "http://localhost:" + port + URI + DELETE_URI + TITLE;
    }
}
