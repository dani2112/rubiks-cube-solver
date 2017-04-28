package de.rubikscubesolver.recognition;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

import de.rubikscubesolver.recognition.SlidingWindowExecutor.SlidingWindowAlgorithm;

public class CubeEdgeDetector {

	public static class DataPoint {

		public DataPoint(int rowIndex, double gradientValue) {
			this.colIndex = rowIndex;
			this.gradientValue = gradientValue;
		}

		public int colIndex;
		public double gradientValue;
	}

	public static void detect(Mat image) {
		Mat procImage = image.clone();
		Imgproc.cvtColor(procImage, procImage, Imgproc.COLOR_BGR2GRAY);

		/*
		 * Apply normalized Box Filter which at each position calculates the
		 * average value of the pixels in a block with the given size
		 */
		Imgproc.blur(procImage, procImage, new Size(5, 5));

		/* Calculate gradient in x direction with Sobel operator */
		Mat gradientX = new Mat(procImage.cols(), procImage.rows(), CvType.CV_16S);
		Imgproc.Sobel(procImage, gradientX, CvType.CV_32F, 1, 0, 3, 1, 0);

		/*
		 * Calculate abs of the gradient image absdiff takes absolute difference
		 * between first and second parameter as second parameter is all zero
		 * this will be equivalent to abs(gradientX)
		 */
		Core.absdiff(gradientX, Scalar.all(0.0), gradientX);

		/* Calculate integral image for faster area content calculation */
		Mat integralImage = new Mat();
		Imgproc.integral(gradientX, integralImage);

		double[] integralValues = new double[(int) integralImage.total()];
		integralImage.get(0, 0, integralValues);

		int rectangleRows = 20;
		int rectangleCols = 5;
		int rowStep = 5;
		int colStep = 5;

		SlidingWindowExecutor.process(integralImage, rectangleRows, rectangleCols, rowStep, colStep,
				new SlidingWindowAlgorithm() {


					@Override
					public void processRoi(int row, int col) {
						double areaValue = getAreaValue(row, col, rectangleRows, rectangleCols, integralImage.cols(),
								integralValues);
						 if(areaValue > 3000) {
						 Imgproc.drawMarker(image, new Point(col, row), new
						 Scalar(255,0,0), Imgproc.MARKER_CROSS, 5, 2,
						 Imgproc.LINE_4);
						 }
					}

				});
	}

	private static double getAreaValue(int upperLeftRow, int upperLeftCol, int rectangleRows, int rectangleCols,
			int cols, double[] integralValues) {
		int upperLeftArrayPos = (upperLeftRow * cols) + upperLeftCol;

		int lowerLeftX = upperLeftCol;
		int lowerLeftY = upperLeftRow + rectangleRows;
		int lowerLeftArrayPos = (lowerLeftY * cols) + lowerLeftX;

		int upperRightX = upperLeftCol + rectangleCols;
		int upperRightY = upperLeftRow;
		int upperRightArrayPos = (upperRightY * cols) + upperRightX;

		int lowerRightX = upperRightX;
		int lowerRightY = lowerLeftY;
		int lowerRightArrayPos = (lowerRightY * cols) + lowerRightX;

		double areaValue = integralValues[upperLeftArrayPos] + integralValues[lowerRightArrayPos]
				- integralValues[upperRightArrayPos] - integralValues[lowerLeftArrayPos];

		return areaValue;
	}

}
