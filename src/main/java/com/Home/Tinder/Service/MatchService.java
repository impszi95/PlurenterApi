package com.Home.Tinder.Service;

import com.Home.Tinder.Model.Match;
import com.Home.Tinder.Model.Photo;
import com.Home.Tinder.Model.User;
import com.Home.Tinder.Repo.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class MatchService {
    @Autowired
    private UserRepo userRepo;

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
