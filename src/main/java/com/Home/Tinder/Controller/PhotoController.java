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
    public ResponseEntity<?> addPhoto(@RequestParam("userId") String userId, @RequestParam("file") MultipartFile image) throws Exception {

        String id = photoService.addPhoto(image,userId);

        return ResponseEntity.ok(id);
    }

    //Működik jól, visszaadja a base64es dekódolt képet a db-ből, de baromi lassu és nagy méretű a kódolás.
    //Inkább url megoldást használok

//   @GetMapping("/photos/{id}")
//   public ResponseEntity<?> getPhoto(@PathVariable String userId, Model model) {
//       Photo photo = photoService.getPhoto(userId);
//       model.addAttribute("image",
//               Base64.getEncoder().encodeToString(photo.getImage().getData()));
//       return ResponseEntity.ok(model);
//   }

    @GetMapping("/photos")
    public ResponseEntity<?> getAllPhotos(){

        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        List<Photo> photos = photoService.finAllByUserId(userDetails.getId());

        return ResponseEntity.ok(photos);
    }
}
