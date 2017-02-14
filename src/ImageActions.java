public class ImageActions {
	Picture picture;
	int imageWidth = 0;
	int imageHeigth = 0;
	int [][] image;

	public ImageActions(){
		picture = new Picture();
	}
	
	public void loadImage(String inFile){
		try {
			picture.loadImage(inFile);
		} catch (Exception e) {
			e.printStackTrace();
		}
		imageWidth = picture.getImageWidth();
		imageHeigth = picture.getImageHeight();
		image = picture.getImage();
	}

	public void saveImage(String outFile){
		picture.setImage(image);
		try {
			picture.saveImage(outFile);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public int[][] getImage(){
		return image;
	}

	public void negateImage(){
		for(int i = 0; i < image.length; i++){
			for(int j = 0; j < image[0].length; j++){
				image[i][j] = Picture.maxImageIntensity - image[i][j];
			}
		}
	}

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


