package com.Home.Tinder.Model;

import org.bson.BsonBinarySubType;
import org.bson.types.Binary;
import org.springframework.data.annotation.Id;
import org.springframework.util.ResourceUtils;

import javax.imageio.ImageIO;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class User {
    @Id
    private String id;

    @NotBlank
    @Size(max = 20)
    private String username;

    @NotBlank
    @Size(max = 120)
    private String password;



//    @DBRef
//    private Set<Role> roles;

    private HashSet<String> previousMeets;

    private String actualMeetId;

    private int likes;

    private List<Photo> photos;

    public User(String username, String password) {
        this.username = username;
        this.password = password;
        this.likes = 0;
        this.actualMeetId = "";
        this.photos = new ArrayList<>();
        this.previousMeets = new HashSet<>();
    }


    public int getLikes(){
        return likes;
    }
    public void setLikes(int likes){
        this.likes = likes;
    }
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public List<Photo> getPhotos(){
        return photos;
    }

    public void addPhoto(Photo photo) {
        this.photos.add(photo);
    }

    public HashSet<String> getPreviousMeets() {
        return previousMeets;
    }

    public void addPreviousMeets(String userId) {
        this.previousMeets.add(userId);
    }

    public String getActualMeetId() {
        return actualMeetId;
    }

    public void setActualMeetId(String actualMeetId) {
        this.actualMeetId = actualMeetId;
    }

//    public Set<Role> getRoles() {
//        return roles;
//    }
//
//    public void setRoles(Set<Role> roles) {
//        this.roles = roles;
//    }
}
