package com.Home.Tinder.Util;

import com.drew.imaging.ImageMetadataReader;
import com.drew.imaging.ImageProcessingException;
import com.drew.metadata.Directory;
import com.drew.metadata.Metadata;
import com.drew.metadata.MetadataException;
import com.drew.metadata.exif.ExifIFD0Directory;
import com.drew.metadata.jpeg.JpegDirectory;
import org.springframework.web.multipart.MultipartFile;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;


public class PhotoRotate {
    public static ImageInformation readImageInformation(MultipartFile imageFile) throws IOException, MetadataException, ImageProcessingException {
        Metadata metadata = ImageMetadataReader.readMetadata(imageFile.getInputStream());
        Directory directory = metadata.getFirstDirectoryOfType(ExifIFD0Directory.class);
        JpegDirectory jpegDirectory = metadata.getFirstDirectoryOfType(JpegDirectory.class);

        int orientation = 1;
        try {
            orientation = directory.getInt(ExifIFD0Directory.TAG_ORIENTATION);
        } catch (MetadataException me) {
            System.out.println("Could not get orientation");
        }
        int width = jpegDirectory.getImageWidth();
        int height = jpegDirectory.getImageHeight();

        return new ImageInformation(orientation, width, height);
    }
    public static AffineTransform getExifTransformation(ImageInformation info) {

        AffineTransform t = new AffineTransform();

        switch (info.orientation) {
            case 1:
                // no correction necessary skip and return the image
                break;
            case 2: // Flip X
                t.scale(-1.0, 1.0);
                t.translate(-info.width, 0);

                break;
            case 3: // PI rotation
                t.translate(info.width, info.height);
                t.rotate(Math.PI);

                break;
            case 4: // Flip Y
                t.scale(1.0, -1.0);
                t.translate(0, -info.height);

                break;
            case 5: // - PI/2 and Flip X
                t.rotate(-Math.PI / 2);
                t.scale(-1.0, 1.0);

                break;
            case 6: // -PI/2 and -width
                t.translate(info.height,0);
                t.rotate(Math.PI / 2);

                break;
            case 7: // PI/2 and Flip
                t.scale(-1.0, 1.0);
                t.translate(info.height, 0);
                t.translate(0, info.width);
                t.rotate(  3 * Math.PI / 2);

                break;
            case 8: // PI / 2
                t.translate(0, info.width);
                t.rotate(  3 * Math.PI / 2);

                break;
        }

        return t;
    }
    public static BufferedImage Rotate(MultipartFile file, BufferedImage image) throws Exception {
        try {
            ImageInformation info = readImageInformation(file);
            AffineTransform transform = getExifTransformation(info);
            boolean isRotated = info.orientation >= 5;
            return transformImage(image, transform, isRotated);
        }catch (Exception e){
            return image;
        }
    }
    private static BufferedImage transformImage(BufferedImage image, AffineTransform transform, boolean isRotated) throws IOException {
            AffineTransformOp op = new AffineTransformOp(transform, AffineTransformOp.TYPE_BICUBIC);
            BufferedImage destinationImage = new BufferedImage(image.getWidth(), image.getHeight(), image.getType());
            if (isRotated) {
                destinationImage = new BufferedImage(image.getHeight(), image.getWidth(), image.getType());
            }
            destinationImage = op.filter(image, destinationImage);
            return destinationImage;
        }
    }