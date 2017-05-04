package de.rubikscubesolver.recognition;

import java.util.ArrayList;
import java.util.List;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.CLAHE;
import org.opencv.imgproc.Imgproc;

import de.dk_s.rubikscubesolver.domain.CubeFace;
import de.rubikscubesolver.recognition.ShapeCubeDetector.CubePosition;

//Saturation: 255, 255, 255, 180, 160, 50
//Value: 150, 160, 160, 240, 240, 215

public class ThresholdCubeFaceRecognizer {

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

	private int hueTolerance = 20;
	private int saturationTolerance = 20;
	private int valueTolerance = 20;

	public CubeFace recognize(Mat frame, CubePosition cubePosition) {
		System.out.println("New Frame");
		
		Mat hsvFrame = new Mat();
		Imgproc.cvtColor(frame, hsvFrame, Imgproc.COLOR_BGR2HSV);
		
		
      List<Mat> channelSplit = new ArrayList<Mat>();
      Core.split(frame, channelSplit);

//      CLAHE clahe = Imgproc.createCLAHE();
//      clahe.setClipLimit(4);
//      Mat valueDest = new Mat();
//      clahe.apply(channelSplit.get(2), valueDest);
//      valueDest.copyTo(channelSplit.get(2));
//      Core.merge(channelSplit, frame);
      
      
//		Core.inRange(hsvFrame,
//				new Scalar(CubeColor.Red.hueValue - hueTolerance, CubeColor.Red.saturationValue - saturationTolerance,
//						CubeColor.Red.valueValue - valueTolerance),
//				new Scalar(CubeColor.Red.hueValue + hueTolerance, CubeColor.Red.saturationValue + saturationTolerance,
//						CubeColor.Red.valueValue + valueTolerance),
//				redFrame);
		
		Mat blueFrame = new Mat();
		Core.inRange(hsvFrame, new Scalar(100, 100, 100),new Scalar(130, 255, 255), blueFrame);
		
		Mat greenFrame = new Mat();
		Core.inRange(hsvFrame, new Scalar(50, 100, 100),new Scalar(75, 255, 255), greenFrame);
		
		
		Mat redFrame1 = new Mat();
		Core.inRange(hsvFrame, new Scalar(0, 70, 50),new Scalar(10, 255, 255), redFrame1);
		Mat redFrame2 = new Mat();
	    Core.inRange(hsvFrame, new Scalar(170, 70, 50), new Scalar(180, 255, 255), redFrame2);
		Mat redFrame = new Mat();
		Core.bitwise_or(redFrame1, redFrame2, redFrame);
		
		Mat redHsvFrame = new Mat();
		hsvFrame.copyTo(redHsvFrame, redFrame);
		
		Mat darkRedFrame = new Mat();
		Core.inRange(redHsvFrame, new Scalar(0, 190, 0), new Scalar(255, 255, 255), darkRedFrame);
		
		Mat orangeFrame = new Mat();
		Core.inRange(redHsvFrame, new Scalar(0, 50, 220), new Scalar(255, 180, 255), orangeFrame);
		
		Mat yellowFrame = new Mat();
		Core.inRange(hsvFrame, new Scalar(15, 100, 100),new Scalar(30, 255, 255), yellowFrame);
		
		Mat whiteFrame = new Mat();
		Core.inRange(hsvFrame, new Scalar(0, 0, 0),new Scalar(255, 80, 255), whiteFrame);
		
		boolean safelyDetected = true;
		
		List<Integer> cubePositionsY = cubePosition.cubePositionsY;
		List<Integer> cubePositionsX = cubePosition.cubePositionsX;
		for (int row = 0; row < cubePositionsY.size() - 1; row++) {
			for (int col = 0; col < cubePositionsX.size() - 1; col++) {
				List<Mat> binaryImages = new ArrayList<Mat>();
				Mat subCubeBlue = blueFrame.submat(cubePositionsY.get(row), cubePositionsY.get(row + 1),
						cubePositionsX.get(col), cubePositionsX.get(col + 1));
				binaryImages.add(subCubeBlue);
				Mat subCubeRed = darkRedFrame.submat(cubePositionsY.get(row), cubePositionsY.get(row + 1),
						cubePositionsX.get(col), cubePositionsX.get(col + 1));
				binaryImages.add(subCubeRed);
				Mat subCubeGreen = greenFrame.submat(cubePositionsY.get(row), cubePositionsY.get(row + 1),
						cubePositionsX.get(col), cubePositionsX.get(col + 1));
				binaryImages.add(subCubeGreen);
				Mat subCubeOrange = orangeFrame.submat(cubePositionsY.get(row), cubePositionsY.get(row + 1),
						cubePositionsX.get(col), cubePositionsX.get(col + 1));
				binaryImages.add(subCubeOrange);
				Mat subCubeYellow = yellowFrame.submat(cubePositionsY.get(row), cubePositionsY.get(row + 1),
						cubePositionsX.get(col), cubePositionsX.get(col + 1));
				binaryImages.add(subCubeYellow);
				Mat subCubeWhite = whiteFrame.submat(cubePositionsY.get(row), cubePositionsY.get(row + 1),
						cubePositionsX.get(col), cubePositionsX.get(col + 1));
				binaryImages.add(subCubeWhite);
				int maxNonZero = Integer.MIN_VALUE;
				int maxNonZeroIndex = 0;
				int index = 0;
				for(Mat binaryImage : binaryImages) {
					int nonZero = Core.countNonZero(binaryImage);
					if(nonZero > maxNonZero) {
						maxNonZero = nonZero;
						maxNonZeroIndex = index;
					}
					index++;
				}
				if(maxNonZero < 0.4 * subCubeWhite.total()) {
					safelyDetected = false;
					break;
				}
				System.out.println(CubeColor.values()[maxNonZeroIndex].name());
			}
		}
		
		if(safelyDetected) {
			System.out.println("Safe");
		}
		
		return null;

	}



}
