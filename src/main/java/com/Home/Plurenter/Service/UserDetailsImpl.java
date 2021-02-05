package com.Home.Plurenter.Service;

import java.util.*;

import com.Home.Plurenter.Model.Photo;
import com.Home.Plurenter.Model.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;


public class UserDetailsImpl implements UserDetails {
    private static final long serialVersionUID = 1L;

    private String id;

    private String email;

    private String name;

    @JsonIgnore
    private String password;

    private boolean isTenant;

    private String description;

//    private Collection<? extends GrantedAuthority> authorities;

    private int likes;

    private String actualMeetId;

    private List<Photo> photos;

    private HashSet<String> previousMeets;

    private HashSet<String> matchedMeets;

    private boolean active;

    private boolean canActivate;

    public UserDetailsImpl(
            String id, String email, String name, boolean isTenant, int likes, String password, String description, List<Photo> photos,
            String actualMeetId, HashSet<String> previousMeets, HashSet<String> matchedMeets, boolean active, boolean canActivate
//            ,Collection<? extends GrantedAuthority> authorities
    ) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.name = name;
        this.isTenant = isTenant;
        this.description = description;
//        this.authorities = authorities;
        this.likes = likes;
        this.photos = photos;
        this.actualMeetId = actualMeetId;
        this.previousMeets = previousMeets;
        this.matchedMeets = matchedMeets;
        this.active = active;
        this.canActivate = canActivate;
    }

    public static UserDetailsImpl build(User user) {
//        List<GrantedAuthority> authorities = user.getRoles().stream()
//                .map(role -> new SimpleGrantedAuthority(role.getName().name()))
//                .collect(Collectors.toList());

        return new UserDetailsImpl(
                user.getId(),
                user.getEmail(),
                user.getName(),
                user.getIsTenant(),
                user.getLikes(),
                user.getPassword(),
                user.getDescription(),
                user.getPhotos(),
                user.getActualMeetId(),
                user.getPreviousMeets(),
                user.getMatchedMeets(),
                user.getActive(),
                user.getCanActivate()
//                authorities
                );
    }
    public String getId() {
        return id;
    }

    public int getLikes() {
        return likes;
    }

    public boolean getIsTenant(){return isTenant;}

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
        return this.email;
    }
    public String getEmail(){
        return this.email;
    }

    public String getName(){return this.name;}

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

    public boolean getActive(){return this.active;}

    public boolean canActivate(){return this.canActivate;}
}