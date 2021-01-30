package com.Home.Plurenter.Service;

import com.Home.Plurenter.Model.*;
import com.Home.Plurenter.Repo.LandlordRepo;
import com.Home.Plurenter.Repo.TenantRepo;
import com.Home.Plurenter.Repo.UserRepo;
import com.Home.Plurenter.Security.Payload.Response.Meet.LandlordMeetResponse;
import com.Home.Plurenter.Security.Payload.Response.Meet.MeetResponse;
import com.Home.Plurenter.Security.Payload.Response.Meet.TenantMeetResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class PlurenterService {
    @Autowired
    UserRepo userRepo;
    @Autowired
    TenantRepo tenantRepo;
    @Autowired
    LandlordRepo landlordRepo;
    @Autowired
    NotificationService notificationService;
    @Autowired
    PhotoService photoService;

    private boolean SetNextUser(String userId) { //Return isSucceed
        Random rand = new Random();
        User user = userRepo.findById(userId) .orElseThrow(() -> new UsernameNotFoundException("User Not Found with userId: userId"));

        //Tenant or Landlord
        int repo_size = 0;
        if (user.getIsTenant()){
            repo_size = landlordRepo.findAll().size();
        }
        else{
            repo_size = tenantRepo.findAll().size();
        }
        //Without confitions //All visited
//        if (repo_size == user.getPreviousMeets().size())
//        {
//            user.setActualMeetId("");
//            userRepo.save(user);
//            return;
//        }
        String rndUserId = "";
        boolean foundMeet = false;
        if (!user.getIsTenant()) {
            for (int i=0;i<repo_size*2;i++){
                rndUserId = tenantRepo.findAll().get(rand.nextInt(repo_size)).getCommonId();
                boolean haventMet = !user.getPreviousMeets().contains(rndUserId);
                if (haventMet){
                    Tenant probMeet = tenantRepo.findByCommonId(rndUserId).orElseThrow(() -> new UsernameNotFoundException("probMeet Not Found with userId" ));
                    boolean tenantFitsConditions = CheckConditionsForTenant(user, probMeet);
                    if (tenantFitsConditions){
                        i=repo_size*2;
                        foundMeet = true;
                    }
                }
            }
            //Without confitions
//            do {
//                rndUserId = landlordRepo.findAll().get(rand.nextInt(repo_size)).getCommonId();
//            } while (user.getPreviousMeets().contains(rndUserId));
        }
        else{
            for (int i=0;i<repo_size*2;i++){
                rndUserId = landlordRepo.findAll().get(rand.nextInt(repo_size)).getCommonId();
                boolean haventMet = !user.getPreviousMeets().contains(rndUserId);
                if (haventMet){
                    Landlord probMeet = landlordRepo.findByCommonId(rndUserId).orElseThrow(() -> new UsernameNotFoundException("probMeet Not Found with userId" ));
                    boolean landlordFitsConditions = CheckConditionsForLandlord(user, probMeet);
                    if (landlordFitsConditions){
                        i=repo_size*2;
                        foundMeet = true;
                    }
                }
            }
//            do {
//                rndUserId = tenantRepo.findAll().get(rand.nextInt(repo_size)).getCommonId();
//            } while (user.getPreviousMeets().contains(rndUserId));
        }
        //All visited (with conditions means 1000 try
        if(!foundMeet){
            user.setActualMeetId("");
            userRepo.save(user);
            return false;
        }

        String finalRndUserId = rndUserId;
        User nextMeet = userRepo.findById(rndUserId) .orElseThrow(() -> new UsernameNotFoundException("nextMeet Not Found with userId:" + finalRndUserId));

        user.setActualMeetId(nextMeet.getId());
        userRepo.save(user);
        return true;
    }

    private boolean CheckConditionsForLandlord(User user, Landlord probMeet) {
        Tenant tenant = tenantRepo.findByCommonId(user.getId()).orElseThrow(() -> new UsernameNotFoundException("Landlord Not Found with commonId"));
        if (probMeet.getMinRentTime().isLessOrEqualsThan(tenant.getMinRentTime())){
            return true;
        }
        return false;
    }
    private boolean CheckConditionsForTenant(User user, Tenant probMeet) {
        Landlord landlord = landlordRepo.findByCommonId(user.getId()).orElseThrow(() -> new UsernameNotFoundException("Landlord Not Found with commonId"));
        if (probMeet.getMinRentTime().isMoreOrEqualsThan(landlord.getMinRentTime())){
            return true;
        }
        return false;
    }

    public MeetResponse GetActualMeet(){
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String userId = userDetails.getId();

        User user = userRepo.findById(userId).orElseThrow(() -> new UsernameNotFoundException("User Not Found with userId: " + userId));
        String actualMeetId = user.getActualMeetId();

        //First/Empty login
        if(actualMeetId.equals("")){
            boolean isSucceed = SetNextUser(user.getId());  // SetNext  AND Check if Met with all already
            if (isSucceed) {
                user = userRepo.findById(userId).orElseThrow(() -> new UsernameNotFoundException("User Not Found with userId: " + userId));
                actualMeetId = user.getActualMeetId();
            }
            else{
                return new MeetResponse();
            }
        }
        String finalActualMeetId = actualMeetId;
        User actualMeet = userRepo.findById(actualMeetId) .orElseThrow(() -> new UsernameNotFoundException("actualMeet Not Found with userId: "+ finalActualMeetId));

        if (user.getIsTenant() && !actualMeet.getIsTenant()){ // TODO: Make a Validator and/or Factory from this
            LandlordMeetResponse landlordMeetResponse = new LandlordMeetResponse();
            landlordMeetResponse.setId(actualMeet.getId());
            landlordMeetResponse.setUsername(actualMeet.getUsername());
            landlordMeetResponse.setPhotos(actualMeet.getPhotos());
            landlordMeetResponse.setDescription(actualMeet.getDescription());

            Landlord landlord = landlordRepo.findByCommonId(actualMeet.getId()).orElseThrow(() -> new UsernameNotFoundException("User Not Found with userId: " + actualMeet.getId()));
            landlordMeetResponse.setMinRentTime(landlord.getMinRentTime().toString());
            return landlordMeetResponse;
        }
        if (!user.getIsTenant() && actualMeet.getIsTenant()){
            TenantMeetResponse tenantMeetResponse = new TenantMeetResponse();
            tenantMeetResponse.setId(actualMeet.getId());
            tenantMeetResponse.setUsername(actualMeet.getUsername());
            tenantMeetResponse.setPhotos(actualMeet.getPhotos());
            tenantMeetResponse.setDescription(actualMeet.getDescription());

            Tenant tenant = tenantRepo.findByCommonId(actualMeet.getId()).orElseThrow(() -> new UsernameNotFoundException("User Not Found with userId: " + actualMeet.getId()));
            tenantMeetResponse.setMinRentTime(tenant.getMinRentTime().toString());
            return tenantMeetResponse;
        }
        System.out.println("Queried meet and user is in the same role.");
        return new MeetResponse();
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
            try {
                notificationService.NewMatchNotification(newNotification);
            }catch (Exception e){
                System.out.println("Notification service down.");
            }

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
            if (userX.getIsTenant() && !matchedMeet.getIsTenant()){ // TODO: Make a Validator and/or Factory from this
                LandlordMeetResponse landlordMeetResponse = new LandlordMeetResponse();
                landlordMeetResponse.setId(matchedMeet.getId());
                landlordMeetResponse.setUsername(matchedMeet.getUsername());
                landlordMeetResponse.setPhotos(matchedMeet.getPhotos());
                landlordMeetResponse.setDescription(matchedMeet.getDescription());

                Landlord landlord = landlordRepo.findByCommonId(matchedMeet.getId()).orElseThrow(() -> new UsernameNotFoundException("User Not Found with userId: " + matchedMeet.getId()));
                landlordMeetResponse.setMinRentTime(landlord.getMinRentTime().toString());
                matchedMeetsRes.add(landlordMeetResponse);
            }
            if (!userX.getIsTenant() && matchedMeet.getIsTenant()){
                TenantMeetResponse tenantMeetResponse = new TenantMeetResponse();
                tenantMeetResponse.setId(matchedMeet.getId());
                tenantMeetResponse.setUsername(matchedMeet.getUsername());
                tenantMeetResponse.setPhotos(matchedMeet.getPhotos());
                tenantMeetResponse.setDescription(matchedMeet.getDescription());

                Tenant tenant = tenantRepo.findByCommonId(matchedMeet.getId()).orElseThrow(() -> new UsernameNotFoundException("User Not Found with userId: " + matchedMeet.getId()));
                tenantMeetResponse.setMinRentTime(tenant.getMinRentTime().toString());
                matchedMeetsRes.add(tenantMeetResponse);
            }else {
                System.out.println("Queried meet and user is in the same role." + userX.getUsername() +", "+matchedMeet.getUsername());
            }
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