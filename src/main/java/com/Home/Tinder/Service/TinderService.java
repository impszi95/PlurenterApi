package com.Home.Tinder.Service;

import com.Home.Tinder.Model.Photo;
import com.Home.Tinder.Model.User;
import com.Home.Tinder.Repo.UserRepo;
import com.Home.Tinder.Security.Payload.Response.ActualMeetResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class TinderService {

    @Autowired
    UserRepo userRepo;

    private void setNextUser(String userId) {
        //Ha userID queue<20 akk léterhozok 20nagy queue-t
        //kül peek()et-->TindernextResp-t visszaadom, aztán remove first-ölöm
        Random rand = new Random();
        User user = userRepo.findById(userId) .orElseThrow(() -> new UsernameNotFoundException("User Not Found with userId: RandomID"));

        int repo_size = userRepo.findAll().size();
        if (repo_size == user.getPreviousMeets().size())
        {
            return;
        }
        String rndUserId = "";
        do {
            rndUserId = userRepo.findAll().get(rand.nextInt(repo_size)).getId();

        }while (user.getPreviousMeets().contains(rndUserId));

        User nextMeet = userRepo.findById(rndUserId) .orElseThrow(() -> new UsernameNotFoundException("User Not Found with userId: RandomID"));

        user.setActualMeetId(nextMeet.getId());
        userRepo.save(user);
    }

    public ActualMeetResponse GetActualMeet(){
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String actualMeetId = userDetails.getActualMeetId();

        //First login
        if(userDetails.getActualMeetId().equals("")){
            setNextUser(userDetails.getId());
            User actualUser = userRepo.findById(userDetails.getId()).orElseThrow(() -> new UsernameNotFoundException("User Not Found with userId"));
            actualMeetId = actualUser.getActualMeetId();
        }

        //Met with all already
        int repo_size = userRepo.findAll().size();
        if (repo_size == userDetails.getGetPreviousMeets().size())
        {
            return new ActualMeetResponse("", "", new ArrayList<Photo>());
        }

        User actualMeet = userRepo.findById(actualMeetId) .orElseThrow(() -> new UsernameNotFoundException("User Not Found with userId: RandomID"));
        return new ActualMeetResponse(actualMeet.getId(), actualMeet.getUsername(), actualMeet.getPhotos());
    }

    public void XUserLikesYUser(String idX, String idY){
        User userX = userRepo.findById(idX).orElseThrow(() -> new UsernameNotFoundException("User Not Found with userId: " + idX));
        userX.addPreviousMeets(idY);
        userRepo.save(userX);

        User userY = userRepo.findById(idY).orElseThrow(() -> new UsernameNotFoundException("User Not Found with userId: " + idY));
        int prev_likes = userY.getLikes();
        userY.setLikes(prev_likes+1);
        userRepo.save(userY);

        setNextUser(idX);
    }
}