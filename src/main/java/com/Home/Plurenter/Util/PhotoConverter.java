package com.Home.Plurenter.Util;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class PhotoConverter {
    public static BufferedImage ConvertBytesToImage(byte[] imageData) {
        ByteArrayInputStream bais = new ByteArrayInputStream(imageData);
        try {
            BufferedImage img = ImageIO.read(bais);
            return img;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public static byte[] ConvertImageToBytes(BufferedImage img) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        BufferedImage newImage = new BufferedImage( img.getWidth(), img.getHeight(), BufferedImage.TYPE_INT_RGB);
        newImage.createGraphics().drawImage(img,0,0,Color.WHITE,null);
        ImageIO.write(newImage, "jpg", baos);
        return baos.toByteArray();
    }
}
