package com.Home.Plurenter.Controller;

import com.Home.Plurenter.Model.Tenant.TenantInfo;
import com.Home.Plurenter.Security.Payload.Response.Match.MatchResponse;
import com.Home.Plurenter.Service.LocationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
public class LocationController {

    @Autowired
    LocationService locationService;

    @GetMapping("/location/getCountries")
    public ResponseEntity<?> getCountries(){
        return ResponseEntity.ok(locationService.getCountries());
    }

    @GetMapping("/location/getStates/{country}")
    public ResponseEntity<?> getStates(@PathVariable String country){
        return ResponseEntity.ok(locationService.getStates(country));
    }
    @GetMapping("/location/getCities/{country}/{state}")
    public ResponseEntity<?> getCities(@PathVariable String country,@PathVariable String state){
        return ResponseEntity.ok(locationService.getCities(country, state));
    }
}






