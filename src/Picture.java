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
        readImage(imagePath);
    }

    public void readImage(String path) throws Exception{

        int imageWidth;
        int imageHeigth;
        int currentImageIntensity;
        try {
            Scanner imageScanner = new Scanner(new File(path));
            String imageLine = imageScanner.next();
            if (!imageLine.equals("P2")) {
                imageScanner.close();
                throw new Exception("ERROR: cannot read .pgm file " + path);
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
            throw new Exception("ERROR: cannot read .pgm file " + path);
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
        writeImageToFile(imagePath, imageHeight, imageWidth);
    }

    public void writeImageToFile(String imagePath, int imageHeigth, int imageWidth) throws Exception{
        try {
            PrintStream output = new PrintStream(new FileOutputStream(imagePath));
            output.println("P2");
            output.println(imageWidth + " " + imageHeigth);
            output.println(maxImageIntensity);
            for (int heigth = 0; heigth < imageHeigth; heigth++)
                for (int width = 0; width < imageWidth; width++)
                    output.println(image[heigth][width]); // One pixel per line!
            output.close();
        } catch (IOException e) {
            throw new Exception("ERROR: cannot write .pgm file " + imagePath);
        }
    }
}