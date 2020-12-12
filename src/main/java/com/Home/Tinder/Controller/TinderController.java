package com.Home.Tinder.Controller;

import com.Home.Tinder.Model.User;
import com.Home.Tinder.Security.Payload.Response.MeetResponse;
import com.Home.Tinder.Security.Payload.Response.UsersResponse;
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

    @GetMapping("/actualMeet")
    public ResponseEntity<?> actualMeet(){
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        MeetResponse meetResponse = tinderService.GetActualMeet(userDetails.getId());
        return ResponseEntity.ok(meetResponse);
    }

    @PostMapping(value = "/likeUser")
    public ResponseEntity<String> LikeUser(@RequestParam("userId") String userId) throws IOException {
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        tinderService.XUserLikesYUser(userDetails.getId(), userId);
        return new ResponseEntity<>("Liked", HttpStatus.OK);
    }

    @PostMapping(value = "/dislikeUser")
    public ResponseEntity<String> DislikeUser(@RequestParam("userId") String userId) throws IOException {
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        tinderService.XUserDislikeYUser(userDetails.getId(), userId);
        return new ResponseEntity<>("Liked", HttpStatus.OK);
    }

    @GetMapping("/getAllMatches")
    public ResponseEntity<?> getAllMatches(){
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        List<MeetResponse> matchesMeets = tinderService.GetAllMatchedMeets(userDetails.getId());
        return ResponseEntity.ok(matchesMeets);
    }
}
