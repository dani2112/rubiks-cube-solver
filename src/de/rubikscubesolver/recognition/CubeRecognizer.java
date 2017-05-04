package de.rubikscubesolver.recognition;

import org.opencv.core.Mat;

import de.rubikscubesolver.recognition.ShapeCubeDetector.CubePosition;

public class CubeRecognizer {
	
	private CubeDetector cubeDetector = new ShapeCubeDetector();
	
	private MeanCubeFaceRecognizer cubeFaceRecognizer = new MeanCubeFaceRecognizer();
	
	public void recognize(Mat frame) {
		CubePosition cubePosition = cubeDetector.recognize(frame);
		if(cubePosition != null) {
			cubeFaceRecognizer.recognize(frame, cubePosition);
		}
	}

}
