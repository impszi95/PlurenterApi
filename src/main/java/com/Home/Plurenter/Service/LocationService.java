package com.Home.Plurenter.Service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

@Service
public class LocationService {
    @Value("classpath:countries.json")
    Resource countires;

    @Value("classpath:states.json")
    Resource states;

    @Value("classpath:cities.json")
    Resource cities;

    public Resource getCountries(){
        return this.countires;
    }
    public Resource getStates(){
        return this.states;
    }
    public Resource getCities(){
        return this.cities;
    }
}
