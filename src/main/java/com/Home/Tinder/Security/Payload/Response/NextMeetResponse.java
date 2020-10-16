package com.Home.Tinder.Security.Payload.Response;

import com.Home.Tinder.Model.Photo;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class NextMeetResponse {
        private String username;
        private Photo photo;
}
