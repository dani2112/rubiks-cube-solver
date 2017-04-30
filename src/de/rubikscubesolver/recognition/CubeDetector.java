package de.rubikscubesolver.recognition;

import org.opencv.core.Mat;

import de.rubikscubesolver.recognition.ShapeCubeDetector.CubePosition;

public interface CubeDetector {

	public CubePosition recognize(Mat frame);
	
}
