package utils;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import main.Main;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.net.URL;

public class ImageUtils {
    public static ImageView loadImageView(String imagePath) {
        FileInputStream fileImage = null;
        try {
            URL url = ImageUtils.class.getResource(Main.IMAGE_PACKAGE + imagePath);
            fileImage = new FileInputStream(url.getFile());
        } catch (FileNotFoundException e) {
            System.out.println("Image was not found or could not be loaded.");
        }

        ImageView fileImageView = new ImageView(new Image(fileImage));

        return fileImageView;
    }
}
