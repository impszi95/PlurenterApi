package com.Home.Plurenter.Service;

import com.Home.Plurenter.Model.Match;
import com.Home.Plurenter.Model.User;
import com.Home.Plurenter.Model.UserInfo;
import com.Home.Plurenter.Repo.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    private UserRepo userRepo;

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
}