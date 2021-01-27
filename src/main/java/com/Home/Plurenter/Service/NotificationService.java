package com.Home.Plurenter.Service;

import com.Home.Plurenter.Model.Notification;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class NotificationService {

    @Bean
    public RestTemplate getRestTemplate(){
        return new RestTemplate();
    }

    public void NewMatchNotification(Notification notification) {
        //String url = "http://localhost:8082/ws";
        String url = "http://websocket-notifier:8082/ws";

        HttpEntity<Notification> request = new HttpEntity<Notification>(notification);

        RestTemplate restTemplate = new RestTemplate();
        restTemplate.postForEntity(url + "/notify",request, String.class);
    }
}
