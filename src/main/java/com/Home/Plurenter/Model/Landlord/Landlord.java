package com.Home.Plurenter.Model.Landlord;

import com.Home.Plurenter.Model.MinRentTime;
import com.Home.Plurenter.Model.Rent;
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
    private String username;

    private MinRentTime minRentTime;

    private Rent rent;

    public Landlord(String username, String id){
        this.username = username;
        this.id = id;
        this.minRentTime = new MinRentTime();
        this.rent = new Rent(0,"","");
    }

    public String getId(){
        return this.id;
    }
    public MinRentTime getMinRentTime(){return this.minRentTime;}
    public void setMinRentTime(MinRentTime minRentTime) {
        this.minRentTime = minRentTime;
    }
    public Rent getRent(){return this.rent;}
    public void setRent(Rent rent){this.rent = rent;}
}
