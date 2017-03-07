public class ImageActions {
	private final MySuperClassActions superClassActions = new MySuperClassActions();
	Picture picture;
	int imageWidth = 0;
	int imageHeigth = 0;
	int [][] image;

	//Introduce factory method
	private ImageActions(){
		picture = new Picture();
	}

	public static ImageActions createImageActions() {
		return new ImageActions();
	}

	//remove middleman, hierdoor moet klasse MySuperClassActions ook public zijn, want anders kan UserInterface er niet bij
	public MySuperClassActions getSuperClassActions() {
		return superClassActions;
	}

	//Replace inheritance with delegation
	public class MySuperClassActions extends SuperClassActions {
		//Alle methodes hieronder ->Extract interface/abstract pull up/down van SuperClassActions
        @Override
        public void loadImage(String inFile) throws Exception {
            picture.loadImage(inFile);
            superClassActions.setImageSettings();
        }

		//extract method
        @Override
        protected void setImageSettings() {
            imageWidth = picture.getImageWidth();
            imageHeigth = picture.getImageHeight();
            image = picture.getImage();
        }

		@Override
        public void saveImage(String outFile){
            picture.setImage(image);
            try {
                picture.saveImage(outFile);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

		@Override
        public int[][] getImage(){
            return image;
        }

		@Override
        public void negateImage(){
            for(int i = 0; i < image.length; i++){
                for(int j = 0; j < image[0].length; j++){
                    image[i][j] = Picture.maxImageIntensity - image[i][j];
                }
            }
        }

		@Override
        public void increaseBrightness(){
            for(int i = 0; i < image.length; i++){
                for(int j = 0; j < image[0].length; j++){
                    for(int k = 0; k < 32; k++){				//loop through increment
                        if(image[i][j] != Picture.maxImageIntensity){		//checks if already at max
                            image[i][j] = image[i][j] + 1;		//adds 1 to pixel value
                        }
                    }
                }
            }
        }

		@Override
        public void decreaseBrightness(){
            for(int i = 0; i < image.length; i++){
                for(int j = 0; j < image[0].length; j++){
                    for(int k = 0; k < 32; k++){
                        if(image[i][j] != 0){
                            image[i][j] = image[i][j] - 1;
                        }
                    }
                }
            }
        }

		@Override
        public void horizontalFlip(){
            int[][] temp = new int[image.length][image[0].length];
            for(int i = 0; i < image.length; i++){
                for(int j = 0; j < image[0].length; j++){
                    temp[i][image[0].length - 1 - j] = image[i][j];
                }
            }

            for(int i = 0; i < image.length; i++){
                for(int j = 0; j < image[0].length; j++){
                    image[i][j] = temp[i][j];
                }
            }

        }

		@Override
        public void verticalFlip(){
            int[][] temp = new int[image.length][image[0].length];
            for(int i = 0; i < image.length; i++){
                for(int j = 0; j < image[0].length; j++){
                    temp[image.length - 1 - i][j] = image[i][j];
                }
            }

            for(int i = 0; i < image.length; i++){
                for(int j = 0; j < image[0].length; j++){
                    image[i][j] = temp[i][j];
                }
            }

        }
	}
}


