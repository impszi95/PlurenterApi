package com.Home.Plurenter.Controller;

import com.Home.Plurenter.Service.UserDetailsImpl;
import com.Home.Plurenter.Model.Photo;
import com.Home.Plurenter.Service.PhotoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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
