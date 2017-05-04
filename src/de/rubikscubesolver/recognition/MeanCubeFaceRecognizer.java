package de.rubikscubesolver.recognition;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.TreeMap;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;

import de.rubikscubesolver.recognition.ShapeCubeDetector.CubePosition;

public class MeanCubeFaceRecognizer {

	public void recognize(Mat frame, CubePosition cubePosition) {
		System.out.println("New Frame");

		Mat hsvFrame = new Mat();
		Imgproc.cvtColor(frame, hsvFrame, Imgproc.COLOR_BGR2HSV_FULL);

//		List<Mat> channelSplit = new ArrayList<Mat>();
//		Core.split(frame, channelSplit);
//
//		Mat hueChannel = channelSplit.get(1);
		
		

		List<Integer> cubePositionsY = cubePosition.cubePositionsY;
		List<Integer> cubePositionsX = cubePosition.cubePositionsX;
		for (int row = 0; row < cubePositionsY.size() - 1; row++) {
			for (int col = 0; col < cubePositionsX.size() - 1; col++) {
				System.out.println("SubCube");
				Mat subCube = frame.submat(cubePositionsY.get(row), cubePositionsY.get(row + 1),
						cubePositionsX.get(col), cubePositionsX.get(col + 1));
				byte[] subCubeValues = new byte[(int) subCube.total() * 3];

				subCube.get(0, 0, subCubeValues);

				
				
			}
		}
	}

}
