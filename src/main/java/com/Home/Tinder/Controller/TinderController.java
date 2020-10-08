package com.Home.Tinder.Controller;

import com.Home.Tinder.Model.Photo;
import com.Home.Tinder.Security.Payload.Response.TinderNextUserResponse;
import com.Home.Tinder.Service.TinderService;
import com.Home.Tinder.Service.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@CrossOrigin(origins = "*")
@RestController
public class TinderController {

    @Autowired
    TinderService tinderService;

    @GetMapping("/nextUser")
    public ResponseEntity<?> getAllPhotos(){

        TinderNextUserResponse nextUsersResponse = tinderService.getNextUser();

        return ResponseEntity.ok(nextUsersResponse);
    }
}
