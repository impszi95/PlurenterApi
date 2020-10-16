package com.Home.Tinder.Service;

import com.Home.Tinder.Model.Photo;
import com.Home.Tinder.Model.User;
import com.Home.Tinder.Repo.PhotoRepository;
import com.Home.Tinder.Repo.UserRepo;
import org.bson.BsonBinarySubType;
import org.bson.types.Binary;
import org.imgscalr.Scalr;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
public class PhotoService {

    @Autowired
    private PhotoRepository photoRepo;

    @Autowired
    private UserRepo userRepo;

    public String addPhoto(MultipartFile file, String userId) throws Exception {
        Photo photo = new Photo();
        photo.setUserId(userId);

        byte[] resizedImg = ConvertAndResizeFile(file.getBytes());

        photo.setImage(
                new Binary(BsonBinarySubType.BINARY, resizedImg));
        photo = photoRepo.insert(photo);

        //Link photo to its user also
        User user = userRepo.findById(userId)
                .orElseThrow(() -> new UsernameNotFoundException("User Not Found with userId: " + userId));
        user.addPhoto(photo);
        userRepo.save(user);

        return photo.getId();
    }

    public Photo getPhoto(String userId) {
        return photoRepo.findById(userId).get();
    }

    public List<Photo> finAllByUserId(String userId){
        return photoRepo.findByUserId(userId);
    }

    private byte[] ConvertAndResizeFile(byte[] imageData) throws Exception {
        BufferedImage img = ConvertBytesToImage(imageData);
        BufferedImage resized = ResizeImage(img, 500);
        return ConvertImageToBytes(resized);
    }

    BufferedImage ResizeImage(BufferedImage originalImage, int targetWidth) throws Exception {
        return Scalr.resize(originalImage, targetWidth);
    }

    private BufferedImage ConvertBytesToImage(byte[] imageData) {
        ByteArrayInputStream bais = new ByteArrayInputStream(imageData);
        try {
            return ImageIO.read(bais);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private byte[] ConvertImageToBytes(BufferedImage img) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(img, "jpg", baos);
        return baos.toByteArray();
    }
}