package com.Home.Tinder.Service;

import com.Home.Tinder.Model.Photo;
import com.Home.Tinder.Model.User;
import com.Home.Tinder.Repo.UserRepo;
import com.Home.Tinder.Security.Payload.Response.MeetResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class TinderService {

    @Autowired
    UserRepo userRepo;

    private void SetNextUser(String userId) {
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

    public MeetResponse GetActualMeet(){
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String actualMeetId = userDetails.getActualMeetId();

        //First login
        if(userDetails.getActualMeetId().equals("")){
            SetNextUser(userDetails.getId());
            User actualUser = userRepo.findById(userDetails.getId()).orElseThrow(() -> new UsernameNotFoundException("User Not Found with userId"));
            actualMeetId = actualUser.getActualMeetId();
        }

        //Met with all already
        int repo_size = userRepo.findAll().size();
        if (repo_size == userDetails.getGetPreviousMeets().size())
        {
            return new MeetResponse("", "", new ArrayList<Photo>());
        }

        User actualMeet = userRepo.findById(actualMeetId) .orElseThrow(() -> new UsernameNotFoundException("User Not Found with userId: RandomID"));
        return new MeetResponse(actualMeet.getId(), actualMeet.getUsername(), actualMeet.getPhotos());
    }

    public void XMetY(String idX, String idY){
        User userX = userRepo.findById(idX).orElseThrow(() -> new UsernameNotFoundException("User Not Found with userId: " + idX));
        userX.addPreviousMeets(idY);
        userRepo.save(userX);

        SetNextUser(idX);
    }

    public void XUserLikesYUser(String idX, String idY){
        XMetY(idX,idY);
        SaveLike(idX, idY);
        IncementLikes(idY);
    }

    private void SaveLike(String idX, String idY) {
        User userX = userRepo.findById(idX).orElseThrow(() -> new UsernameNotFoundException("User Not Found with userId: " + idX));
        User userY = userRepo.findById(idY).orElseThrow(() -> new UsernameNotFoundException("User Not Found with userId: " + idY));

        boolean matched = userX.getReceivedLikesMeets().contains(idY);
        if (matched){
            userX.addMatchedMeet(idY);
            userY.addMatchedMeet(idX);

            userX.removeReceivedMeet(idY);
            userY.removeLikedMeet(idX);
        }
        else {
            userX.addLikedMeet(idY);
            userY.addReceivedLikesMeets(idX);
        }
        userRepo.save(userX);
        userRepo.save(userY);
    }

    public void IncementLikes(String idY){
        User userY = userRepo.findById(idY).orElseThrow(() -> new UsernameNotFoundException("User Not Found with userId: " + idY));
        int prev_likes = userY.getLikes();
        userY.setLikes(prev_likes+1);
        userRepo.save(userY);
    }

    public List<MeetResponse> GetAllMatchedMeets(String idX){
        List<MeetResponse> matchedMeetsRes = new ArrayList<>();
        User userX = userRepo.findById(idX).orElseThrow(() -> new UsernameNotFoundException("User Not Found with userId: " + idX));

        for (String matchedMeetId : userX.getMatchedMeets()){
            User matchedMeet = userRepo.findById(matchedMeetId).orElseThrow(() -> new UsernameNotFoundException("User Not Found with userId: " + idX));
            MeetResponse matchedMeetRes = new MeetResponse(matchedMeet.getId(), matchedMeet.getUsername(), matchedMeet.getPhotos());
            matchedMeetsRes.add(matchedMeetRes);
        }
        return matchedMeetsRes;
    }

}