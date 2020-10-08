package com.Home.Tinder.Security.Payload.Request;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotBlank;

@Data
public class PhotoRequest {
    @NotBlank
    private String userId;

    @NotBlank
    private MultipartFile image;
}
