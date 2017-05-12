package de.dk_s.rubikscubesolver.recognition;

import java.util.Arrays;
import java.util.Observable;

import org.opencv.core.Mat;

import de.dk_s.rubikscubesolver.domain.Cube;
import de.dk_s.rubikscubesolver.domain.CubeFace;
import de.dk_s.rubikscubesolver.recognition.ShapeCubeDetector.CubePosition;
import de.dk_s.rubikscubesolver.recognition.ml.CubeFaceMLRecognizer;
import de.dk_s.rubikscubesolver.recognition.ml.SvmClassifier.ClassificationResult;

public class CubeRecognizer extends Observable {
	
	
	/* Constructed domain model */
	private Cube cube;
	
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
	
	
	public CubeRecognizer(Cube cube) {
		this.cube = cube;
	}
	
	public void reset() {
		currentCubeFaceIndex = 0;
		subCubeClasses = new int[9];
		probabilityOverThresholdCount = new int[9];
		isCurrentCubeFaceComplete = false;
	}
	
	public boolean isCurrentCubeFaceComplete() {
		return isCurrentCubeFaceComplete;
	}
	
	public int getCurrentCubeFaceIndex() {
		return currentCubeFaceIndex;
	}
	
	public void setCurrentCubeFaceIndex(int index) {
		/* Reset recognition variables */
		reset();
		currentCubeFaceIndex = index;
	}
	
	public void recognize(Mat frame) {
		CubePosition cubePosition = cubeDetector.recognize(frame);
		if(!isCurrentCubeFaceComplete && cubePosition != null) {
			ClassificationResult[] classificationResults = cubeFaceRecognizer.recognize(frame, cubePosition);
			for(int i = 0; i < classificationResults.length; i++) {
				if(classificationResults[i] == null) {
					continue;
				}
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
			if(isCurrentCubeFaceComplete == true) {
				this.isCurrentCubeFaceComplete = true;
				CubeFace cubeFaceNew = new CubeFace();
				cubeFaceNew.setCubeFaceId(currentCubeFaceIndex);
				for(int i = 0; i < subCubeClasses.length; i++) {
					cubeFaceNew.setSubCubeColor(i % 3, i / 3, subCubeClasses[i]);
				}
				cube.setCubeFace(currentCubeFaceIndex, cubeFaceNew);
			}
			setChanged();
			notifyObservers("faceComplete");
		}
	}

}
