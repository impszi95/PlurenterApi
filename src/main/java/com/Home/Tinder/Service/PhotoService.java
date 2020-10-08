package com.Home.Tinder.Service;

import com.Home.Tinder.Model.Photo;
import com.Home.Tinder.Model.User;
import com.Home.Tinder.Repo.PhotoRepository;
import com.Home.Tinder.Repo.UserRepo;
import org.bson.BsonBinarySubType;
import org.bson.types.Binary;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
public class PhotoService {

    @Autowired
    private PhotoRepository photoRepo;

    @Autowired
    private UserRepo userRepo;

    public String addPhoto(MultipartFile file, String userId) throws IOException {
        Photo photo = new Photo();
        photo.setUserId(userId);
        photo.setImage(
                new Binary(BsonBinarySubType.BINARY, file.getBytes()));
        photo = photoRepo.insert(photo);

        //Link photo to its user also
        User user = userRepo.findById(userId)
                .orElseThrow(() -> new UsernameNotFoundException("User Not Found with userId: " + userId));
        user.addPhoto(photo.getId());
        userRepo.save(user);

        return photo.getId();
    }

    public Photo getPhoto(String userId) {
        return photoRepo.findById(userId).get();
    }

    public List<Photo> finAllByUserId(String userId){
        return photoRepo.findByUserId(userId);
    }
}