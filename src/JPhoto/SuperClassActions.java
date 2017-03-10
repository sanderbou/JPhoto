package JPhoto;

/**
 * Created by Sander on 2-3-2017.
 */
public abstract class SuperClassActions {

    public abstract void loadImage(String inFile);

    //extract method
    protected abstract void setImageSettings();

    public abstract void saveImage(String outFile);

    public abstract int[][] getImage();

    public abstract void negateImage();

    public abstract void increaseBrightness();

    public abstract void decreaseBrightness();

    public abstract void horizontalFlip();

    public abstract void verticalFlip();
}
