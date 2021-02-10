package com.Home.Plurenter.Service;

import com.Home.Plurenter.Model.*;
import com.Home.Plurenter.Model.Landlord.Landlord;
import com.Home.Plurenter.Model.Tenant.Tenant;
import com.Home.Plurenter.Repo.*;
import com.Home.Plurenter.Security.Payload.Response.Match.LandlordMatchResponse;
import com.Home.Plurenter.Security.Payload.Response.Match.MatchResponse;
import com.Home.Plurenter.Security.Payload.Response.Match.MatchThumbnail;
import com.Home.Plurenter.Security.Payload.Response.Match.TenantMatchResponse;
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
    ActiveTenantRepo activeTenantRepo;
    @Autowired
    ActiveLandlordRepo activeLandlordRepo;

    private boolean SetNextUser(String userId) { //Return isSucceed
        Random rand = new Random();
        User user = userRepo.findById(userId) .orElseThrow(() -> new UsernameNotFoundException("User Not Found with userId: userId"));

        //Tenant or Landlord
        int repo_size = 0;
        if (user.getIsTenant()){
            repo_size = activeLandlordRepo.findAll().size();
        }
        else{
            repo_size = activeTenantRepo.findAll().size();
        }
        String rndUserId = "";
        boolean foundMeet = false;
        if (!user.getIsTenant()) {
            Landlord landlord = landlordRepo.findById(user.getId()).orElseThrow(() -> new UsernameNotFoundException("Landlord Not Found with commonId"));
            for (int i=0;i<repo_size;i++){  //Try N random
                rndUserId = activeTenantRepo.findAll().get(rand.nextInt(repo_size)).getId();
                boolean haventMet = !user.getPreviousMeets().contains(rndUserId);
                if (haventMet){
                    //TODO User->abstract Tenant/Landlord extended
                    User userProbMeet = userRepo.findById(rndUserId) .orElseThrow(() -> new UsernameNotFoundException("User Not Found with userId: userId"));
                    Tenant probMeet = tenantRepo.findById(rndUserId).orElseThrow(() -> new UsernameNotFoundException("probMeet Not Found with userId" ));
                    boolean tenantFitsConditions = CheckConditionsForTenant(user, landlord, probMeet, userProbMeet);
                    if (tenantFitsConditions){
                        i=repo_size;
                        foundMeet = true;
                    }
                }
            }
            if (!foundMeet){
                for (int i = 0;i<repo_size;i++){    //If no random, Go through sequential
                    rndUserId = activeTenantRepo.findAll().get(i).getId();
                    boolean haventMet = !user.getPreviousMeets().contains(rndUserId);
                    if (haventMet){
                        Tenant probMeet = tenantRepo.findById(rndUserId).orElseThrow(() -> new UsernameNotFoundException("probMeet Not Found with userId" ));
                        User userProbMeet = userRepo.findById(rndUserId) .orElseThrow(() -> new UsernameNotFoundException("User Not Found with userId: userId"));
                        boolean tenantFitsConditions = CheckConditionsForTenant(user, landlord, probMeet, userProbMeet);
                        if (tenantFitsConditions){
                            i=repo_size;
                            foundMeet = true;
                        }
                    }
                }
            }
            //Without confitions
//            do {
//                rndUserId = landlordRepo.findAll().get(rand.nextInt(repo_size)).getCommonId();
//            } while (user.getPreviousMeets().contains(rndUserId));
        }
        else{
            Tenant tenant = tenantRepo.findById(user.getId()).orElseThrow(() -> new UsernameNotFoundException("Tenant Not Found with commonId"));
            for (int i=0;i<repo_size;i++){
                rndUserId = activeLandlordRepo.findAll().get(rand.nextInt(repo_size)).getId();
                boolean haventMet = !user.getPreviousMeets().contains(rndUserId);
                if (haventMet){
                    User userProbMeet = userRepo.findById(rndUserId) .orElseThrow(() -> new UsernameNotFoundException("User Not Found with userId: userId"));
                    Landlord probMeet = landlordRepo.findById(rndUserId).orElseThrow(() -> new UsernameNotFoundException("probMeet Not Found with userId" ));
                    boolean landlordFitsConditions = CheckConditionsForLandlord(user, tenant, probMeet, userProbMeet);
                    if (landlordFitsConditions){
                        i=repo_size;
                        foundMeet = true;
                    }
                }
            }
            if (!foundMeet){
                for (int i = 0;i<repo_size;i++){    //If no random, Go through sequential
                    rndUserId = activeLandlordRepo.findAll().get(i).getId();
                    boolean haventMet = !user.getPreviousMeets().contains(rndUserId);
                    if (haventMet){
                        User userProbMeet = userRepo.findById(rndUserId) .orElseThrow(() -> new UsernameNotFoundException("User Not Found with userId: userId"));
                        Landlord probMeet = landlordRepo.findById(rndUserId).orElseThrow(() -> new UsernameNotFoundException("probMeet Not Found with userId" ));
                        boolean landlordFitsConditions = CheckConditionsForLandlord(user, tenant, probMeet, userProbMeet);
                        if (landlordFitsConditions){
                            i=repo_size;
                            foundMeet = true;
                        }
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
    private boolean CheckConditionsForLandlord(User user,Tenant tenant, Landlord probMeet, User userProbMeet) {
        if (!probMeet.getMinRentTime().isLessOrEqualsThan(tenant.getMinRentTime())||
            !userProbMeet.getLocation().equals(user.getLocation())){
            return false;
        }
        return true;
    }
    private boolean CheckConditionsForTenant(User user, Landlord landlord, Tenant probMeet, User userProbMeet) {
        if (!probMeet.getMinRentTime().isMoreOrEqualsThan(landlord.getMinRentTime()) ||
            !userProbMeet.getLocation().equals(user.getLocation())){
            return false;
        }
        return true;
    }
    public MeetResponse GetActualMeet(){
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String userId = userDetails.getId();

        User user = userRepo.findById(userId).orElseThrow(() -> new UsernameNotFoundException("User Not Found with userId: " + userId));
        String actualMeetId = user.getActualMeetId();

        if (!user.getActive()){
            System.out.println("User queried meet while deactivated.");
            return new MeetResponse();
        }

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

        if (user.getIsTenant() && !actualMeet.getIsTenant()){
            LandlordMeetResponse landlordMeetResponse = new LandlordMeetResponse();
            landlordMeetResponse.setId(actualMeet.getId());
            landlordMeetResponse.setTenant(actualMeet.getIsTenant());
            landlordMeetResponse.setPhotos(actualMeet.getPhotos());
            landlordMeetResponse.setDescription(actualMeet.getDescription());
            landlordMeetResponse.setLocation(actualMeet.getLocation());

            Landlord landlord = landlordRepo.findById(actualMeet.getId()).orElseThrow(() -> new UsernameNotFoundException("User Not Found with userId: " + actualMeet.getId()));
            landlordMeetResponse.setMinRentTime(landlord.getMinRentTime().toString());
            landlordMeetResponse.setRent(landlord.getRent().toString());
            return landlordMeetResponse;
        }
        if (!user.getIsTenant() && actualMeet.getIsTenant()){
            TenantMeetResponse tenantMeetResponse = new TenantMeetResponse();
            tenantMeetResponse.setId(actualMeet.getId());
            tenantMeetResponse.setName(actualMeet.getName());
            tenantMeetResponse.setTenant(actualMeet.getIsTenant());
            tenantMeetResponse.setPhotos(actualMeet.getPhotos());
            tenantMeetResponse.setDescription(actualMeet.getDescription());

            Tenant tenant = tenantRepo.findById(actualMeet.getId()).orElseThrow(() -> new UsernameNotFoundException("User Not Found with userId: " + actualMeet.getId()));
            tenantMeetResponse.setMinRentTime(tenant.getMinRentTime().toString());
            tenantMeetResponse.setJob(tenant.getJob());
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
            userX.addMatchedMeet(idY);
            userX.removeReceivedMeet(idY);
            IncrementMatches(userX);

            userY.addMatchedMeet(idX);
            userY.removeLikedMeet(idX);
            IncrementMatches(userY);

            SendNotification(userX,userY);
        }
        else {
            userX.addLikedMeet(idY);
            userY.addReceivedLikesMeets(idX);
        }
        userRepo.save(userX);
        userRepo.save(userY);
    }
    private void SendNotification(User userX, User userY) {
        Notification newNotification = notificationService.createNotification(userX,userY);
        try {
            notificationService.NewMatchNotification(newNotification);
        }catch (Exception e){
            System.out.println("Notification service down.");
        }
    }
    public void IncementLikes(String idY){
        User userY = userRepo.findById(idY).orElseThrow(() -> new UsernameNotFoundException("User Not Found with userId: " + idY));
        int prev_likes = userY.getLikes();
        userY.setLikes(prev_likes+1);
        userRepo.save(userY);
    }
    public void IncrementMatches(User user){
        int prev_matches = user.getMatches();
        user.setMatches(prev_matches+1);
    }
    public List<MatchThumbnail> GetAllMatchedMeets(){
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String idX = userDetails.getId();

        List<MatchThumbnail> matches = new ArrayList<>();
        User userX = userRepo.findById(idX).orElseThrow(() -> new UsernameNotFoundException("User Not Found with userId: " + idX));

        for (String matchedMeetId : userX.getMatchedMeets()){
            User matchedMeet = userRepo.findById(matchedMeetId).orElseThrow(() -> new UsernameNotFoundException("User Not Found with userId: " + idX));
            MatchThumbnail matchThumbnail = new MatchThumbnail();
            matchThumbnail.setId(matchedMeet.getId());
            matchThumbnail.setThumbnail(matchedMeet.getPhotos().isEmpty() ? null : matchedMeet.getPhotos().get(0));

            if (userX.getIsTenant() && !matchedMeet.getIsTenant()){
                matchThumbnail.setDisplayName(matchedMeet.getLocation().getCity());
            }
            else if (!userX.getIsTenant() && matchedMeet.getIsTenant()){
                matchThumbnail.setDisplayName(matchedMeet.getName());
            }else {
                System.out.println("Queried meet and user is in the same role." + userX.getEmail() +", "+matchedMeet.getEmail());
            }
            matches.add(matchThumbnail);
        }
        return matches;
    }
    public void UserDislikesActualMeet() {
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String idX = userDetails.getId();
        String idY = userDetails.getActualMeetId();

        XMetY(idX,idY);
    }
}