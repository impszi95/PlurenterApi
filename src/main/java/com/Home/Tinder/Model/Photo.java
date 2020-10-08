package com.Home.Tinder.Model;

import lombok.Data;
import org.bson.types.Binary;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "photos")
@Data
public class Photo {
    @Id
    private String id;
    private String userId;
    private Binary image;
}