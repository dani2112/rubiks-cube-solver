package de.dk_s.rubikscubesolver.recognition;

import org.opencv.core.Mat;

import de.dk_s.rubikscubesolver.recognition.ShapeCubeDetector.CubePosition;

public interface CubeDetector {

	public CubePosition recognize(Mat frame);
	
}
