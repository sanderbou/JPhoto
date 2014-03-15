public class P7 {
	//class instance variables
	Picture picture;
	int width = 0;
	int height = 0;
	int [][] imgData;
	
	
	//Constructor method
	public P7(){
		picture = new Picture();
	}
	
	public void readImage(String inFile){
		try {
			picture.readPGM(inFile);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		width = picture.getWidth();
		height = picture.getHeight();
		imgData = picture.getData();
	}

	public void writeImage(String outFile){
		picture.setData(imgData);
		try {
			picture.writePGM(outFile);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	public int[][] imageData(){
		return imgData;
	}

	public void negateImage(){
		for(int i = 0; i < imgData.length; i++){
			for(int j = 0; j < imgData[0].length;j++){
				imgData[i][j] = Picture.MAXVAL - imgData[i][j];
			}
		}
	}

	public void increaseBrightness(){
		for(int i = 0; i < imgData.length; i++){
			for(int j = 0; j < imgData[0].length;j++){
				for(int k = 0; k < 32; k++){				//loop through increment 
					if(imgData[i][j] != Picture.MAXVAL){		//checks if already at max
						imgData[i][j] = imgData[i][j] + 1;		//adds 1 to pixel value
					}
				}
			}
		}
	}

	public void decreaseBrightness(){
		for(int i = 0; i < imgData.length; i++){
			for(int j = 0; j < imgData[0].length;j++){
				for(int k = 0; k < 32; k++){
					if(imgData[i][j] != 0){
						imgData[i][j] = imgData[i][j] - 1;
					}
				}
			}
		}
	}
	
	public void horizontalFlip(){
		int[][] temp = new int[imgData.length][imgData[0].length];
		for(int i = 0; i < imgData.length; i++){
			for(int j = 0; j < imgData[0].length; j++){
				temp[i][imgData[0].length - 1 - j] = imgData[i][j];
			}
		}
		
		for(int i = 0; i < imgData.length; i++){
			for(int j = 0; j < imgData[0].length; j++){
				imgData[i][j] = temp[i][j];
			}
		}
		
	}

	public void verticalFlip(){
		int[][] temp = new int[imgData.length][imgData[0].length];
		for(int i = 0; i < imgData.length; i++){
			for(int j = 0; j < imgData[0].length; j++){
				temp[imgData.length - 1 - i][j] = imgData[i][j];
			}
		}
		
		for(int i = 0; i < imgData.length; i++){
			for(int j = 0; j < imgData[0].length; j++){
				imgData[i][j] = temp[i][j];
			}
		}
		
	}
}


