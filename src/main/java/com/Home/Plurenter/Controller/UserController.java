package com.Home.Plurenter.Controller;

//import com.Home.Tinder.Model.Role;
//import com.Home.Tinder.Repo.RoleRepository;
import com.Home.Plurenter.Model.Landlord.LandlordInfo;
import com.Home.Plurenter.Model.Tenant.TenantInfo;
import com.Home.Plurenter.Repo.UserRepo;
import com.Home.Plurenter.Security.Payload.Response.JwtResponse;
import com.Home.Plurenter.Security.Payload.Response.Match.MatchResponse;
import com.Home.Plurenter.Service.PhotoService;
import com.Home.Plurenter.Service.UserDetailsImpl;
import com.Home.Plurenter.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("/getIsActive")
    public ResponseEntity<?> getIsActive() {
        return ResponseEntity.ok(userService.getIsActive());
    }

    @GetMapping("/usersCount")
    public ResponseEntity<Long> CountUsers() {
        Long count = userRepo.count();
        return new ResponseEntity<>(count, HttpStatus.OK);
    }

    @GetMapping("/match/{matchId}")
    public ResponseEntity<?> getMatch(@PathVariable String matchId) {
        MatchResponse matchResponse = userService.GetMatch(matchId);
        return ResponseEntity.ok(matchResponse);
    }

    @PostMapping(value = "/tenant/save")
    public ResponseEntity<?> saveTenantInfos(@RequestBody TenantInfo infos) {
        userService.SaveTenantInfos(infos);
        return ResponseEntity.ok(infos);
    }
    @GetMapping("/getTenantInfo")
    public ResponseEntity<?> getTenantInfo() {
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        TenantInfo tenantInfo = userService.GetTenant();
        return ResponseEntity.ok(tenantInfo);
    }
    @PostMapping(value = "/landlord/save")
    public ResponseEntity<?> saveLandlordInfos(@RequestBody LandlordInfo infos) {
        userService.SaveLandlordInfos(infos);
        return ResponseEntity.ok(infos);
    }
    @GetMapping("/getLandlordInfo")
    public ResponseEntity<?> getLandlordInfo() {
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        LandlordInfo landlordInfo = userService.GetLandlord();
        return ResponseEntity.ok(landlordInfo);
    }
    @PostMapping(value = "/activateUser")
    public ResponseEntity<?> activateUser() {
        boolean activated = userService.ActivateUser();
        return ResponseEntity.ok(activated);
    }
    @PostMapping(value = "/deactivateUser")
    public ResponseEntity<?> deactivateUser() {
        boolean active = userService.DeactivateUser();
        return ResponseEntity.ok(active);
    }
}


