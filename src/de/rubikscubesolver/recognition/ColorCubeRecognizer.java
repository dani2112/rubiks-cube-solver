package de.rubikscubesolver.recognition;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.core.TermCriteria;
import org.opencv.imgproc.CLAHE;
import org.opencv.imgproc.Imgproc;

import de.rubikscubesolver.recognition.SlidingWindowExecutor.SlidingWindowAlgorithm;

public class ColorCubeRecognizer implements CubeRecognizer {

	public static class Distance {

		public Distance(int similarDistances, double averageOfSimilarDistances) {
			this.similarDistances = similarDistances;
			this.averageOfSimilarDistances = averageOfSimilarDistances;
		}

		public int similarDistances;
		public double averageOfSimilarDistances;
	}

	@Override
	public void recognize(Mat frame) {
		processImageShape(frame);
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

						if (areaValue > 50000) {
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
		int rectangleCols2 = integralImage2.cols() - 1;
		int rowStep2 = 5;
		int colStep2 = 5;

		List<Integer> highGradientYRows = new ArrayList<>();

		SlidingWindowExecutor.process(integralImage2, rectangleRows2, rectangleCols2, rowStep2, colStep2,
				new SlidingWindowAlgorithm() {

					@Override
					public void processRoi(int row, int col) {
						double areaValue = getAreaValue(row, col, rectangleRows2, rectangleCols2, integralImage2.cols(),
								integralValues2);

						if (areaValue > 60000) {
							highGradientYRows.add(row);
						}
					}

				});

		summarizePoints(highGradientXCols);
		summarizePoints(highGradientYRows);

		List<Distance> similarCountsPerDistanceX = new ArrayList<>();
		List<Distance> similarCountsPerDistanceY = new ArrayList<>();
		double averageDistance = doDistanceVoting(highGradientXCols, highGradientYRows, similarCountsPerDistanceX,
				similarCountsPerDistanceY);

		List<Integer> selectedPointsX = new ArrayList<>();
		if (!Double.isNaN(averageDistance)) {
			selectPoints(highGradientXCols, similarCountsPerDistanceX, selectedPointsX, averageDistance);
			if (selectedPointsX.size() != 0 && isPointListSorted(selectedPointsX)) {
				Imgproc.drawMarker(frame, new Point(selectedPointsX.get(0), 300), new Scalar(0, 255, 0),
						Imgproc.MARKER_CROSS, 5, 2, Imgproc.LINE_4);
				Imgproc.drawMarker(frame, new Point(selectedPointsX.get(1), 300), new Scalar(255, 0, 0),
						Imgproc.MARKER_CROSS, 5, 2, Imgproc.LINE_4);
				Imgproc.drawMarker(frame, new Point(selectedPointsX.get(2), 300), new Scalar(0, 0, 255),
						Imgproc.MARKER_CROSS, 5, 2, Imgproc.LINE_4);
				Imgproc.drawMarker(frame, new Point(selectedPointsX.get(3), 300), new Scalar(255, 0, 255),
						Imgproc.MARKER_CROSS, 5, 2, Imgproc.LINE_4);
			}
		}

		
		List<Integer> selectedPointsY = new ArrayList<>();
		if (!Double.isNaN(averageDistance)) {
			selectPoints(highGradientYRows, similarCountsPerDistanceY, selectedPointsY, averageDistance);
			if (selectedPointsY.size() != 0 && isPointListSorted(selectedPointsY)) {
				Imgproc.drawMarker(frame, new Point(50, selectedPointsY.get(0)), new Scalar(0, 255, 0),
						Imgproc.MARKER_CROSS, 5, 2, Imgproc.LINE_4);
				Imgproc.drawMarker(frame, new Point(50, selectedPointsY.get(1)), new Scalar(255, 0, 0),
						Imgproc.MARKER_CROSS, 5, 2, Imgproc.LINE_4);
				Imgproc.drawMarker(frame, new Point(50, selectedPointsY.get(2)), new Scalar(0, 0, 255),
						Imgproc.MARKER_CROSS, 5, 2, Imgproc.LINE_4);
				Imgproc.drawMarker(frame, new Point(50, selectedPointsY.get(3)), new Scalar(255, 0, 255),
						Imgproc.MARKER_CROSS, 5, 2, Imgproc.LINE_4);
			}
		}
		
		// for (Integer x : highGradientXCols) {
		// Imgproc.drawMarker(frame, new Point(x, 300), new Scalar(255, 0, 0),
		// Imgproc.MARKER_CROSS, 5, 2,
		// Imgproc.LINE_4);
		// }
		// for (Integer y : highGradientYRows) {
		// Imgproc.drawMarker(frame, new Point(150, y), new Scalar(255, 0, 0),
		// Imgproc.MARKER_CROSS, 5, 2,
		// Imgproc.LINE_4);
		// }

	}
	
	public boolean isPointListSorted(List<Integer> pointList)
	{
	    boolean sorted = true;        
	    for (int i = 1; i < pointList.size(); i++) {
	        if (pointList.get(i-1).compareTo(pointList.get(i)) > 0) sorted = false;
	    }

	    return sorted;
	}

	private void selectPoints(List<Integer> pointList, List<Distance> distances, List<Integer> selectedPointsList,
			double averageDistance) {
		if (pointList.size() == 0) {
			return;
		}
		/* Berechnung von Wahrscheinlichkeiten für die Begrenzungspunkte */
		List<Double> pointProbabilities = new ArrayList<>();
		/* Berechnung für Startpunkt */
		int pointIndex = -1;
		double maxProbability = Double.MIN_VALUE;
		for (int i = 0; i < pointList.size(); i++) {
			int pos1 = (int) (pointList.get(i) + averageDistance);
			int pos2 = (int) (pointList.get(i) + (2 * averageDistance));
			int pos3 = (int) (pointList.get(i) + (3 * averageDistance));
			double probability = 1 / (double) (getMinimumDistance(pointList, pos1) + getMinimumDistance(pointList, pos2)
					+ getMinimumDistance(pointList, pos3));
			if (probability > maxProbability) {
				maxProbability = probability;
				pointIndex = i;
			}
		}
		selectedPointsList.add(pointList.get(pointIndex));
		
		pointIndex = -1;
		maxProbability = Double.MIN_VALUE;
		for (int i = 0; i < pointList.size(); i++) {
			int pos1 = (int) (pointList.get(i) - averageDistance);
			int pos2 = (int) (pointList.get(i) + averageDistance);
			int pos3 = (int) (pointList.get(i) + (2 * averageDistance));
			double probability = 1 / (double) (getMinimumDistance(pointList, pos1) + getMinimumDistance(pointList, pos2)
					+ getMinimumDistance(pointList, pos3));
			if (probability > maxProbability) {
				maxProbability = probability;
				pointIndex = i;
			}
		}
		selectedPointsList.add(pointList.get(pointIndex));
		
		pointIndex = -1;
		maxProbability = Double.MIN_VALUE;
		for (int i = 0; i < pointList.size(); i++) {
			int pos1 = (int) (pointList.get(i) - averageDistance);
			int pos2 = (int) (pointList.get(i) - (2 * averageDistance));
			int pos3 = (int) (pointList.get(i) + averageDistance);
			double probability = 1 / (double) (getMinimumDistance(pointList, pos1) + getMinimumDistance(pointList, pos2)
					+ getMinimumDistance(pointList, pos3));
			if (probability > maxProbability) {
				maxProbability = probability;
				pointIndex = i;
			}
		}
		selectedPointsList.add(pointList.get(pointIndex));
		
		pointIndex = -1;
		maxProbability = Double.MIN_VALUE;
		for (int i = 0; i < pointList.size(); i++) {
			int pos1 = (int) (pointList.get(i) - averageDistance);
			int pos2 = (int) (pointList.get(i) - (2 * averageDistance));
			int pos3 = (int) (pointList.get(i) - (3 * averageDistance));
			double probability = 1 / (double) (getMinimumDistance(pointList, pos1) + getMinimumDistance(pointList, pos2)
					+ getMinimumDistance(pointList, pos3));
			if (probability > maxProbability) {
				maxProbability = probability;
				pointIndex = i;
			}
		}
		selectedPointsList.add(pointList.get(pointIndex));
	}

	private int getMinimumDistance(List<Integer> pointList, int position) {
		int minDistance = Integer.MAX_VALUE;
		for (int i = 0; i < pointList.size(); i++) {
			int currentPosition = pointList.get(i);
			int distance = Math.abs(currentPosition - position);
			if (distance < minDistance) {
				minDistance = distance;
			}
		}
		return minDistance;
	}

	private double doDistanceVoting(List<Integer> pointListX, List<Integer> pointListY, List<Distance> distancesX,
			List<Distance> distancesY) {
		/*
		 * Tolerance that distances can be different to still be voted as the
		 * same
		 */
		int tolerance = 30;

		/* Index and value of the maximum similarity Distance in x direction */
		int maxSimilarityIndexX = -1;
		int maxSimilarityX = Integer.MIN_VALUE;

		/*
		 * Compare each Distance in X direction with all other distances and
		 * safe count of similar distances in similarCountsPerDistanceX
		 */
		for (int i = 0; i < pointListX.size() - 1; i++) {
			int distance = Math.abs(pointListX.get(i) - pointListX.get(i + 1));
			int similarCounts = 0;
			double averageDistance = 0.0;
			for (int j = 0; j < pointListX.size() - 1; j++) {
				int compareDistance = Math.abs(pointListX.get(j) - pointListX.get(j + 1));
				if (i == j) {
					continue;
				} else {
					if (Math.abs(distance - compareDistance) < tolerance) {
						similarCounts++;
						averageDistance += distance;
					}
				}
			}
			for (int j = 0; j < pointListY.size() - 1; j++) {
				int compareDistance = Math.abs(pointListY.get(j) - pointListY.get(j + 1));
				if (Math.abs(distance - compareDistance) < tolerance) {
					similarCounts++;
					averageDistance += distance;
				}
			}
			averageDistance /= similarCounts;
			distancesX.add(new Distance(similarCounts, averageDistance));
			if (similarCounts > maxSimilarityX) {
				maxSimilarityX = similarCounts;
				maxSimilarityIndexX = distancesX.size() - 1;
			}

		}
		/* Index and value of the maximum similarity Distance in y direction */
		int maxSimilarityIndexY = -1;
		int maxSimilarityY = Integer.MIN_VALUE;
		/*
		 * Compare each Distance in Y direction with all other distances and
		 * safe count of similar distances in similarCountsPerDistanceY
		 */
		for (int i = 0; i < pointListY.size() - 1; i++) {
			int distance = Math.abs(pointListY.get(i) - pointListY.get(i + 1));
			int similarCounts = 0;
			double averageDistance = 0.0;
			for (int j = 0; j < pointListY.size() - 1; j++) {
				int compareDistance = Math.abs(pointListY.get(j) - pointListY.get(j + 1));
				if (i == j) {
					continue;
				} else {
					if (Math.abs(distance - compareDistance) < tolerance) {
						similarCounts++;
						averageDistance += distance;
					}
				}
			}
			for (int j = 0; j < pointListX.size() - 1; j++) {
				int compareDistance = Math.abs(pointListX.get(j) - pointListX.get(j + 1));
				if (Math.abs(distance - compareDistance) < tolerance) {
					similarCounts++;
					averageDistance += distance;
				}
			}
			averageDistance /= similarCounts;
			distancesY.add(new Distance(similarCounts, averageDistance));

			if (similarCounts > maxSimilarityY) {
				maxSimilarityY = similarCounts;
				maxSimilarityIndexY = distancesY.size() - 1;
			}
		}

		double averageDistance = 0.0;
		if (maxSimilarityIndexX != -1 || maxSimilarityIndexY != -1) {
			if (maxSimilarityX > maxSimilarityY) {
				averageDistance = distancesX.get(maxSimilarityIndexX).averageOfSimilarDistances;
			} else if (maxSimilarityY >= maxSimilarityX) {
				averageDistance = distancesY.get(maxSimilarityIndexY).averageOfSimilarDistances;
			}
		}
		return averageDistance;
	}

	private void summarizePoints(List<Integer> pointList) {
		int maxDistance = 30;
		int clusterStart = -1;
		int clusterEnd = -1;

		for (int i = 0; i < pointList.size() - 1; i++) {
			int distance = Math.abs(pointList.get(i) - pointList.get(i + 1));
			if (distance <= maxDistance) {
				if (clusterStart == -1) {
					clusterStart = i;
				}
				clusterEnd = i + 1;
				if (i == pointList.size() - 2) {
					if (clusterStart != -1) {
						int averageValue = 0;
						for (int j = clusterStart; j <= clusterEnd; j++) {
							averageValue += pointList.get(j);
						}
						averageValue /= ((clusterEnd - clusterStart) + 1);

						for (int k = 0; k < ((clusterEnd - clusterStart)) + 1; k++) {
							pointList.remove(clusterStart);
						}
						pointList.add(clusterStart, averageValue);
					}
				}
			} else {
				if (clusterStart != -1) {
					int averageValue = 0;
					for (int j = clusterStart; j <= clusterEnd; j++) {
						averageValue += pointList.get(j);
					}
					averageValue /= ((clusterEnd - clusterStart) + 1);

					for (int k = 0; k < ((clusterEnd - clusterStart)) + 1; k++) {
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
