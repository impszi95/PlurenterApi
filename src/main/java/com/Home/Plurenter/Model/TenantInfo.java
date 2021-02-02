package com.Home.Plurenter.Model;

import lombok.Data;

@Data
public class TenantInfo {
    private int likes;
    private MinRentTime minRentTime;
    private boolean active;
    private String description;
    private String job;

    public TenantInfo(){
        this.likes = 0;
        this.minRentTime = new MinRentTime();
        this.minRentTime.setYear(0);
        this.minRentTime.setMonth(0);
        this.minRentTime.setDay(0);
        this.active = false;
        this.description = "";
        this.job = "";
    }
}
