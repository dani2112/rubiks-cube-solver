package de.rubikscubesolver.recognition.ml;

import org.opencv.core.Mat;

import libsvm.svm_node;

public interface FeatureExtractor {

	public double[] extractFeatures(Mat roi);
	
	
}
