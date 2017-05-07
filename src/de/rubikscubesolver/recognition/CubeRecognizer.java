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
	private Cube cube = new Cube();
	
	private CubeFace[] cubeFaces = new CubeFace[6];
	
	/* Recognition variables */
	int currentCubeFaceIndex = 0;
	
	int[] subCubeClasses = new int[9];
	
	double probabilityThreshold = 0.70;
	
	int[] probabilityOverThresholdCount = new int[9];
	
	int isCompleteThreshold = 30;
	
	boolean isCurrentCubeFaceComplete = false;
	
	
	/* Helpers used for recognition */
	private CubeDetector cubeDetector = new ShapeCubeDetector();
	
	private CubeFaceMLRecognizer cubeFaceRecognizer = new CubeFaceMLRecognizer();
	
	
	public CubeRecognizer() {
		
	}
	
	public void reset() {
		currentCubeFaceIndex = 0;
		subCubeClasses = new int[9];
		probabilityOverThresholdCount = new int[9];
		cube = new Cube();
		cubeFaces = new CubeFace[6];
		isCurrentCubeFaceComplete = true;
	}
	
	public boolean isCurrentCubeFaceComplete() {
		return isCurrentCubeFaceComplete;
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
		if(!isCurrentCubeFaceComplete && cubePosition != null) {
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
			boolean isCurrentCubeFaceComplete = true;
			for(int i = 0; i < probabilityOverThresholdCount.length; i++) {
				if(probabilityOverThresholdCount[i] < isCompleteThreshold) {
					isCurrentCubeFaceComplete = false;
				}
			}
			if(isCurrentCubeFaceComplete = true) {
				this.isCurrentCubeFaceComplete = true;
				cubeFaces[currentCubeFaceIndex] = new CubeFace();
				cubeFaces[currentCubeFaceIndex].setCubeFaceId(currentCubeFaceIndex);
				for(int i = 0; i < subCubeClasses.length; i++) {
					cubeFaces[currentCubeFaceIndex].setSubCube(i % 3, i / 3, subCubeClasses[i]);
				}
			}
			System.out.println(isCurrentCubeFaceComplete);
		}
	}

}
