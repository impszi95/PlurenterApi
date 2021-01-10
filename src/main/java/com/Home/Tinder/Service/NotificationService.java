package com.Home.Tinder.Service;

import com.Home.Tinder.Model.Notification;
import lombok.Data;
import org.apache.http.HttpMessage;
import org.apache.http.protocol.HTTP;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Service
public class NotificationService {

    @Bean
    public RestTemplate getRestTemplate(){
        return new RestTemplate();
    }

    public void NewMatchNotification(Notification notification) {
        String url = "http://localhost:8082";

        HttpEntity<Notification> request = new HttpEntity<Notification>(notification);

        RestTemplate restTemplate = new RestTemplate();
        restTemplate.postForEntity(url + "/notify",request, String.class);
    }
}
