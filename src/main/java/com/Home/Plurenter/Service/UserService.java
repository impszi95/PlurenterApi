package com.Home.Plurenter.Service;

import com.Home.Plurenter.Model.*;
import com.Home.Plurenter.Model.Landlord.ActiveLandlord;
import com.Home.Plurenter.Model.Landlord.Landlord;
import com.Home.Plurenter.Model.Landlord.LandlordInfo;
import com.Home.Plurenter.Model.Tenant.ActiveTenant;
import com.Home.Plurenter.Model.Tenant.Tenant;
import com.Home.Plurenter.Model.Tenant.TenantInfo;
import com.Home.Plurenter.Repo.*;
import com.Home.Plurenter.Security.Payload.Request.SignupRequest;
import com.Home.Plurenter.Security.Payload.Response.Match.LandlordMatchResponse;
import com.Home.Plurenter.Security.Payload.Response.Match.MatchResponse;
import com.Home.Plurenter.Security.Payload.Response.Match.TenantMatchResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    PasswordEncoder encoder;

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private TenantRepo tenantRepo;

    @Autowired
    private LandlordRepo landlordRepo;

    @Autowired
    private ActiveLandlordRepo activeLandlordRepo;

    @Autowired
    private ActiveTenantRepo activeTenantRepo;

    @Autowired
    private Valider valider;

    public void SaveTenantInfos(TenantInfo infos) {
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = getUserFromDB(userDetails.getId());
        Tenant tenant = getTenantFromDB(user.getId());

        user.setDescription(infos.getDescription()); //common prop
        tenant.setMinRentTime(infos.getMinRentTime()); //uniqe prop
        tenant.setJob(infos.getJob());

        if (valider.ValidTenantDatas(tenant)){
            if (!user.getActive()){
                user.setCanActivate(true);
            }
            userRepo.save(user);
            tenantRepo.save(tenant);
        }
    }
    public void SaveLandlordInfos(LandlordInfo infos) {
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = getUserFromDB(userDetails.getId());
        Landlord landlord = getLandlordFromDB(user.getId());

        user.setDescription(infos.getDescription()); //common prop
        landlord.setMinRentTime(infos.getMinRentTime()); //unique prop
        landlord.setRent(infos.getRent());

        if (valider.ValidLandlordDatas(landlord)) {
            if (!user.getActive()){
                user.setCanActivate(true);
            }
            userRepo.save(user);
            landlordRepo.save(landlord);
        }
    }
    public MatchResponse GetMatch(String matchId) {
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        boolean validMatch = userDetails.getMatchedMeets().contains(matchId);
        if (validMatch){
            User matchedUser = getUserFromDB(matchId);
            if (userDetails.getIsTenant() && !matchedUser.getIsTenant()){//Match is a valid landlord
                Landlord landlord = getLandlordFromDB(matchedUser.getId());
                LandlordMatchResponse landlordMatchResponse = new LandlordMatchResponse();
                landlordMatchResponse.setUsername(matchedUser.getUsername());
                landlordMatchResponse.setDescription(matchedUser.getDescription());
                landlordMatchResponse.setPhotos(matchedUser.getPhotos());
                landlordMatchResponse.setTenant(matchedUser.getIsTenant());

                landlordMatchResponse.setMinRentTime(landlord.getMinRentTime().toString());
                landlordMatchResponse.setRent(landlord.getRent().toString());
                return landlordMatchResponse;
            }
            if (!userDetails.getIsTenant() && matchedUser.getIsTenant()) { //Match is a valid tenant
                Tenant tenant = getTenantFromDB(matchedUser.getId());
                TenantMatchResponse tenantMatchResponse = new TenantMatchResponse();
                tenantMatchResponse.setUsername(matchedUser.getUsername());
                tenantMatchResponse.setDescription(matchedUser.getDescription());
                tenantMatchResponse.setPhotos(matchedUser.getPhotos());
                tenantMatchResponse.setTenant(matchedUser.getIsTenant());

                tenantMatchResponse.setMinRentTime(tenant.getMinRentTime().toString());
                tenantMatchResponse.setJob(tenant.getJob());
                return tenantMatchResponse;
            }
            System.out.println("Queried match and user is in the same role.");
            return new MatchResponse();
        }
        System.out.println("Queried user as a match is not a valid match for user.");
        return new MatchResponse();
    }
    public TenantInfo GetTenant(){ //Self
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = getUserFromDB(userDetails.getId());
        Tenant tenant = getTenantFromDB(user.getId());

        boolean validTenant = userDetails.getIsTenant();
        if (validTenant) {
            TenantInfo tenantInfo = new TenantInfo();
            tenantInfo.setLikes(user.getLikes());
            tenantInfo.setDescription(user.getDescription());
            tenantInfo.setActive(user.getActive());
            tenantInfo.setCanActivate(user.getCanActivate());
            tenantInfo.setMinRentTime(tenant.getMinRentTime());
            tenantInfo.setJob(tenant.getJob());
            return tenantInfo;
        }
        System.out.println("Unauthorized tenant query!");
        return new TenantInfo();
    }
    public LandlordInfo GetLandlord(){ //Self
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = getUserFromDB(userDetails.getId());
        Landlord landlord = getLandlordFromDB(user.getId());

        boolean validLandlord = !userDetails.getIsTenant();
        if (validLandlord) {
            LandlordInfo landlordInfo = new LandlordInfo();
            landlordInfo.setLikes(user.getLikes());
            landlordInfo.setDescription(user.getDescription());
            landlordInfo.setActive(user.getActive());
            landlordInfo.setCanActivate(user.getCanActivate());
            landlordInfo.setMinRentTime(landlord.getMinRentTime());
            landlordInfo.setRent(landlord.getRent());
            return landlordInfo;
        }
        System.out.println("Unauthorized landlord query!");
        return new LandlordInfo();
    }
    public void CreateNewUser(SignupRequest signUpRequest){
        User user = new User(signUpRequest.getUsername(),
                encoder.encode(signUpRequest.getPassword()));
        user.setIsTenant(signUpRequest.getType());
        user.setActive(false);
        userRepo.save(user);

        if (user.getIsTenant()){
            Tenant tenant = new Tenant(user.getUsername(),user.getId());
            tenantRepo.save(tenant);
        }
        else{
            Landlord landlord = new Landlord(user.getUsername(),user.getId());
            landlordRepo.save(landlord);
        }
    }

    private User getUserFromDB(String id){
      return userRepo.findById(id).orElseThrow(() -> new UsernameNotFoundException("User Not Found with userId:" + id));
    }
    private Tenant getTenantFromDB(String id){
        return tenantRepo.findById(id).orElseThrow(() -> new UsernameNotFoundException("Tenant Not Found with userId:" + id));
    }
    private Landlord getLandlordFromDB(String id){
        return landlordRepo.findById(id).orElseThrow(() -> new UsernameNotFoundException("Landlord Not Found with userId:" + id));
    }
    public boolean ActivateUser(){
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = getUserFromDB(userDetails.getId());
        if (user.getIsTenant()){
            Tenant tenant = getTenantFromDB(user.getId());
            if (valider.ValidTenantDatas(tenant)){
                user.setActive(true);
                userRepo.save(user);

                ActiveTenant activeTenant = new ActiveTenant(tenant.getId());
                activeTenantRepo.save(activeTenant);
                return true;
            }
        }
        else {
            Landlord landlord = getLandlordFromDB(user.getId());
            if (valider.ValidLandlordDatas(landlord)){
                user.setActive(true);
                userRepo.save(user);

                ActiveLandlord activeLandlord = new ActiveLandlord(landlord.getId());
                activeLandlordRepo.save(activeLandlord);
                return true;
            }
        }
        System.out.println("Activation issue with"+user.getId());
        return false;
    }
    public boolean DeactivateUser() {
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = getUserFromDB(userDetails.getId());
        if (user.getIsTenant()){
            Tenant tenant = getTenantFromDB(user.getId());
            if (valider.ValidTenantDatas(tenant)){
                user.setActive(false);
                userRepo.save(user);

                ActiveTenant activeTenant = new ActiveTenant(tenant.getId());
                activeTenantRepo.delete(activeTenant);
                return false;
            }
        }
        else {
            Landlord landlord = getLandlordFromDB(user.getId());
            if (valider.ValidLandlordDatas(landlord)){
                user.setActive(false);
                userRepo.save(user);

                ActiveLandlord activeLandlord = new ActiveLandlord(landlord.getId());
                activeLandlordRepo.delete(activeLandlord);
                return false;
            }
        }
        System.out.println("Deactivation issue with"+user.getId());
        return true;
    }

    public Object getIsActive() {
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return userDetails.getActive();
    }
}