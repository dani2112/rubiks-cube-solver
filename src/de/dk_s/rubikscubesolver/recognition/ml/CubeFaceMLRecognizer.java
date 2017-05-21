package de.dk_s.rubikscubesolver.recognition.ml;

import java.util.ArrayList;
import java.util.List;

import org.opencv.core.Core;
import org.opencv.core.Mat;

import de.dk_s.rubikscubesolver.recognition.ShapeCubeDetector.CubePosition;
import de.dk_s.rubikscubesolver.recognition.ml.SvmClassifier.ClassificationResult;

public class CubeFaceMLRecognizer {

	private SvmClassifier subCubeClassifier = null;
	
	private String modelPath = "C:\\Users\\Daniel\\Code\\Java_Eclipse\\RubiksCubeSolver\\ml_models\\test5prob.txt.model";
	
	private String rangePath = "C:\\Users\\Daniel\\Code\\Java_Eclipse\\RubiksCubeSolver\\ml_models\\test5.txt.range";
	
	public CubeFaceMLRecognizer() {
		subCubeClassifier = new SvmClassifier(modelPath, rangePath, new SubCubeFeatureExtractor());
	}
	
	public ClassificationResult[] recognize(Mat frame, CubePosition cubePosition) {
		List<ClassificationResult> classificationResults = new ArrayList<>();
		List<Integer> cubePositionsY = cubePosition.cubePositionsY;
		List<Integer> cubePositionsX = cubePosition.cubePositionsX;
		for (int row = 0; row < cubePositionsY.size() - 1; row++) {
			for (int col = 0; col < cubePositionsX.size() - 1; col++) {
				Mat subCube = frame.submat(cubePositionsY.get(row) + 10, cubePositionsY.get(row + 1) - 10,
						cubePositionsX.get(col) + 10, cubePositionsX.get(col + 1) - 10);
				ClassificationResult classificationResult = subCubeClassifier.classify(subCube);
				classificationResults.add(classificationResult);
			}
		}
		ClassificationResult[] results = new ClassificationResult[classificationResults.size()];
		results = classificationResults.toArray(results);
		return results;
	}
	
}
