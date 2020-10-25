package com.Home.Tinder.Security.Payload.Response;

import com.Home.Tinder.Model.Photo;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class MeetResponse {
        private String id;
        private String username;
        private List<Photo> photos;
}
