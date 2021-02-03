package com.Home.Plurenter.Model.Tenant;

import lombok.AllArgsConstructor;
import org.springframework.data.annotation.Id;

@AllArgsConstructor
public class ActiveTenant {
    @Id
    private String id;

    public String getId(){return this.id;}
}
