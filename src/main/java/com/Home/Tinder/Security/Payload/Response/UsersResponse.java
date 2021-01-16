package com.Home.Tinder.Security.Payload.Response;

import com.Home.Tinder.Model.Photo;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.bson.types.Binary;

import java.util.List;

@Data
public class UsersResponse {
    private String id;
    private String username;
    private List<Binary> photos;
    private int likes;
    private String description;

}