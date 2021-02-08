package com.Home.Plurenter.Model.Tenant;

import com.Home.Plurenter.Model.Location;
import com.Home.Plurenter.Model.MinRentTime;
import lombok.Data;

import java.util.Locale;

@Data
public class TenantInfo {
    private String name;
    private int likes;
    private int matches;
    private MinRentTime minRentTime;
    private boolean active;
    private String description;
    private String phone;
    private String job;
    private boolean canActivate;
    private Location location;

    public TenantInfo(){
        this.name="";
        this.likes = 0;
        this.matches = 0;
        this.minRentTime = new MinRentTime();
        this.minRentTime.setYear(0);
        this.minRentTime.setMonth(0);
        this.minRentTime.setDay(0);
        this.active = false;
        this.description = "";
        this.phone = "";
        this.job = "";
        this.canActivate = false;
        this.location = new Location("","","",  -1,-1,-1);
    }
}
