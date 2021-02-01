package com.Home.Plurenter.Model;

import org.springframework.data.annotation.Id;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class Landlord{
    @Id
    private String id;

    @NotBlank
    private String commonId;

    @NotBlank
    private String username;

    private MinRentTime minRentTime;

    private Rent rent;

    public Landlord(String username, String commonId){
        this.username = username;
        this.commonId = commonId;
        this.minRentTime = new MinRentTime();
        this.rent = new Rent(0,"","");
    }

    public String getCommonId(){
        return this.commonId;
    }
    public MinRentTime getMinRentTime(){return this.minRentTime;}
    public void setMinRentTime(MinRentTime minRentTime) {
        this.minRentTime = minRentTime;
    }
    public Rent getRent(){return this.rent;}
    public void setRent(Rent rent){this.rent = rent;}
}
