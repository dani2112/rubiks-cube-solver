package de.rubikscubesolver.recognition;

import java.util.Arrays;

import org.opencv.core.Mat;

import de.dk_s.rubikscubesolver.domain.Cube;
import de.dk_s.rubikscubesolver.domain.CubeFace;
import de.rubikscubesolver.recognition.ShapeCubeDetector.CubePosition;
import de.rubikscubesolver.recognition.ml.CubeFaceMLRecognizer;
import de.rubikscubesolver.recognition.ml.SvmClassifier.ClassificationResult;

public class CubeRecognizer {
	
	
	/* Constructed domain model */
	private Cube cube = null;
	
	private CubeFace[] cubeFaces = null;
	
	/* Recognition variables */
	int currentCubeFaceIndex = 0;
	
	int[] subCubeClasses = new int[9];
	
	double probabilityThreshold = 0.70;
	
	int[] probabilityOverThresholdCount = new int[9];
	
	int isCompleteThreshold = 30;
	
	boolean isComplete = false;
	
	
	/* Helpers used for recognition */
	private CubeDetector cubeDetector = new ShapeCubeDetector();
	
	private CubeFaceMLRecognizer cubeFaceRecognizer = new CubeFaceMLRecognizer();
	
	
	public CubeRecognizer() {
		
	}
	
	public void reset() {
		currentCubeFaceIndex = 0;
		subCubeClasses = new int[9];
		probabilityOverThresholdCount = new int[9];
		cube = null;
		cubeFaces = null;
		isComplete = true;
	}
	
	public boolean isComplete() {
		return isComplete;
	}
	
	public int getCurrentCubeFaceIndex() {
		return currentCubeFaceIndex;
	}
	
	public void setCurrentCubeFaceIndex(int index) {
		/* Reset recognition variables */
		subCubeClasses = new int[9];
		probabilityOverThresholdCount = new int[9];
		currentCubeFaceIndex = index;
	}
	
	public void recognize(Mat frame) {
		CubePosition cubePosition = cubeDetector.recognize(frame);
		if(!isComplete && cubePosition != null) {
			ClassificationResult[] classificationResults = cubeFaceRecognizer.recognize(frame, cubePosition);
			for(int i = 0; i < classificationResults.length; i++) {
				if(classificationResults[i].probability > probabilityThreshold) {
					if(subCubeClasses[i] == classificationResults[i].classLabel) {
						probabilityOverThresholdCount[i]++;
					} else {
						subCubeClasses[i] = classificationResults[i].classLabel;
						probabilityOverThresholdCount[i] = 0;
					}
					probabilityOverThresholdCount[i]++;
				}
			}
			/* Completeness check */
			boolean isComplete = true;
			for(int i = 0; i < probabilityOverThresholdCount.length; i++) {
				if(probabilityOverThresholdCount[i] < isCompleteThreshold) {
					isComplete = false;
				}
			}
			this.isComplete = isComplete;
			System.out.println(isComplete);
		}
	}

}
