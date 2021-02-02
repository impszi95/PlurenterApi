package com.Home.Plurenter.Model;

import lombok.Data;

@Data
public class LandlordInfo {
    private int likes;
    private MinRentTime minRentTime;
    private boolean active;
    private String description;
    private Rent rent;

    public LandlordInfo(){
        this.likes = 0;
        this.minRentTime = new MinRentTime();
        this.minRentTime.setYear(0);
        this.minRentTime.setMonth(0);
        this.minRentTime.setDay(0);
        this.active = false;
        this.description = "";
        this.rent = new Rent(0,"","");
    }
}
