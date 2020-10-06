package com.Home.Tinder.Security.Payload.Response;


import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class JwtResponse {
    private String token;
    private String id;
    private String username;
    private int like;
//    private List<String> roles;
}