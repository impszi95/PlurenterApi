package com.Home.Plurenter.Security.Payload.Response.Match;

import com.Home.Plurenter.Model.Location;
import com.Home.Plurenter.Model.MinRentTime;
import com.Home.Plurenter.Model.Photo;
import lombok.Data;

import java.util.List;

@Data
public class MatchResponse {
    private String email;
    private String name;
    private String description;
    private List<Photo> photos;
    private boolean isTenant;
    private String minRentTime;
    private String phone;
}
