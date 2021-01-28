package com.Home.Plurenter.Model;

import org.springframework.data.annotation.Id;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.*;

public class User {
    @Id
    private String id;

    @NotBlank
    @Size(max = 20)
    private String username;

    @NotBlank
    @Size(max = 120)
    private String password;

    private boolean isTenant;

    @Size(max = 500)
    private String description;

//    @DBRef
//    private Set<Role> roles;

    private HashSet<String> previousMeets;

    private HashSet<String> matchedMeets;

    private HashSet<String> likedMeets;

    private HashSet<String> receivedLikesMeets;

    private String actualMeetId;

    private int likes;

    private List<Photo> photos;

    public User(String username, String password) {
        this.username = username;
        this.password = password;
        this.description = "";
        this.likes = 0;
        this.actualMeetId = "";
        this.photos = new ArrayList<>();
        this.previousMeets = new HashSet<>();
        this.matchedMeets = new HashSet<>();
        this.likedMeets = new HashSet<>();
        this.receivedLikesMeets = new HashSet<>();
        this.isTenant = true;
    }

    public boolean getIsTenant(){return this.isTenant;}
    public void setIsTenant(String type){
        if (type.equals("tenant")){
            this.isTenant = true;
        }
        else if (type.equals("landlord")){
            this.isTenant = false;
        }
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

    public HashSet<String> getMatchedMeets() {
        return matchedMeets;
    }

    public void addMatchedMeet(String matchedMeets) {
        this.matchedMeets.add(matchedMeets);
    }

    public HashSet<String> getLikedMeets() {
        return likedMeets;
    }

    public void removeLikedMeet(String meet){
        this.likedMeets.remove(meet);
    }

    public void addLikedMeet(String likedMeet) {
        this.likedMeets.add(likedMeet);
    }

    public HashSet<String> getReceivedLikesMeets() {
        return receivedLikesMeets;
    }

    public void addReceivedLikesMeets(String receivedLikesMeet) {
        this.receivedLikesMeets.add(receivedLikesMeet);
    }

    public void removeReceivedMeet(String meet){
        this.receivedLikesMeets.remove(meet);
    }

    public void deletePhoto(String photoId) {
        this.photos.removeIf(photo -> photo.getId().equals(photoId));
    }
    public void setDescription(String description){this.description = description;}
    public String getDescription(){return this.description;}
}
