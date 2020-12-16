package com.Home.Tinder.Controller;

import com.Home.Tinder.Model.Photo;
import com.Home.Tinder.Model.User;
import com.Home.Tinder.Repo.UserRepo;
import com.Home.Tinder.Service.PhotoService;
import com.Home.Tinder.Service.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

@CrossOrigin(origins = "*")
@RestController
public class PhotoController {

    @Autowired
    PhotoService photoService;


    @PostMapping(value = "/photos/add", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> addPhoto(@RequestParam("file") MultipartFile image) throws Exception {
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String id = photoService.addPhoto(image,userDetails.getId());
        return ResponseEntity.ok(id);
    }

    @GetMapping("/photos")
    public ResponseEntity<?> getAllPhotos(){
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        List<Photo> photos = userDetails.getPhotos();
        return ResponseEntity.ok(photos);
    }

    @DeleteMapping("/deletePhoto/{photoId}")
    public void deletePhoto(@PathVariable String photoId){
        photoService.DeletePhoto(photoId);
    }
}
