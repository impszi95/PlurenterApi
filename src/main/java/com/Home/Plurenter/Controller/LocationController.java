package com.Home.Plurenter.Controller;

import com.Home.Plurenter.Service.LocationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin
@RestController
public class LocationController {

    @Autowired
    LocationService locationService;

    @GetMapping("/location/getCountries")
    public ResponseEntity<?> getCountries(){
        return ResponseEntity.ok(locationService.getCountries());
    }

    @GetMapping("/location/getStates")
    public ResponseEntity<?> getStates(){
        return ResponseEntity.ok(locationService.getStates());
    }

    @GetMapping("/location/getCities")
    public ResponseEntity<?> getCities(){
        return ResponseEntity.ok(locationService.getCities());
    }
}






