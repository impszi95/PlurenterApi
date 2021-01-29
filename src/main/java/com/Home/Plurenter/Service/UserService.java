package com.Home.Plurenter.Service;

import com.Home.Plurenter.Model.*;
import com.Home.Plurenter.Repo.LandlordRepo;
import com.Home.Plurenter.Repo.TenantRepo;
import com.Home.Plurenter.Repo.UserRepo;
import com.Home.Plurenter.Security.Payload.Request.SignupRequest;
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

    public void saveUserInfos(UserInfo infos) {
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userRepo.findById(userDetails.getId()).orElseThrow(() -> new UsernameNotFoundException("User Not Found with userId: " + userDetails.getId()));
        user.setDescription(infos.getDescription());
        userRepo.save(user);
    }

    public Match getMatch(String matchId) {
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        boolean validMatch = userDetails.getMatchedMeets().contains(matchId);
        if (validMatch){
            User matchedUser = userRepo.findById(matchId)
                    .orElseThrow(() -> new UsernameNotFoundException("User Not Found with userId: " + matchId));
            Match match = new Match();
            match.setUsername(matchedUser.getUsername());
            match.setDescription(matchedUser.getDescription());
            match.setPhotos(matchedUser.getPhotos());
            return match;
        }
        System.out.println("Unauthorized match query!");
        return new Match();
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