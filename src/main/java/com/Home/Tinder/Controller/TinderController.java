package com.Home.Tinder.Controller;

import com.Home.Tinder.Model.Photo;
import com.Home.Tinder.Security.Payload.Response.TinderNextUserResponse;
import com.Home.Tinder.Service.TinderService;
import com.Home.Tinder.Service.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
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

    @PostMapping(value = "/likeUser")
    public ResponseEntity<String> LikeUser(@RequestParam("userId") String userId) throws IOException {
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        tinderService.XUserLikesYUser(userDetails.getId(), userId);

        return new ResponseEntity<String>("Liked", HttpStatus.OK);
    }
}
