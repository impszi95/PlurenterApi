package com.Home.Tinder.Service;

import com.Home.Tinder.Model.Photo;
import com.Home.Tinder.Model.User;
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
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
public class PhotoService {

    @Autowired
    private UserRepo userRepo;

    public String addPhoto(MultipartFile file, String userId) throws Exception {
        Photo photo = new Photo();

        BufferedImage img = ConvertBytesToImage(file.getBytes());
        BufferedImage resized = ResizeImage(img);
        byte[] resizedByte = ConvertImageToBytes(resized);

        photo.setImage(
                new Binary(BsonBinarySubType.BINARY, resizedByte));

        //Link photo to its user also
        User user = userRepo.findById(userId)
                .orElseThrow(() -> new UsernameNotFoundException("User Not Found with userId: " + userId));
        user.addPhoto(photo);
        userRepo.save(user);

        return photo.getId();
    }
    BufferedImage ResizeImage(BufferedImage originalImage) throws Exception {
        Dimension imgSize = new Dimension(originalImage.getWidth(), originalImage.getHeight());
        Dimension boundary = new Dimension(600, 600);
        Dimension targetDimension = getScaledDimension(imgSize,boundary);

        return Scalr.resize(originalImage, Scalr.Method.QUALITY, targetDimension.width,targetDimension.height);
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

    public static Dimension getScaledDimension(Dimension imgSize, Dimension boundary) {

        int original_width = imgSize.width;
        int original_height = imgSize.height;
        int bound_width = boundary.width;
        int bound_height = boundary.height;
        int new_width = original_width;
        int new_height = original_height;

        // first check if we need to scale width
        if (original_width > bound_width) {
            //scale width to fit
            new_width = bound_width;
            //scale height to maintain aspect ratio
            new_height = (new_width * original_height) / original_width;
        }

        // then check if we need to scale even with the new height
        if (new_height > bound_height) {
            //scale height to fit instead
            new_height = bound_height;
            //scale width to maintain aspect ratio
            new_width = (new_height * original_width) / original_height;
        }

        return new Dimension(new_width, new_height);
    }
}