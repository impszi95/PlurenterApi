package com.Home.Plurenter.Security.Payload.Response;

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