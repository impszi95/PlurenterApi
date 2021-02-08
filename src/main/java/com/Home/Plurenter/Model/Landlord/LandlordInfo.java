package com.Home.Plurenter.Model.Landlord;

import ch.qos.logback.classic.spi.LoggerContextAware;
import com.Home.Plurenter.Model.Location;
import com.Home.Plurenter.Model.MinRentTime;
import com.Home.Plurenter.Model.Rent;
import lombok.Data;

import java.util.Locale;

@Data
public class LandlordInfo {
    private String name;
    private int likes;
    private int matches;
    private MinRentTime minRentTime;
    private boolean active;
    private String description;
    private  String phone;
    private Rent rent;
    private boolean canActivate;
    private Location location;

    public LandlordInfo() {
        this.name = "";
        this.likes = 0;
        this.matches = 0;
        this.minRentTime = new MinRentTime();
        this.minRentTime.setYear(0);
        this.minRentTime.setMonth(0);
        this.minRentTime.setDay(0);
        this.active = false;
        this.description = "";
        this.phone = "";
        this.rent = new Rent(0, "", "");
        this.canActivate = false;
        this.location = new Location("", "", "",-1,-1,-1);
    }
}
