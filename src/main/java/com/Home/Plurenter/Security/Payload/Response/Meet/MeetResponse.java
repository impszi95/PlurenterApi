package com.Home.Plurenter.Security.Payload.Response.Meet;

import com.Home.Plurenter.Model.Location;
import com.Home.Plurenter.Model.Photo;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
public class MeetResponse {
        private String id;
        private boolean isTenant;
        private List<Photo> photos;
        private String minRentTime;
        private String description;
}
