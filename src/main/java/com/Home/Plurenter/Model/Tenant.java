package com.Home.Plurenter.Model;

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
    private String commonId;

    @NotBlank
    private String username;

    public Tenant(String username, String commonId){
        this.username = username;
        this.commonId = commonId;
    }

    public String getCommonId(){
        return this.commonId;
    }
}
