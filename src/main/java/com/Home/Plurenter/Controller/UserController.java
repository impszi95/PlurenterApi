package com.Home.Plurenter.Controller;

//import com.Home.Tinder.Model.Role;
import com.Home.Plurenter.Model.Match;
import com.Home.Plurenter.Model.Photo;
import com.Home.Plurenter.Model.User;
//import com.Home.Tinder.Repo.RoleRepository;
import com.Home.Plurenter.Model.UserInfo;
import com.Home.Plurenter.Repo.UserRepo;
import com.Home.Plurenter.Security.Payload.Response.UsersResponse;
import com.Home.Plurenter.Service.PhotoService;
import com.Home.Plurenter.Service.UserDetailsImpl;
import com.Home.Plurenter.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@CrossOrigin
@RestController
public class UserController {

    @Autowired
    UserRepo userRepo;

    @Autowired
    UserService userService;

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    private PhotoService photoService;

    @GetMapping("/getAlluser")
    public ResponseEntity<?> AllUser() {
        List<User> users = userRepo.findAll();

        List<UsersResponse> usersResponse = users.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());

        return ResponseEntity.ok(usersResponse);
    }

    @GetMapping("/usersCount")
    public ResponseEntity<Long> CountUsers() {
        Long count = userRepo.count();
        return new ResponseEntity<>(count, HttpStatus.OK);
    }

    private UsersResponse convertToDto(User user) {
        UsersResponse res = new UsersResponse();
        res.setUsername(user.getUsername());
        res.setLikes(user.getLikes());
        res.setId(user.getId());
        List<Photo> photos = user.getPhotos();
        res.setPhotos(photos.stream().map(x -> x.getImage()).collect(Collectors.toList()));
        res.setDescription(user.getDescription());
        return res;
    }

    @GetMapping("/getUsersLikes")
    public ResponseEntity<Integer> GetUsersLikes() {
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return new ResponseEntity<>(userDetails.getLikes(), HttpStatus.OK);
    }

    @GetMapping("/match/{matchId}")
    public ResponseEntity<?> getAllPhotos(@PathVariable String matchId) {
        Match match = userService.getMatch(matchId);
        return ResponseEntity.ok(match);
    }

    @PostMapping(value = "/user/save")
    public ResponseEntity<?> saveInfos(@RequestBody UserInfo infos) {
        userService.saveUserInfos(infos);
        return ResponseEntity.ok(infos);
    }

    @GetMapping("/description")
    public ResponseEntity<?> getDescription() {
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return ResponseEntity.ok(userDetails.getDescription());
    }
}


