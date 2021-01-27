package com.Home.Plurenter.Model;

import lombok.Data;

import java.util.List;

@Data
public class Match {
    String username;
    String description;
    List<Photo> photos;
}
