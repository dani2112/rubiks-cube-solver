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

public class ColorCubeRecognizer implements CubeRecognizer {

	@Override
	public void recognize(Mat frame) {
		//processImageColor(frame);
		//processImageShape(frame);
		processSlidingWindow(frame);
	}
	
	private void processSlidingWindow(Mat frame) {
		CubeEdgeDetector.detect(frame);
	}
	
	private void processImageColor(Mat frame) {
		Imgproc.cvtColor(frame, frame, Imgproc.COLOR_BGR2HSV);
		Core.inRange(frame, new Scalar(160, 60, 60), new Scalar(180, 255, 255), frame);
	}

	private void processImageShape(Mat frame) {
		Mat procFrame = frame.clone();
		Imgproc.cvtColor(procFrame, procFrame, Imgproc.COLOR_BGR2GRAY);
		/* Apply normalized Box Filter which at each position calculates the average value
		 * of the pixels in a block with the given size*/
		Imgproc.blur(procFrame, procFrame, new Size(5, 5));
		
		Mat gradientX = new Mat(procFrame.cols(), procFrame.rows(), CvType.CV_16S);
		Imgproc.Sobel(procFrame, gradientX, CvType.CV_32F, 1, 0, 3, 1, 0);
		
		float[] pixelsGradientX = new float[(int) gradientX.total()];
		gradientX.get(0, 0, pixelsGradientX);
		
		
		List<Integer> highGradientXCols = new ArrayList<>();
		
		for(int x = 0; x < gradientX.cols(); x++) {
			double colSum = 0;
			for(int y = 0; y < gradientX.rows(); y++) {
				colSum += Math.abs(getPixelValue(x, y, gradientX.cols(), pixelsGradientX));
			}
			if(colSum > 6000.0) {
				highGradientXCols.add(x);
			}
		}
		
		Mat gradientY = new Mat();
		Imgproc.Sobel(procFrame, gradientY, CvType.CV_32F, 0, 1, 3, 1, 0);
		
		float[] pixelsGradientY = new float[(int) gradientY.total()];
		gradientY.get(0, 0, pixelsGradientY);
		
		
		List<Integer> highGradientYRows = new ArrayList<>();
		
		for(int y = 0; y < gradientY.rows(); y++) {
			double rowSum = 0;
			for(int x = 0; x < gradientY.cols(); x++) {
				rowSum += Math.abs(getPixelValue(x, y, gradientY.cols(), pixelsGradientY));
			}
			if(rowSum > 8000.0) {
				highGradientYRows.add(y);
			}
		}
		
		
		int[] xClusterSamples = highGradientXCols.stream().mapToInt(i->i).toArray();	
		List<Double> clusters = kMeans(xClusterSamples, 4);
		for(Double center : clusters) {
			Imgproc.drawMarker(frame, new Point(center, 300), new Scalar(255,0,0), Imgproc.MARKER_CROSS, 5, 2, Imgproc.LINE_4);
		}
		
		
		clusters.clear();
		int[] yClusterSamples = highGradientYRows.stream().mapToInt(i->i).toArray();
		clusters = kMeans(yClusterSamples, 4);
		for(Double center : clusters) {
			Imgproc.drawMarker(frame, new Point(50, center), new Scalar(255,0,0), Imgproc.MARKER_CROSS, 5, 2, Imgproc.LINE_4);
		}
		
//		for(Integer x : highGradientXCols) {
//			Imgproc.drawMarker(frame, new Point(x, 300), new Scalar(255,0,0), Imgproc.MARKER_CROSS, 5, 2, Imgproc.LINE_4);
//		}
//		for(Integer y : highGradientYRows) {
//			Imgproc.drawMarker(frame, new Point(50, y), new Scalar(255,0,0), Imgproc.MARKER_CROSS, 5, 2, Imgproc.LINE_4);
//		}
		
	}
	
	private static List<Double> kMeans(int[] data, int k) {
		Mat samples = new Mat(data.length, 1, CvType.CV_16S);
		for(int i = 0; i < data.length; i++) {
			samples.put(i, 0, data[i]);
		}
		Mat samples32f = new Mat();
		samples.convertTo(samples32f, CvType.CV_32F);
		
		
		Mat labels = new Mat();
		Mat centers = new Mat();
		TermCriteria criteria = new TermCriteria(TermCriteria.COUNT, 1000, 1);
		double compactness = Core.kmeans(samples32f, k, labels, criteria, 1, Core.KMEANS_PP_CENTERS, centers);
		List<Double> resultCenters = new ArrayList<>(k);
		for(int i = 0; i < k; i++) {
			resultCenters.add(centers.get(i, 0)[0]);
		}
		return resultCenters;
	}
	
	private static float getPixelValue(int col, int row, int cols, float[] array) {
		int arrayPos = (row * cols) + col;
		return array[arrayPos];
	}
	
}
