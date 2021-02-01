package com.Home.Plurenter.Security.Payload.Response.Match;

import com.Home.Plurenter.Model.Rent;
import com.Home.Plurenter.Security.Payload.Response.Match.MatchResponse;
import lombok.Data;

@Data
public class LandlordMatchResponse extends MatchResponse {
    private String rent;
}
