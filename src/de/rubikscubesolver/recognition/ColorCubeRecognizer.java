package de.rubikscubesolver.recognition;

import java.util.ArrayList;
import java.util.List;

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.core.TermCriteria;
import org.opencv.imgproc.Imgproc;

import de.rubikscubesolver.recognition.SlidingWindowExecutor.SlidingWindowAlgorithm;

public class ColorCubeRecognizer implements CubeRecognizer {

	@Override
	public void recognize(Mat frame) {
		// processImageShape(frame);
		processImageShape2(frame);
		// processSlidingWindow(frame);
	}

	private void processSlidingWindow(Mat frame) {
		CubeEdgeDetector.detect(frame);
	}

	private void processImageShape2(Mat frame) {
		Mat procFrame = frame.clone();
		Imgproc.cvtColor(procFrame, procFrame, Imgproc.COLOR_BGR2GRAY);
		/*
		 * Apply normalized Box Filter which at each position calculates the
		 * average value of the pixels in a block with the given size
		 */
		Imgproc.blur(procFrame, procFrame, new Size(5, 5));

		Mat gradientX = new Mat(procFrame.cols(), procFrame.rows(), CvType.CV_16S);
		Imgproc.Sobel(procFrame, gradientX, CvType.CV_32F, 1, 0, 3, 1, 0);

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

		int rectangleRows = integralImage.rows() - 1;
		int rectangleCols = 5;
		int rowStep = 5;
		int colStep = 5;

		List<Integer> highGradientXCols = new ArrayList<>();

		SlidingWindowExecutor.process(integralImage, rectangleRows, rectangleCols, rowStep, colStep,
				new SlidingWindowAlgorithm() {

					@Override
					public void processRoi(int row, int col) {
						double areaValue = getAreaValue(row, col, rectangleRows, rectangleCols, integralImage.cols(),
								integralValues);

						if (areaValue > 60000) {
							highGradientXCols.add(col);
						}
					}

				});

		Mat gradientY = new Mat(procFrame.cols(), procFrame.rows(), CvType.CV_16S);
		Imgproc.Sobel(procFrame, gradientY, CvType.CV_32F, 0, 1, 3, 1, 0);

		/*
		 * Calculate abs of the gradient image absdiff takes absolute difference
		 * between first and second parameter as second parameter is all zero
		 * this will be equivalent to abs(gradientX)
		 */
		Core.absdiff(gradientY, Scalar.all(0.0), gradientY);

		/* Calculate integral image for faster area content calculation */
		Mat integralImage2 = new Mat();
		Imgproc.integral(gradientY, integralImage2);

		double[] integralValues2 = new double[(int) integralImage2.total()];
		integralImage2.get(0, 0, integralValues2);

		int rectangleRows2 = 5;
		int rectangleCols2 = integralImage2.cols() - 1 ;
		int rowStep2 = 5;
		int colStep2 = 5;

		List<Integer> highGradientYRows = new ArrayList<>();

		SlidingWindowExecutor.process(integralImage2, rectangleRows2, rectangleCols2, rowStep2, colStep2,
				new SlidingWindowAlgorithm() {

					@Override
					public void processRoi(int row, int col) {
						double areaValue = getAreaValue(row, col, rectangleRows2, rectangleCols2, integralImage2.cols(),
								integralValues2);

						if (areaValue > 75000) {
							highGradientYRows.add(row);
						}
					}

				});
		
		
		summarizePoints(highGradientXCols);
		summarizePoints(highGradientYRows);
		
	
		for (Integer x : highGradientXCols) {
			Imgproc.drawMarker(frame, new Point(x, 300), new Scalar(255, 0, 0), Imgproc.MARKER_CROSS, 5, 2,
					Imgproc.LINE_4);
		}	
		for (Integer y : highGradientYRows) {
			Imgproc.drawMarker(frame, new Point(150, y), new Scalar(255, 0, 0), Imgproc.MARKER_CROSS, 5, 2,
					Imgproc.LINE_4);
		}
		
	}

	private void summarizePoints(List<Integer> pointList) {
		int maxDistance = 30;
		int clusterStart = -1;
		int clusterEnd = -1;
		
		for(int i = 0; i < pointList.size() - 1; i++) {
			int distance = Math.abs(pointList.get(i) - pointList.get(i+1));
			if(distance <= maxDistance) {
				if(clusterStart == -1) {
					clusterStart = i;
				}
				clusterEnd = i+1;
				if(i == pointList.size() - 2) {
					if(clusterStart != -1) {
						int averageValue = 0;
						for (int j = clusterStart; j <= clusterEnd; j++) {
							averageValue += pointList.get(j);
						}
						averageValue /= ((clusterEnd - clusterStart) + 1);
						
						for(int k = 0; k < ((clusterEnd - clusterStart)) + 1; k++) {
							pointList.remove(clusterStart);
						}
						pointList.add(clusterStart, averageValue);
					}
				}
			} else {
				if(clusterStart != -1) {
					int averageValue = 0;
					for (int j = clusterStart; j <= clusterEnd; j++) {
						averageValue += pointList.get(j);
					}
					averageValue /= ((clusterEnd - clusterStart) + 1);
					
					for(int k = 0; k < ((clusterEnd - clusterStart)) + 1; k++) {
						pointList.remove(clusterStart);
					}
					pointList.add(clusterStart, averageValue);
					i -= ((clusterEnd - clusterStart) + 1);
					clusterEnd = -1;
					clusterStart = -1;
				}
			}
		}
	}

	private void processImageShape(Mat frame) {
		Mat procFrame = frame.clone();
		Imgproc.cvtColor(procFrame, procFrame, Imgproc.COLOR_BGR2GRAY);
		/*
		 * Apply normalized Box Filter which at each position calculates the
		 * average value of the pixels in a block with the given size
		 */
		Imgproc.blur(procFrame, procFrame, new Size(5, 5));

		Mat gradientX = new Mat(procFrame.cols(), procFrame.rows(), CvType.CV_16S);
		Imgproc.Sobel(procFrame, gradientX, CvType.CV_32F, 1, 0, 3, 1, 0);

		float[] pixelsGradientX = new float[(int) gradientX.total()];
		gradientX.get(0, 0, pixelsGradientX);

		List<Integer> highGradientXCols = new ArrayList<>();

		for (int x = 0; x < gradientX.cols(); x++) {
			double colSum = 0;
			for (int y = 0; y < gradientX.rows(); y++) {
				colSum += Math.abs(getPixelValue(x, y, gradientX.cols(), pixelsGradientX));
			}
			if (colSum > 6000.0) {
				highGradientXCols.add(x);
			}
		}

		Mat gradientY = new Mat();
		Imgproc.Sobel(procFrame, gradientY, CvType.CV_32F, 0, 1, 3, 1, 0);

		float[] pixelsGradientY = new float[(int) gradientY.total()];
		gradientY.get(0, 0, pixelsGradientY);

		List<Integer> highGradientYRows = new ArrayList<>();

		for (int y = 0; y < gradientY.rows(); y++) {
			double rowSum = 0;
			for (int x = 0; x < gradientY.cols(); x++) {
				rowSum += Math.abs(getPixelValue(x, y, gradientY.cols(), pixelsGradientY));
			}
			if (rowSum > 8000.0) {
				highGradientYRows.add(y);
			}
		}

		int[] xClusterSamples = highGradientXCols.stream().mapToInt(i -> i).toArray();
		List<Double> clusters = kMeans(xClusterSamples, 4);
		for (Double center : clusters) {
			Imgproc.drawMarker(frame, new Point(center, 300), new Scalar(255, 0, 0), Imgproc.MARKER_CROSS, 5, 2,
					Imgproc.LINE_4);
		}

		clusters.clear();
		int[] yClusterSamples = highGradientYRows.stream().mapToInt(i -> i).toArray();
		clusters = kMeans(yClusterSamples, 4);
		for (Double center : clusters) {
			Imgproc.drawMarker(frame, new Point(50, center), new Scalar(255, 0, 0), Imgproc.MARKER_CROSS, 5, 2,
					Imgproc.LINE_4);
		}

		// for(Integer x : highGradientXCols) {
		// Imgproc.drawMarker(frame, new Point(x, 300), new Scalar(255,0,0),
		// Imgproc.MARKER_CROSS, 5, 2, Imgproc.LINE_4);
		// }
		// for(Integer y : highGradientYRows) {
		// Imgproc.drawMarker(frame, new Point(50, y), new Scalar(255,0,0),
		// Imgproc.MARKER_CROSS, 5, 2, Imgproc.LINE_4);
		// }

	}

	private static List<Double> kMeans(int[] data, int k) {

		Mat samples = new Mat(data.length, 1, CvType.CV_16S);
		for (int i = 0; i < data.length; i++) {
			samples.put(i, 0, data[i]);
		}
		Mat samples32f = new Mat();
		samples.convertTo(samples32f, CvType.CV_32F);

		Mat labels = new Mat();
		Mat centers = new Mat();
		TermCriteria criteria = new TermCriteria(TermCriteria.COUNT, 100, 1);
		double compactness = Core.kmeans(samples32f, k, labels, criteria, 1, Core.KMEANS_PP_CENTERS, centers);
		List<Double> resultCenters = new ArrayList<>(k);
		for (int i = 0; i < k; i++) {
			resultCenters.add(centers.get(i, 0)[0]);
		}
		return resultCenters;
	}

	private static float getPixelValue(int col, int row, int cols, float[] array) {
		int arrayPos = (row * cols) + col;
		return array[arrayPos];
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
