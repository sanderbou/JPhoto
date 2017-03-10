package JPhoto;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Scanner;

public class Picture {
    public static final int maxImageIntensity = 255;
    private int[][] image;

    public int getImageHeight() {
        return image.length;
    }

    public int getImageWidth() {
        return image[0].length;
    }

    public int[][] getImage() {
        return image;
    }

    public void setImage(int[][] image) {
        this.image = image;
    }

    public void loadImage(String imagePath) throws Exception {
        //inline (stuk hieronder was eerst doorgeven naar aparte methode)
        int imageWidth;
        int imageHeigth;
        int currentImageIntensity;
        try {
            Scanner imageScanner = new Scanner(new File(imagePath));
            String imageLine = imageScanner.next();
            if (!imageLine.equals("P2")) {
                imageScanner.close();
                throw new Exception("ERROR: cannot read .pgm file " + imagePath);
            }
            imageWidth  = imageScanner.nextInt();
            imageHeigth = imageScanner.nextInt();
            currentImageIntensity = imageScanner.nextInt();
            image = new int[imageHeigth][imageWidth];
            for (int heigth = 0; heigth < imageHeigth; heigth++)
                for (int width = 0; width < imageWidth; width++)
                    image[heigth][width] = imageScanner.nextInt();
            imageScanner.close();
        } catch (IOException e) {
            throw new Exception("ERROR: cannot read .pgm file " + imagePath);
        }
        updateImageIntensity(currentImageIntensity, imageHeigth, imageWidth);
    }

    public void updateImageIntensity(int currentImageIntensity, int imageHeigth, int imageWidth){
        // Scale values to the range 0-maxImageIntensity
        if (currentImageIntensity != maxImageIntensity)
            for (int heigth = 0; heigth < imageHeigth; heigth++)
                for (int width = 0; width < imageWidth; width++)
                    image[heigth][width] = (image[heigth][width] * maxImageIntensity) / currentImageIntensity;
    }

    public void saveImage(String imagePath) throws Exception {
        int imageHeight = getImageHeight();
        int imageWidth  = getImageWidth();
        ImageSettings.writeImageToDisk(new ImageSettings(imagePath, imageHeight, imageWidth), this);
    }

    //Extract parameter Object
    private static class ImageSettings {
        private final String filePath;
        private final int imageHeight;
        private final int imageWidth;

        private ImageSettings(String filePath, int imageHeight, int imageWidth) {
            this.filePath = filePath;
            this.imageHeight = imageHeight;
            this.imageWidth = imageWidth;
        }

        public String getFilePath() {
            return filePath;
        }

        public int getImageHeight() {
            return imageHeight;
        }

        public int getImageWidth() {
            return imageWidth;
        }

        //Change method signature && Move refactoring && make static/instance
        public static void writeImageToDisk(ImageSettings imageSettings, Picture picture) throws Exception{
            try {
                PrintStream output = new PrintStream(new FileOutputStream(imageSettings.getFilePath()));
                output.println("P2");
                output.println(imageSettings.getImageWidth() + " " + imageSettings.getImageHeight());
                output.println(maxImageIntensity);
                for (int heigth = 0; heigth < imageSettings.getImageHeight(); heigth++)
                    for (int width = 0; width < imageSettings.getImageWidth(); width++)
                        output.println(picture.image[heigth][width]); // One pixel per line!
                output.close();
            } catch (IOException e) {
                throw new Exception("ERROR: cannot write .pgm file " + imageSettings.getFilePath());
            }
        }
    }
}