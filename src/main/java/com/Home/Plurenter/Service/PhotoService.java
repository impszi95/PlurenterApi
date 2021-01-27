package com.Home.Plurenter.Service;

import com.Home.Plurenter.Model.Photo;
import com.Home.Plurenter.Model.User;
import com.Home.Plurenter.Repo.UserRepo;
import com.Home.Plurenter.Util.PhotoConverter;
import com.Home.Plurenter.Util.PhotoResize;
import com.Home.Plurenter.Util.PhotoRotate;
import org.bson.BsonBinarySubType;
import org.bson.types.Binary;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.awt.image.BufferedImage;

@Service
public class PhotoService {

    @Autowired
    private UserRepo userRepo;

    public void DeletePhoto(String photoId) {
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String userId = userDetails.getId();

        User user = userRepo.findById(userId)
                .orElseThrow(() -> new UsernameNotFoundException("User Not Found with userId: " + userId));
        user.deletePhoto(photoId);
        userRepo.save(user);
    }

    public String addPhoto(MultipartFile file, String userId) throws Exception {
        Photo photo = new Photo();
        String id = new ObjectId().toString();
        photo.setId(id);

        BufferedImage img = PhotoConverter.ConvertBytesToImage(file.getBytes());
        BufferedImage rot = PhotoRotate.Rotate(file,img);

        BufferedImage resized = PhotoResize.ResizeImage(rot);
        byte[] resizedByte = PhotoConverter.ConvertImageToBytes(resized);
        photo.setImage(new Binary(BsonBinarySubType.BINARY, resizedByte));

        //Link photo to its user also
        User user = userRepo.findById(userId)
                .orElseThrow(() -> new UsernameNotFoundException("User Not Found with userId: " + userId));
        user.addPhoto(photo);
        userRepo.save(user);

        return photo.getId();
    }

    public byte[] getThumbnailForUser(String userId){
        User user = userRepo.findById(userId)
                .orElseThrow(() -> new UsernameNotFoundException("User Not Found with userId: " + userId));

        boolean photosNotEmpty = !user.getPhotos().isEmpty();
        if(photosNotEmpty){
            return user.getPhotos().get(0).getImage().getData();
        }
        return new byte[0];
    }
}