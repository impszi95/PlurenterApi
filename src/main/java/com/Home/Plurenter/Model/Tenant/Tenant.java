package com.Home.Plurenter.Model.Tenant;

import com.Home.Plurenter.Model.MinRentTime;
import org.springframework.data.annotation.Id;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class Tenant{
    @Id
    private String id;

    @NotBlank
    private String username;

    private MinRentTime minRentTime;

    private String job;

    public Tenant(String username, String id){
        this.username = username;
        this.id = id;
        this.minRentTime = new MinRentTime();
        this.job = "";
    }

    public String getId(){
        return this.id;
    }
    public MinRentTime getMinRentTime(){return this.minRentTime;}
    public void setMinRentTime(MinRentTime minRentTime) {
        this.minRentTime = minRentTime;
    }
    public String getJob(){return this.job;}
    public void setJob(String job){this.job = job;}
}
