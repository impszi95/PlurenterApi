package com.Home.Plurenter.Model.Landlord;

import lombok.AllArgsConstructor;
import org.springframework.data.annotation.Id;

@AllArgsConstructor
public class ActiveLandlord {
    @Id
    private String id;

    public String getId(){return this.id;}
}
