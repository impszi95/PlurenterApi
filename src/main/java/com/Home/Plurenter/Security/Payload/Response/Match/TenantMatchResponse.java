package com.Home.Plurenter.Security.Payload.Response.Match;

import com.Home.Plurenter.Security.Payload.Response.Match.MatchResponse;
import lombok.Data;

@Data
public class TenantMatchResponse extends MatchResponse {
    private String job;
}
