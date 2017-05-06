package de.rubikscubesolver.recognition;

import org.opencv.core.Mat;

import de.rubikscubesolver.recognition.ShapeCubeDetector.CubePosition;
import de.rubikscubesolver.recognition.ml.CubeFaceMLRecognizer;

public class CubeRecognizer {
	
	private CubeDetector cubeDetector = new ShapeCubeDetector();
	
	private CubeFaceMLRecognizer cubeFaceRecognizer = new CubeFaceMLRecognizer();
	
	public void recognize(Mat frame) {
		CubePosition cubePosition = cubeDetector.recognize(frame);
		if(cubePosition != null) {
			cubeFaceRecognizer.recognize(frame, cubePosition);
		}
	}

}
