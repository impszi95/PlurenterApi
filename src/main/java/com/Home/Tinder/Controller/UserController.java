package com.Home.Tinder.Controller;

//import com.Home.Tinder.Model.Role;
import com.Home.Tinder.Model.Photo;
import com.Home.Tinder.Model.User;
//import com.Home.Tinder.Repo.RoleRepository;
import com.Home.Tinder.Repo.UserRepo;
import com.Home.Tinder.Security.Payload.Response.UsersResponse;
import com.Home.Tinder.Service.PhotoService;
import com.Home.Tinder.Service.UserDetailsImpl;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;

@CrossOrigin
@RestController
public class UserController {
//    @Autowired
//    UserRepo userRepo;
//
//    @Autowired
//    RoleRepository roleRepository;
//
//    @GetMapping("/roles")
//    @CrossOrigin
//    public ResponseEntity<List<Role>> getRoles() {
//        List <Role> roles = roleRepository.findAll();
//        return new ResponseEntity < >(roles, HttpStatus.OK);
//    }
//
//    @PostMapping("/role")
//    public ResponseEntity<Role> AddUser(@Valid @RequestBody Role role){
//        roleRepository.save(role);
//        return ResponseEntity.ok(role);
//    }

    @Autowired
    UserRepo userRepo;

    @Autowired
    AuthenticationManager authenticationManager;

//    @Autowired
//    private ModelMapper modelMapper;

    @Autowired
    private PhotoService photoService;

    @GetMapping("/getAlluser")
        public ResponseEntity<?> AllUser(){
        List<User> users = userRepo.findAll();

        List<UsersResponse> usersResponse = users.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());

            return ResponseEntity.ok(usersResponse);
        }

    @GetMapping("/usersCount")
    public ResponseEntity<Long> CountUsers(){
        Long count = userRepo.count();
        return new ResponseEntity<>(count,HttpStatus.OK);
    }

    private UsersResponse convertToDto(User user) {
        UsersResponse res = new UsersResponse();
        res.setUsername(user.getUsername());
        res.setLikes(user.getLikes());
        res.setId(user.getId());
        List<Photo> photos = user.getPhotos();
        res.setPhotos(photos.stream().map(x->x.getImage()).collect(Collectors.toList()));
        return res;
    }

    @GetMapping("/getUsersLikes")
    public ResponseEntity<Integer> GetUsersLikes(){
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return new ResponseEntity<>(userDetails.getLikes(),HttpStatus.OK);
    }

    }


