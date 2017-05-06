package de.rubikscubesolver.recognition.ml;

import java.util.ArrayList;
import java.util.List;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

import de.rubikscubesolver.recognition.SlidingWindowExecutor;
import de.rubikscubesolver.recognition.SlidingWindowExecutor.SlidingWindowAlgorithm;
import libsvm.svm_node;

public class SubCubeFeatureExtractor implements FeatureExtractor {

	public double[] extractFeatures(Mat roi) {
		if(roi.cols() < 50 || roi.rows() < 50) {
			return null;
		}
		int roiCols = roi.cols();
        int roiRows = roi.rows();
        int difference = roiRows - roiCols;
        Mat roiCut = new Mat();
        float scaleFactor = 0;
        if (difference > 0) {
            scaleFactor = 50 / (float) roiCols;
            Imgproc.resize(roi, roi, new Size(50, scaleFactor * roiRows));
            roiCut = roi.submat(0, 50, 0, 50);
        } else if (difference < 0) {
            scaleFactor = 50 / (float) roiRows;
            Imgproc.resize(roi, roi, new Size(scaleFactor * roiCols, 50));
            roiCut = roi.submat(0, 50, 0, 50);
        } else {
            Imgproc.resize(roi, roi, new Size(50, 50));
            roiCut = roi.submat(0, 50, 0, 50);
        }
        Mat hsvRoi = new Mat();
        Imgproc.cvtColor(roiCut, hsvRoi, Imgproc.COLOR_BGR2HSV_FULL);
        
        List<Double> features = new ArrayList<>();
        SlidingWindowExecutor.process(hsvRoi, 25, 25, 5, 5, new SlidingWindowAlgorithm() {

			@Override
			public void processRoi(int row, int col) {
				Mat currentWindow = hsvRoi.submat(row, row + 10, col, col + 10);
				Scalar mean = Core.mean(currentWindow);
				features.add(mean.val[0]);
				features.add(mean.val[1]);
			}
        	
        });
        double[] featureArray = new double[features.size()];
        for(int i = 0; i < featureArray.length; i++) {
        	featureArray[i] = features.get(i);
        }
        
		return featureArray;
	}
	
}
