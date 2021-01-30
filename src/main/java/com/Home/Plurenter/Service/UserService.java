package com.Home.Plurenter.Service;

import com.Home.Plurenter.Model.*;
import com.Home.Plurenter.Repo.LandlordRepo;
import com.Home.Plurenter.Repo.TenantRepo;
import com.Home.Plurenter.Repo.UserRepo;
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

    public void SaveTenantInfos(TenantInfo infos) {
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userRepo.findById(userDetails.getId()).orElseThrow(() -> new UsernameNotFoundException("User Not Found with userId: " + userDetails.getId()));
        Tenant tenant = tenantRepo.findByCommonId(user.getId()).orElseThrow(() -> new UsernameNotFoundException("User Not Found with userId: " + user.getId()));
        user.setDescription(infos.getDescription()); //common prop
        tenant.setMinRentTime(infos.getMinRentTime()); //uniqe prop
        userRepo.save(user);
        tenantRepo.save(tenant);
    }
    public void SaveLandlordInfos(LandlordInfo infos) {
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userRepo.findById(userDetails.getId()).orElseThrow(() -> new UsernameNotFoundException("User Not Found with userId: " + userDetails.getId()));
        Landlord landlord = landlordRepo.findByCommonId(user.getId()).orElseThrow(() -> new UsernameNotFoundException("User Not Found with userId: " + user.getId()));
        user.setDescription(infos.getDescription()); //common prop
        landlord.setMinRentTime(infos.getMinRentTime()); //unique prop
        userRepo.save(user);
        landlordRepo.save(landlord);
    }
    //<----- divide to tenant/landlord!!!
    public MatchResponse GetMatch(String matchId) {
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        boolean validMatch = userDetails.getMatchedMeets().contains(matchId);
        if (validMatch){
            User matchedUser = userRepo.findById(matchId).orElseThrow(() -> new UsernameNotFoundException("User Not Found with userId: " + matchId));
            if (userDetails.getIsTenant() && !matchedUser.getIsTenant()){//Match is a valid landlord
                Landlord landlord = landlordRepo.findByCommonId(matchedUser.getId()).orElseThrow(() -> new UsernameNotFoundException("User Not Found with userId: " + matchedUser.getId()));
                LandlordMatchResponse landlordMatchResponse = new LandlordMatchResponse();
                landlordMatchResponse.setUsername(matchedUser.getUsername());
                landlordMatchResponse.setDescription(matchedUser.getDescription());
                landlordMatchResponse.setPhotos(matchedUser.getPhotos());
                landlordMatchResponse.setTenant(matchedUser.getIsTenant());

                landlordMatchResponse.setMinRentTime(landlord.getMinRentTime().toString());
                return landlordMatchResponse;
            }
            if (!userDetails.getIsTenant() && matchedUser.getIsTenant()) { //Match is a valid tenant
                Tenant tenant = tenantRepo.findByCommonId(matchedUser.getId()).orElseThrow(() -> new UsernameNotFoundException("User Not Found with userId: " + matchedUser.getId()));
                TenantMatchResponse tenantMatchResponse = new TenantMatchResponse();
                tenantMatchResponse.setUsername(matchedUser.getUsername());
                tenantMatchResponse.setDescription(matchedUser.getDescription());
                tenantMatchResponse.setPhotos(matchedUser.getPhotos());
                tenantMatchResponse.setTenant(matchedUser.getIsTenant());

                tenantMatchResponse.setMinRentTime(tenant.getMinRentTime().toString());
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
        boolean validTenant = userDetails.getIsTenant();
        if (validTenant) {
            User user = userRepo.findById(userDetails.getId()).orElseThrow(() -> new UsernameNotFoundException("User Not Found with userId: " + userDetails.getId()));
            Tenant tenant = tenantRepo.findByCommonId(user.getId()).orElseThrow(() -> new UsernameNotFoundException("User Not Found with userId: " + user.getId()));

            TenantInfo tenantInfo = new TenantInfo();
            tenantInfo.setLikes(user.getLikes());
            tenantInfo.setDescription(user.getDescription());
            tenantInfo.setMinRentTime(tenant.getMinRentTime());
            return tenantInfo;
        }
        System.out.println("Unauthorized tenant query!");
        return new TenantInfo();
    }
    public LandlordInfo GetLandlord(){ //Self
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        boolean validLandlord = !userDetails.getIsTenant();
        if (validLandlord) {
            User user = userRepo.findById(userDetails.getId()).orElseThrow(() -> new UsernameNotFoundException("User Not Found with userId: " + userDetails.getId()));
            Landlord landlord = landlordRepo.findByCommonId(user.getId()).orElseThrow(() -> new UsernameNotFoundException("User Not Found with userId: " + user.getId()));

            LandlordInfo landlordInfo = new LandlordInfo();
            landlordInfo.setLikes(user.getLikes());
            landlordInfo.setDescription(user.getDescription());
            landlordInfo.setMinRentTime(landlord.getMinRentTime());
            return landlordInfo;
        }
        System.out.println("Unauthorized landlord query!");
        return new LandlordInfo();
    }
    public void CreateNewUser(SignupRequest signUpRequest){
        User user = new User(signUpRequest.getUsername(),
                encoder.encode(signUpRequest.getPassword()));
        user.setIsTenant(signUpRequest.getType());
        userRepo.save(user);

        User _user = userRepo.findById(user.getId()) .orElseThrow(() -> new UsernameNotFoundException("user Not Found with userId:" + user.getId()));

        if (_user.getIsTenant()){
            Tenant tenant = new Tenant(_user.getUsername(),_user.getId());
            tenantRepo.save(tenant);
        }
        else{
            Landlord landlord = new Landlord(_user.getUsername(),_user.getId());
            landlordRepo.save(landlord);
        }
    }
}