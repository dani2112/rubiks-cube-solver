package de.rubikscubesolver.recognition;

import org.opencv.core.Mat;

public interface CubeRecognizer {

	public void recognize(Mat frame);
	
}
