// UserInterface.java
// User interface for image manipulation
// Original from Fritz Sieker
// Modified by Chris Wilcox
// 11/20/2012

import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import java.io.*;
import javax.swing.*;

public class UserInterface extends JFrame implements ActionListener
{
    private static final long serialVersionUID = 1L; // get rid of warning

    private boolean mIsGUIInitialized;
    private boolean mIsImageModified;

    // Menu items
    private JMenuItem mLoadImage;
    private JMenuItem mSaveImage;
    private JMenuItem mExitCommand;
    private JMenuItem mFlipHorizontal;
    private JMenuItem mFlipVertical;
    private JMenuItem mNegateImage;
    private JMenuItem mIncreaseBrightness;
    private JMenuItem mDecreaseBrightness;
    private JLabel    mLabel;

    private ImageActions mImageActions;

    // Array and static code are used to convert a gray scale to RGB
    private static int[] imageToColors;
    static {
        imageToColors = new int[256];

        for (int i = 0; i < 256; i++) {
            imageToColors[i] = (255 << 24) | (i << 16) | (i << 8) | i;
        }
    }

    // Constructor. Note that very little initialization is done here.
    // Since a derived class may override some of the initialization methods
    // these methods should NOT be called from a constructor because routines
    // in the derived class could be executed before the constructor of the super
    // class completes. In general, all code in the super class constructor
    // should be executed before ANY code in the derived class is executed.
    public UserInterface () {
        super();
        setSize(new Dimension(400, 300));
    }

    // Satisfy the ActionListener interface. Most of the work is delegated to
    // the method doAction(). This allows a derived class to override doAction
    // (instead of actionPerformed()) and take advantage of the error handling
    // done here. If a derived class overrides actionPerformed() in may need
    // to duplicate the error handling.
    public void actionPerformed (ActionEvent actionEvent) {
        try {
            doAction(actionEvent);
        }
        catch (Throwable t) {
            t.printStackTrace();
        }
    }

    // This can throw exceptions, because they are caught by performAction()
    // If you derive your own class from this class, and add new menus
    // or menu items, you would override this method to handle your new
    // menu items and delegate the work back to this method if the "action"
    // is not one of those you defined in your derived class. There are many
    // ways to dispatch from an event to the underlying code. This illustrates
    // one simple way of doing that.
    protected void doAction (ActionEvent actionEvent) throws Exception {
        Object clickedMenuItem = actionEvent.getSource();
        new SelectImageAction(clickedMenuItem).invoke();  //Extract method object
    }

    // Override setVisible() to initialize everything the first time the
    // component becomes visible
    public void setVisible (boolean isGUIVisible) {
        if (isGUIVisible) {
            if (!mIsGUIInitialized) {
                startGUI();
                mIsGUIInitialized = true;
            }
        }
        super.setVisible(isGUIVisible);
    }

    // Build the GUI.
    protected void startGUI() {
        setJMenuBar(makeMenuBar());

        addWindowListener(new WindowAdapter() {
            public void windowClosing (WindowEvent we) {
                mExitCommand.doClick(); // Press the Exit menu item
            }
        });

        getContentPane().add(makeMainPanel());
    }

    // Exit the GUI



    // Creates the main panel of the GUI
    protected JPanel makeMainPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        mLabel = new JLabel();
        panel.add(mLabel, BorderLayout.CENTER);

        return panel;
    }

    // Created the menu bar for the GUI. Delegates most of the work to
    // methods which create the individual menus. The "adds" should remind you
    // of your work with ArrayLists. A JMenuBar is conceptually just a list of
    // menus. You will find methods on a JMenuBar to manipulate the elements
    // of the list.
    protected JMenuBar makeMenuBar() {
        JMenuBar mb = new JMenuBar();
        mb.add(makeFileMenu());
        mb.add(makeActionMenu());
        return mb;
    }

    // Create the file menu. Again, the "adds" (see makeMeniItem)should remind you
    // of list manipulation. A JMenu is conceptually a list of JMenuItems.
    // Interestingly, a JMenu is a JMenuItem. Why do you think that is??
    protected JMenu makeFileMenu() {
        JMenu menu  = makeMenu("File", 'F');
        mLoadImage = makeMenuItem(menu, "Open...", 'O');
        mSaveImage = makeMenuItem(menu, "Save...", 'S');
        mExitCommand  = makeMenuItem(menu, "Exit", 'x');
        return menu;
    }

    // Create the action menu.
    protected JMenu makeActionMenu() {
        JMenu menu  = makeMenu("Action", 'A');
        mFlipHorizontal     = makeMenuItem(menu, "Flip Horizontal"     , 'H');
        mFlipVertical       = makeMenuItem(menu, "Flip Vertical"       , 'V');
        mNegateImage        = makeMenuItem(menu, "Negate Image"        , 'N');
        mIncreaseBrightness = makeMenuItem(menu, "Increase Brightness" , 'I');
        mDecreaseBrightness = makeMenuItem(menu, "Decrease Brightness" , 'D');
        return menu;
    }

    // Convenience method for making JMenu
    protected JMenu makeMenu (String name, char mnemonic) {
        JMenu menu = new JMenu(name);
        menu.setMnemonic(mnemonic);
        return menu;
    }

    // Convenience method for making JMenuItem
    protected JMenuItem makeMenuItem (String name, char mnemonic) {
        JMenuItem mi = new JMenuItem(name, (int) mnemonic);
        mi.addActionListener(this);
        return mi;
    }

    // Convenience method for putting JMenuItem in a menu
    protected JMenuItem makeMenuItem (JMenu menu, String name, char mnemonic) {
        JMenuItem mi = makeMenuItem(name, mnemonic);
        menu.add(mi);
        return mi;
    }

    // Convenience method to get yes/no from user
    protected boolean getConfirmNoImageSave(String title, String message) {
        int answer = JOptionPane.showInternalConfirmDialog(getContentPane(),
                message,
                title,
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE);

        return (answer == JOptionPane.YES_OPTION);
    }

    // Open image file
    private void openImage() throws Exception {

        // Data saved?
        if (mIsImageModified) {
            if (!getConfirmNoImageSave("Open file", "Data has not been saved. Continue?"))
                return;
        }

        String image = selectFile("Select file to open", true);

        if (image != null) {
            mImageActions = ImageActions.createImageActions();
            mImageActions.getSuperClassActions().loadImage(image);
            resetImage();
            mIsImageModified = false;
        }
    }

    // Save image file
    private void saveImage() throws Exception {
        String image = selectFile("Select file name to save", false);

        if (image != null) {
            mImageActions.getSuperClassActions().saveImage(image);
            mIsImageModified = false;
        }
    }

    // Other student methods
    private void flipHorizontal() {
        if (mImageActions != null) {
            mImageActions.getSuperClassActions().horizontalFlip();
            resetImage();
        }
    }

    private void flipVertical() {
        if (mImageActions != null) {
            mImageActions.getSuperClassActions().verticalFlip();
            resetImage();
        }
    }

    private void negateImage() {
        if (mImageActions != null) {
            mImageActions.getSuperClassActions().negateImage();
            resetImage();
        }
    }

    private void increaseBrightness() {
        if (mImageActions != null) {
            mImageActions.getSuperClassActions().increaseBrightness();
            resetImage();
        }
    }

    private void decreaseBrightness() {
        if (mImageActions != null) {
            mImageActions.getSuperClassActions().decreaseBrightness();
            resetImage();
        }
    }

    // File selector
    private String selectFile (String title, boolean open) {
        String imagePath = null;

        JFileChooser jFileChooser = new JFileChooser();
        jFileChooser.setCurrentDirectory(new File("."));
        jFileChooser.setDialogTitle(title);

        int result;
        if (open)
            result = jFileChooser.showOpenDialog(this);
        else
            result = jFileChooser.showSaveDialog(this);

        if (result == JFileChooser.APPROVE_OPTION) {
            File file = jFileChooser.getSelectedFile();
            imagePath = file.getAbsolutePath();
        }

        return imagePath;
    }

    // Reset image
    private void resetImage() {
        if (mImageActions != null) {

            // Copy the pixel values
            int image[][] = mImageActions.getSuperClassActions().getImage();
            int imageHeigth = image.length;
            int imageWidth = image[0].length;
            BufferedImage buffer = new BufferedImage(imageWidth, imageHeigth, BufferedImage.TYPE_INT_ARGB);


            for (int heigth = 0; heigth < imageHeigth; heigth++) {
                for (int width=0; width < imageWidth; width++) {
                    int rgb = imageToColors[image[heigth][width]];
                    buffer.setRGB(width, heigth, rgb);
                }
            }

            ImageIcon imageIcon = new ImageIcon(buffer);
            mLabel.setIcon(imageIcon);
            mIsImageModified = true;
            pack(); // make window just fit image
        }
    }

    // Main program
    public static void main (String[] args) throws IOException {
        //Introduce builder
        UserInterface gui = new UserInterfaceBuilder().createUserInterface();
        gui.setVisible(true);
    }

    //Extract method object
    private class SelectImageAction {
        private Object clickedMenuItem;

        public SelectImageAction(Object clickedMenuItem) {
            this.clickedMenuItem = clickedMenuItem;
        }
		
		private void exitGUI() {
        if (mIsImageModified) {
            if (!getConfirmNoImageSave("Data has not been saved.", "Continue?"))
                return;
        }
        System.exit(0);
    }
	
        public void invoke() throws Exception {
            if      (clickedMenuItem == mLoadImage)          openImage();
            else if (clickedMenuItem == mSaveImage)          saveImage();
            else if (clickedMenuItem == mExitCommand)        exitGUI();
            else if (clickedMenuItem == mFlipHorizontal)     flipHorizontal();
            else if (clickedMenuItem == mFlipVertical)       flipVertical();
            else if (clickedMenuItem == mNegateImage)        negateImage();
            else if (clickedMenuItem == mIncreaseBrightness) increaseBrightness();
            else if (clickedMenuItem == mDecreaseBrightness) decreaseBrightness();
        }
    }
}