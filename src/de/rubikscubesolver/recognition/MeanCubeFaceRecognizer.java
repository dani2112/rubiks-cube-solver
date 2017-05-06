package de.rubikscubesolver.recognition;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Scalar;
import org.opencv.core.TermCriteria;
import org.opencv.imgproc.Imgproc;

import de.rubikscubesolver.recognition.ShapeCubeDetector.CubePosition;

public class MeanCubeFaceRecognizer {

	public void recognize(Mat frame, CubePosition cubePosition) {
		System.out.println("New Frame");

//		Mat hsvFrame = new Mat();
//		Imgproc.cvtColor(frame, hsvFrame, Imgproc.COLOR_BGR2HSV_FULL);

//		List<Mat> channelSplit = new ArrayList<Mat>();
//		Core.split(frame, channelSplit);
//
//		Mat hueChannel = channelSplit.get(1);
		
		
		List<Integer> cubePositionsY = cubePosition.cubePositionsY;
		List<Integer> cubePositionsX = cubePosition.cubePositionsX;
		for (int row = 0; row < cubePositionsY.size() - 1; row++) {
			for (int col = 0; col < cubePositionsX.size() - 1; col++) {
				System.out.println("SubCube");
				Mat subCube = frame.submat(cubePositionsY.get(row) + 10, cubePositionsY.get(row + 1) - 10,
						cubePositionsX.get(col) + 10, cubePositionsX.get(col + 1) - 10);
				System.out.println(Core.mean(subCube));
				
			}
		}
		
	}

}
