package de.rubikscubesolver.recognition;

import java.util.ArrayList;
import java.util.List;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;

import de.dk_s.rubikscubesolver.domain.CubeFace;
import de.rubikscubesolver.recognition.ShapeCubeDetector.CubePosition;

//Saturation: 255, 255, 255, 180, 160, 50
//Value: 150, 160, 160, 240, 240, 215

public class CubeFaceRecognizer {

	public enum CubeColor {
		Blue(101, 255, 150), Red(177, 255, 160), Green(47, 255, 160), Orange(5, 180, 240), Yellow(20, 160,
				240), White(33, 50, 215);

		public int hueValue;
		public int saturationValue;
		public int valueValue;

		private CubeColor(int hueValue, int saturationValue, int valueValue) {
			this.hueValue = hueValue;
			this.saturationValue = saturationValue;
			this.valueValue = valueValue;
		}
	}

	public CubeFace recognize(Mat frame, CubePosition cubePosition) {
		System.out.println("New Frame");
		Mat hsvFrame = new Mat();
		Imgproc.cvtColor(frame, hsvFrame, Imgproc.COLOR_BGR2HSV);

		List<Mat> channelSplit = new ArrayList<>();
		Core.split(hsvFrame, channelSplit);

		Mat hueChannel = channelSplit.get(0);
		Mat saturationChannel = channelSplit.get(1);

		List<Integer> cubePositionsY = cubePosition.cubePositionsY;
		List<Integer> cubePositionsX = cubePosition.cubePositionsX;
		for (int row = 0; row < cubePositionsY.size() - 1; row++) {
			for (int col = 0; col < cubePositionsX.size() - 1; col++) {
				Mat subCubeHue = hueChannel.submat(cubePositionsY.get(row), cubePositionsY.get(row + 1),
						cubePositionsX.get(col), cubePositionsX.get(col + 1));
				byte[] hueValues = new byte[(int) subCubeHue.total()];
				subCubeHue.get(0, 0, hueValues);

				Mat subCubeSaturation = saturationChannel.submat(cubePositionsY.get(row), cubePositionsY.get(row + 1),
						cubePositionsX.get(col), cubePositionsX.get(col + 1));
				byte[] saturationValues = new byte[(int) subCubeSaturation.total()];
				subCubeSaturation.get(0, 0, saturationValues);

				double[] averageDifferences = new double[CubeColor.values().length];

				for (int i = 0; i < CubeColor.values().length; i++) {
					averageDifferences[i] = calculateAverageHueDistance(hueValues, saturationValues,
							CubeColor.values()[i]);
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

	private double calculateAverageHueDistance(byte[] hueValues, byte[] saturationValues, CubeColor cubeColor) {
		double averageDistance = 0.0;
		for (int i = 0; i < hueValues.length; i++) {
			int hueValue = 0xFF & hueValues[i];
			averageDistance += 0.5 * (Math.min(Math.abs(hueValue -cubeColor.hueValue), 180 - Math.abs(hueValue - cubeColor.hueValue)));
			int saturationValue = 0xFF & saturationValues[i];
			averageDistance += 0.5 * Math.abs(saturationValue - cubeColor.saturationValue);
		}
		averageDistance /= saturationValues.length;
		return averageDistance;
	}

}
