package com.Home.Plurenter.Model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
public class Notification {
    String userIdX;
    String nameX;
    byte[] thumbnailX;

    String userIdY;
    String nameY;
    byte[] thumbnailY;
}
