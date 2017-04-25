package de.rubikscubesolver.recognition;

import org.opencv.core.Mat;

public class SlidingWindowExecutor {
	
	public static interface SlidingWindowAlgorithm {
		
		public void processRoi(int row, int col);
		
	}

	public static void process(Mat image, int rectangleRows, int rectangleCols, int rowStep, int colStep, SlidingWindowAlgorithm alg) {
		int imageRows = image.rows();
		int imageCols = image.cols();

		int rowLimit = imageRows - rectangleRows; 
		int colLimit = imageCols - rectangleCols;
		
		for (int row = 0; row <= rowLimit; row += rowStep) {
			for (int col = 0; col <= colLimit; col += colStep) {
				alg.processRoi(row, col);
			}
		}
	}

	
	
	
}
