package com.Home.Plurenter.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;

@Service
public class LocationService {
    @Value("classpath:countries_mini.json")
    Resource countires;

    @Autowired
    ResourceLoader resourceLoader;

    public Resource getCountries(){
        return this.countires;
    }
    public Resource getStates(String country){
        return resourceLoader.getResource(
                "classpath:Countries/"+country+"/states.json");
    }
    public Resource getCities(String country, String state){
        return resourceLoader.getResource(
                "classpath:Countries/"+country+"/"+state+"/cities.json");
    }
}
