package com.Home.Tinder.Security.Payload.Response;

import com.Home.Tinder.Model.Photo;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TinderNextUserResponse {
        private String username;
        private String photo;
}
