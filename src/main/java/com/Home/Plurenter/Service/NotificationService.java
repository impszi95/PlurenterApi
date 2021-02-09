package com.Home.Plurenter.Service;

import com.Home.Plurenter.Model.Notification;
import com.Home.Plurenter.Model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class NotificationService {

    @Autowired
    PhotoService photoService;
    @Bean
    public RestTemplate getRestTemplate(){
        return new RestTemplate();
    }

    public void NewMatchNotification(Notification notification) {
        String url = "http://localhost:8082/ws";
        //String url = "http://websocket-notifier:8082/ws";

        HttpEntity<Notification> request = new HttpEntity<Notification>(notification);

        RestTemplate restTemplate = new RestTemplate();
        restTemplate.postForEntity(url + "/notify",request, String.class);
    }
    public Notification createNotification(User userX, User userY){
        Notification notification = new Notification();

        notification.setUserIdX(userX.getId());
        notification.setNameX(userX.getName());
        byte[] thumbnailX = photoService.getThumbnailForUser(userX.getId());
        notification.setThumbnailX(thumbnailX);

        notification.setUserIdY(userY.getId());
        notification.setNameY(userY.getName());
        byte[] thumbnailY = photoService.getThumbnailForUser(userY.getId());
        notification.setThumbnailY(thumbnailY);

        return notification;
    }
}
