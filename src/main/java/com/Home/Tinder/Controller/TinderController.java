package com.Home.Tinder.Controller;

import com.Home.Tinder.Model.User;
import com.Home.Tinder.Security.Payload.Response.MeetResponse;
import com.Home.Tinder.Security.Payload.Response.UsersResponse;
import com.Home.Tinder.Service.NotificationService;
import com.Home.Tinder.Service.TinderService;
import com.Home.Tinder.Service.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@CrossOrigin(origins = "*")
@RestController
public class TinderController {

    @Autowired
    TinderService tinderService;

    @Autowired
    NotificationService notiService;

    @GetMapping("/actualMeet")
    public ResponseEntity<?> actualMeet(){
        MeetResponse meetResponse = tinderService.GetActualMeet();
        return ResponseEntity.ok(meetResponse);
    }

    @PostMapping(value = "/likeUser")
    public ResponseEntity<String> LikeUser() throws IOException {
        tinderService.UserLikesActualMeet();
        return new ResponseEntity<>("Liked", HttpStatus.OK);
    }

    @PostMapping(value = "/dislikeUser")
    public ResponseEntity<String> DislikeUser() throws IOException {
        tinderService.UserDislikesActualMeet();
        return new ResponseEntity<>("Liked", HttpStatus.OK);
    }

    @GetMapping("/getAllMatches")
    public ResponseEntity<?> getAllMatches(){
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        List<MeetResponse> matchesMeets = tinderService.GetAllMatchedMeets();
        return ResponseEntity.ok(matchesMeets);
    }
}
