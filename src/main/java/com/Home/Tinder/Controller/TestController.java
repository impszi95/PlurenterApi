package com.Home.Tinder.Controller;

//import com.Home.Tinder.Model.Role;
import com.Home.Tinder.Model.User;
//import com.Home.Tinder.Repo.RoleRepository;
import com.Home.Tinder.Repo.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
public class TestController {
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
    @GetMapping("/user")
        public ResponseEntity<List<User>> AllUser(){

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken("bence", "pass"));

        List<User> users = userRepo.findAll();
            return new ResponseEntity<>(users,HttpStatus.OK);
        }
    }

