package de.dk_s.rubikscubesolver.recognition.ml;

import org.opencv.core.Mat;

import libsvm.svm_node;

public interface FeatureExtractor {

	public double[] extractFeatures(Mat roi);
	
	
}
