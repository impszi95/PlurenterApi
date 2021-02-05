package com.Home.Plurenter.Security.Payload.Request;


import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.Set;

@Data
public class SignupRequest {
    @NotBlank
    private String email;

    @NotBlank
    private String name;

    @NotBlank
    @Size(min = 8)
    private String password;

    @NotBlank
    private String type;
    @NotBlank
    private boolean terms;
}
