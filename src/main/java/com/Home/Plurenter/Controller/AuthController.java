package com.Home.Plurenter.Controller;

import javax.validation.Valid;
//
//import com.Home.Tinder.Model.ERole;
//import com.Home.Tinder.Model.Role;
import com.Home.Plurenter.Service.UserDetailsImpl;
import com.Home.Plurenter.Model.User;
//import com.Home.Tinder.Repo.RoleRepository;
import com.Home.Plurenter.Repo.UserRepo;
import com.Home.Plurenter.Security.Payload.Request.LoginRequest;
import com.Home.Plurenter.Security.Payload.Request.SignupRequest;
import com.Home.Plurenter.Security.Payload.Response.JwtResponse;
import com.Home.Plurenter.Security.Payload.Response.MessageResponse;
import com.Home.Plurenter.Security.jwt.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/auth")
public class AuthController {
    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    UserRepo userRepo;
//
//    @Autowired
//    RoleRepository roleRepository;

    @Autowired
    PasswordEncoder encoder;

    @Autowired
    JwtUtils jwtUtils;

    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {


        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
//        List<String> roles = userDetails.getAuthorities().stream()
//                .map(item -> item.getAuthority())
//                .collect(Collectors.toList());

        return ResponseEntity.ok(new JwtResponse(jwt,
                userDetails.getId(),
                userDetails.getUsername()
//                roles
        ));
    }

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signUpRequest) {
        if (userRepo.existsByUsername(signUpRequest.getUsername())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Username is already taken!"));
        }


        // Create new user's account
        User user = new User(signUpRequest.getUsername(),
                encoder.encode(signUpRequest.getPassword()));

        userRepo.save(user);

        //Put his id to meets hasset
        User _user = userRepo.findByUsername(user.getUsername()).orElseThrow(() -> new UsernameNotFoundException("Username not exists"));
        _user.addPreviousMeets(user.getId());
        userRepo.save(_user);

        return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
    }
}