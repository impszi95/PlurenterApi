package com.Home.Plurenter.Security.Payload.Response.Match;

import com.Home.Plurenter.Model.Photo;
import lombok.Data;

@Data
public class MatchThumbnail {
    private String id;
    private String displayName;
    private Photo thumbnail;
    //date
}
