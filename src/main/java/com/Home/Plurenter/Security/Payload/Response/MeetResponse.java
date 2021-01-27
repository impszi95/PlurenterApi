package com.Home.Plurenter.Security.Payload.Response;

import com.Home.Plurenter.Model.Photo;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class MeetResponse {
        private String id;
        private String username;
        private List<Photo> photos;
}
