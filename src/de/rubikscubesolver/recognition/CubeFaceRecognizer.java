package de.rubikscubesolver.recognition;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;

import de.dk_s.rubikscubesolver.domain.CubeFace;
import de.rubikscubesolver.recognition.ShapeCubeDetector.CubePosition;

public class CubeFaceRecognizer {

	public enum CubeColor {
		Blue(101), Red(177), Green(47), Orange(5), Yellow(20), White(33);

		public int hueValue;

		private CubeColor(int hueValue) {
			this.hueValue = hueValue;
		}
	}

	public CubeFace recognize(Mat frame, CubePosition cubePosition) {
		Mat hsvFrame = new Mat();
		Imgproc.cvtColor(frame, hsvFrame, Imgproc.COLOR_BGR2HSV);

		List<Mat> channelSplit = new ArrayList<>();
		Core.split(hsvFrame, channelSplit);

		Mat hueChannel = channelSplit.get(0);

		System.out.println("New Frame");
		List<Integer> cubePositionsY = cubePosition.cubePositionsY;
		List<Integer> cubePositionsX = cubePosition.cubePositionsX;
		for (int row = 0; row < cubePositionsY.size() - 1; row++) {
			for (int col = 0; col < cubePositionsX.size() - 1; col++) {
				Mat subCube = hueChannel.submat(cubePositionsY.get(row), cubePositionsY.get(row + 1),
						cubePositionsX.get(col), cubePositionsX.get(col + 1));
				byte[] hueValues = new byte[(int) subCube.total()];

				subCube.get(0, 0, hueValues);
				
				double[] averageDifferences = new double[CubeColor.values().length];
				
				for(int i = 0; i < CubeColor.values().length; i++) {
					averageDifferences[i] = calculateAverageHueDistance(hueValues, CubeColor.values()[i].hueValue);
				}
				
				System.out.println(CubeColor.values()[getIndexOfMin(averageDifferences)].name());
				
				
			}
		}

		// Mat blueMask = new Mat();
		// Core.inRange(hsvFrame, new Scalar(colorBlue - tolerance, 30, 30), new
		// Scalar(colorBlue + tolerance, 255, 255), blueMask);
		// Mat redMask = new Mat();
		// Core.inRange(hsvFrame, new Scalar(colorRed - tolerance, 30, 30), new
		// Scalar(colorRed + tolerance, 255, 255), redMask);
		// redMask.assignTo(frame);
		return null;
	}

	private int getIndexOfMin(double[] array) {
		int index = 0;
		double minValue = Double.MAX_VALUE;
		for (int i = 0; i < array.length; i++) {
			if (array[i] < minValue) {
				minValue = array[i];
				index = i;
			}
		}
		return index;
	}

	private double calculateAverageHueDistance(byte[] hueValues, int hueValue) {
		double averageDistance = 0.0;
		for (int i = 0; i < hueValues.length; i++) {
			averageDistance += Math.min(Math.abs(hueValues[i] - hueValue), 180 - Math.abs(hueValues[i] - hueValue));
		}
		averageDistance /= hueValues.length;
		return averageDistance;
	}

}
