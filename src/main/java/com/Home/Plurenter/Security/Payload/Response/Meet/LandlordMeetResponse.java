package com.Home.Plurenter.Security.Payload.Response.Meet;

import com.Home.Plurenter.Model.Rent;
import lombok.Data;

@Data
public class LandlordMeetResponse extends MeetResponse {
    private String rent;
}
