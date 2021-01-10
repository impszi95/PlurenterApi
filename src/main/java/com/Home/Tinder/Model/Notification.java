package com.Home.Tinder.Model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
public class Notification {
    String userIdX;
    String usernameX;
    byte[] thumbnailX;

    String userIdY;
    String usernameY;
    byte[] thumbnailY;
}
