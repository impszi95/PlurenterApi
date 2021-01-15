package com.Home.Tinder.Service;

import java.util.*;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.stream.Collectors;

import com.Home.Tinder.Model.Photo;
import com.Home.Tinder.Model.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.parameters.P;
import org.springframework.security.core.userdetails.UserDetails;


public class UserDetailsImpl implements UserDetails {
    private static final long serialVersionUID = 1L;

    private String id;

    private String username;

    @JsonIgnore
    private String password;

    private String description;

//    private Collection<? extends GrantedAuthority> authorities;

    private int likes;

    private String actualMeetId;

    private List<Photo> photos;

    private HashSet<String> previousMeets;

    private HashSet<String> matchedMeets;

    public UserDetailsImpl(
            String id, String username, int likes, String password,String description, List<Photo> photos,
            String actualMeetId, HashSet<String> previousMeets, HashSet<String> matchedMeets
//            ,Collection<? extends GrantedAuthority> authorities
    ) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.description = description;
//        this.authorities = authorities;
        this.likes = likes;
        this.photos = photos;
        this.actualMeetId = actualMeetId;
        this.previousMeets = previousMeets;
        this.matchedMeets = matchedMeets;
    }

    public static UserDetailsImpl build(User user) {
//        List<GrantedAuthority> authorities = user.getRoles().stream()
//                .map(role -> new SimpleGrantedAuthority(role.getName().name()))
//                .collect(Collectors.toList());

        return new UserDetailsImpl(
                user.getId(),
                user.getUsername(),
                user.getLikes(),
                user.getPassword(),
                user.getDescription(),
                user.getPhotos(),
                user.getActualMeetId(),
                user.getPreviousMeets(),
                user.getMatchedMeets()
//                authorities
                );
    }
    public String getId() {
        return id;
    }

    public int getLikes() {
        return likes;
    }


    public List<Photo> getPhotos(){
        return photos;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        UserDetailsImpl user = (UserDetailsImpl) o;
        return Objects.equals(id, user.id);
    }

    public HashSet<String> getGetPreviousMeets() {
        return previousMeets;
    }

    public String getActualMeetId(){return  this.actualMeetId;}

    public HashSet<String> getMatchedMeets(){return this.matchedMeets;}

    public String getDescription(){return this.description;}
}