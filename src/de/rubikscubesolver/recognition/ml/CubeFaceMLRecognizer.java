package de.rubikscubesolver.recognition.ml;

import java.util.List;

import org.opencv.core.Core;
import org.opencv.core.Mat;

import de.rubikscubesolver.recognition.ShapeCubeDetector.CubePosition;

public class CubeFaceMLRecognizer {

	private SvmClassifier subCubeClassifier = null;
	
	private String modelPath = "C:\\Users\\Daniel\\Code\\Java_Eclipse\\RubiksCubeSolver\\ml_models\\test.txt.model";
	
	private String rangePath = "C:\\Users\\Daniel\\Code\\Java_Eclipse\\RubiksCubeSolver\\ml_models\\test.txt.range";
	
	public CubeFaceMLRecognizer() {
		subCubeClassifier = new SvmClassifier(modelPath, rangePath, new SubCubeFeatureExtractor());
	}
	
	public void recognize(Mat frame, CubePosition cubePosition) {
		System.out.println("Recognition started");
		List<Integer> cubePositionsY = cubePosition.cubePositionsY;
		List<Integer> cubePositionsX = cubePosition.cubePositionsX;
		for (int row = 0; row < cubePositionsY.size() - 1; row++) {
			for (int col = 0; col < cubePositionsX.size() - 1; col++) {
				Mat subCube = frame.submat(cubePositionsY.get(row) + 10, cubePositionsY.get(row + 1) - 10,
						cubePositionsX.get(col) + 10, cubePositionsX.get(col + 1) - 10);
				int classLabel = subCubeClassifier.classify(subCube);
				System.out.println(classLabel);
			}
		}
	}
	
}
