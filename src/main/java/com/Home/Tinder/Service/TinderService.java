package com.Home.Tinder.Service;

import com.Home.Tinder.Model.User;
import com.Home.Tinder.Repo.UserRepo;
import com.Home.Tinder.Security.Payload.Response.NextMeetResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class TinderService {

    @Autowired
    UserRepo userRepo;

    public NextMeetResponse getNextUser() {
        //Ha userID queue<20 akk léterhozok 20nagy queue-t
        //kül peek()et-->TindernextResp-t visszaadom, aztán remove first-ölöm
        Random rand = new Random();
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        int repo_size = userRepo.findAll().size();

        //Met with all already
        if (repo_size == userDetails.getGetPreviousMeets().size())
        {
            return new NextMeetResponse("",null);
        }

        String rndUserId = "";
        do {
            rndUserId = userRepo.findAll().get(rand.nextInt(repo_size)).getId();

        }while (userDetails.getGetPreviousMeets().contains(rndUserId));

        User nextMeet = userRepo.findById(rndUserId) .orElseThrow(() -> new UsernameNotFoundException("User Not Found with userId: RandomID"));

        return new NextMeetResponse(nextMeet.getUsername(), nextMeet.getPhotos().get(0));
    }

    public void XUserLikesYUser(String idX, String idY){
        User userX = userRepo.findById(idX).orElseThrow(() -> new UsernameNotFoundException("User Not Found with userId: " + idX));
        userX.addPreviousMeets(idY);
        userRepo.save(userX);

        User userY = userRepo.findById(idY).orElseThrow(() -> new UsernameNotFoundException("User Not Found with userId: " + idY));
        int prev_likes = userY.getLikes();
        userY.setLikes(prev_likes+1);
        userRepo.save(userY);
    }
}