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

//    private Collection<? extends GrantedAuthority> authorities;

    private int likes;

    private List<Photo> photos;

    private HashSet<String> getPreviousMeets;

    public UserDetailsImpl(String id, String username, int likes, String password,List<Photo> photos, HashSet<String> getPreviousMeets
//            ,Collection<? extends GrantedAuthority> authorities
    ) {
        this.id = id;
        this.username = username;
        this.password = password;
//        this.authorities = authorities;
        this.likes = likes;
        this.photos = photos;
        this.getPreviousMeets = getPreviousMeets;
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
                user.getPhotos(),
                user.getPreviousMeets()
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
        return getPreviousMeets;
    }

    public void setGetPreviousMeets(HashSet<String> getPreviousMeets) {
        this.getPreviousMeets = getPreviousMeets;
    }
}