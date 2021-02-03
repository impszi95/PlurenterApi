package com.Home.Plurenter.Model.Landlord;

import com.Home.Plurenter.Model.MinRentTime;
import com.Home.Plurenter.Model.Rent;
import lombok.Data;

@Data
public class LandlordInfo {
    private int likes;
    private MinRentTime minRentTime;
    private boolean active;
    private String description;
    private Rent rent;
    private boolean canActivate;

    public LandlordInfo(){
        this.likes = 0;
        this.minRentTime = new MinRentTime();
        this.minRentTime.setYear(0);
        this.minRentTime.setMonth(0);
        this.minRentTime.setDay(0);
        this.active = false;
        this.description = "";
        this.rent = new Rent(0,"","");
        this.canActivate = false;
    }
}
