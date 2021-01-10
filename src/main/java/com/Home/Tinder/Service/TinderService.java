package com.Home.Tinder.Service;

import com.Home.Tinder.Model.Notification;
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

    @Autowired
    NotificationService notificationService;

    @Autowired
    PhotoService photoService;

    private void SetNextUser(String userId) {
        //Ha userID queue<20 akk léterhozok 20nagy queue-t
        //kül peek()et-->TindernextResp-t visszaadom, aztán remove first-ölöm
        Random rand = new Random();
        User user = userRepo.findById(userId) .orElseThrow(() -> new UsernameNotFoundException("User Not Found with userId: userId"));

        int repo_size = userRepo.findAll().size();
        if (repo_size == user.getPreviousMeets().size())
        {
            user.setActualMeetId("");
            userRepo.save(user);
            return;
        }
        String rndUserId = "";
        do {
            rndUserId = userRepo.findAll().get(rand.nextInt(repo_size)).getId();
        }while (user.getPreviousMeets().contains(rndUserId));

        String finalRndUserId = rndUserId;
        User nextMeet = userRepo.findById(rndUserId) .orElseThrow(() -> new UsernameNotFoundException("nextMeet Not Found with userId:" + finalRndUserId));

        user.setActualMeetId(nextMeet.getId());
        userRepo.save(user);
    }

    public MeetResponse GetActualMeet(){
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String userId = userDetails.getId();

        User user = userRepo.findById(userId).orElseThrow(() -> new UsernameNotFoundException("User Not Found with userId: " + userId));
        String actualMeetId = user.getActualMeetId();

        //First/Empty login
        if(actualMeetId.equals("")){
            SetNextUser(user.getId());
            user = userRepo.findById(userId).orElseThrow(() -> new UsernameNotFoundException("User Not Found with userId: " + userId));
            actualMeetId = user.getActualMeetId();
        }

        //Met with all already
        int repo_size = userRepo.findAll().size();
        if (repo_size == user.getPreviousMeets().size())
        {
            return new MeetResponse("", "", new ArrayList<Photo>());
        }

        String finalActualMeetId = actualMeetId;
        User actualMeet = userRepo.findById(actualMeetId) .orElseThrow(() -> new UsernameNotFoundException("actualMeet Not Found with userId: "+ finalActualMeetId));
        return new MeetResponse(actualMeet.getId(), actualMeet.getUsername(), actualMeet.getPhotos());
    }

    public void XMetY(String idX, String idY){
        User userX = userRepo.findById(idX).orElseThrow(() -> new UsernameNotFoundException("User Not Found with userId: " + idX));
        userX.addPreviousMeets(idY);
        userRepo.save(userX);

        SetNextUser(idX);
    }

    public void UserLikesActualMeet(){
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String idX = userDetails.getId();
        String idY = userDetails.getActualMeetId();

        XMetY(idX,idY);
        HandleLike(idX, idY);
        IncementLikes(idY);
    }

    private void HandleLike(String idX, String idY) {
        User userX = userRepo.findById(idX).orElseThrow(() -> new UsernameNotFoundException("User Not Found with userId: " + idX));
        User userY = userRepo.findById(idY).orElseThrow(() -> new UsernameNotFoundException("User Not Found with userId: " + idY));

        boolean matched = userX.getReceivedLikesMeets().contains(idY);
        if (matched){
            Notification newNotification = createNotification(userX,userY);
            notificationService.NewMatchNotification(newNotification);

            userX.addMatchedMeet(idY);
            userX.removeReceivedMeet(idY);

            userY.addMatchedMeet(idX);
            userY.removeLikedMeet(idX);
        }
        else {
            userX.addLikedMeet(idY);
            userY.addReceivedLikesMeets(idX);
        }
        userRepo.save(userX);
        userRepo.save(userY);
    }

    private Notification createNotification(User userX, User userY){
        Notification notification = new Notification();

        notification.setUserIdX(userX.getId());
        notification.setUsernameX(userX.getUsername());
        byte[] thumbnailX = photoService.getThumbnailForUser(userX.getId());
        notification.setThumbnailX(thumbnailX);

        notification.setUserIdY(userY.getId());
        notification.setUsernameY(userY.getUsername());
        byte[] thumbnailY = photoService.getThumbnailForUser(userY.getId());
        notification.setThumbnailY(thumbnailY);

        return notification;
    }

    public void IncementLikes(String idY){
        User userY = userRepo.findById(idY).orElseThrow(() -> new UsernameNotFoundException("User Not Found with userId: " + idY));
        int prev_likes = userY.getLikes();
        userY.setLikes(prev_likes+1);
        userRepo.save(userY);
    }

    public List<MeetResponse> GetAllMatchedMeets(){
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String idX = userDetails.getId();

        List<MeetResponse> matchedMeetsRes = new ArrayList<>();
        User userX = userRepo.findById(idX).orElseThrow(() -> new UsernameNotFoundException("User Not Found with userId: " + idX));

        for (String matchedMeetId : userX.getMatchedMeets()){
            User matchedMeet = userRepo.findById(matchedMeetId).orElseThrow(() -> new UsernameNotFoundException("User Not Found with userId: " + idX));
            MeetResponse matchedMeetRes = new MeetResponse(matchedMeet.getId(), matchedMeet.getUsername(), matchedMeet.getPhotos());
            matchedMeetsRes.add(matchedMeetRes);
        }
        return matchedMeetsRes;
    }

    public void UserDislikesActualMeet() {
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String idX = userDetails.getId();
        String idY = userDetails.getActualMeetId();

        XMetY(idX,idY);
    }
}