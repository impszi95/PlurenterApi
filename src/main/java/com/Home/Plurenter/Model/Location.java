package com.Home.Plurenter.Model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Location {
    private String country;
    private String state;
    private String city;
    private int city_id;
    private int country_id;
    private int state_id;
}
