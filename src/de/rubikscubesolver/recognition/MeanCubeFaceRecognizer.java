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
		
		
		Scalar[] means = new Scalar[9];
		List<Integer> cubePositionsY = cubePosition.cubePositionsY;
		List<Integer> cubePositionsX = cubePosition.cubePositionsX;
		for (int row = 0; row < cubePositionsY.size() - 1; row++) {
			for (int col = 0; col < cubePositionsX.size() - 1; col++) {
				System.out.println("SubCube");
				Mat subCube = frame.submat(cubePositionsY.get(row) + 10, cubePositionsY.get(row + 1) - 10,
						cubePositionsX.get(col) + 10, cubePositionsX.get(col + 1) - 10);
				
				means[row * 3 + col] = Core.mean(subCube);
				
			}
		}
		
		for (int i = 0; i < means.length; i++) {
			for(int j = 0; j < means.length; j++) {
				if(i == j) {
					continue;
				}
				double distance = Math.sqrt((means[i].val[0] * means[i].val[0]) - (means[j].val[0] * means[j].val[0]) +
						(means[i].val[1] * means[i].val[1]) - (means[j].val[1] * means[j].val[1]) +
						(means[i].val[2] * means[i].val[2]) - (means[j].val[2] * means[j].val[2]));
				if(distance < 50) {
					System.out.println("------------");
					System.out.println(i);
					System.out.println(j);
				}
			}
		}
		
	}

}
