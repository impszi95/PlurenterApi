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

    @Override
    public boolean equals(Object obj){
        if (obj == null) {
            return false;
        }

        if (obj.getClass() != this.getClass()) {
            return false;
        }
        final Location other = (Location) obj;
        if (this.getCountry_id() != other.getCountry_id() ||
                this.getState_id()!=other.getState_id() ||
                this.getCity_id() != other.getCity_id()) {
            return false;
        }
        return true;
    }
}
