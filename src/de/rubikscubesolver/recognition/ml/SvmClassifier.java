package de.rubikscubesolver.recognition.ml;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Locale;
import java.util.Scanner;

import org.opencv.core.Mat;

import libsvm.svm;
import libsvm.svm_model;
import libsvm.svm_node;

public class SvmClassifier {

	private String modelPath = null;

	private String rangePath = null;

	private svm_model model = null;

	private FeatureExtractor featureExtractor = null;

	HashMap<Integer, Double[]> scalingHashMap = null;

	public SvmClassifier(String modelPath, String rangePath, FeatureExtractor featureExtractor) {
		this.modelPath = modelPath;
		this.rangePath = rangePath;
		this.featureExtractor = featureExtractor;
		model = loadModelFromFile(modelPath);
		scalingHashMap = loadScalingHashMapFromFile(rangePath);
	}

	public int classify(Mat frame) {
		double[] features = featureExtractor.extractFeatures(frame);
		if(features == null) {
			return 0;
		}
		svm_node[] featureSvmNodes = new svm_node[features.length];
		for (int i = 0; i < features.length; i++) {
			svm_node node = new svm_node();
			node.index = i + 1;
			if (scalingHashMap == null) {
				node.value = features[i];
			} else {
				Double[] minMax = scalingHashMap.get(i+1);
				// scale to range -1 +1 see internet for formula
				node.value = ((2 * (features[i] - minMax[0])) / (minMax[1] - minMax[0])) - 1;
			}
			featureSvmNodes[i] = node;
		}
		double classLabel = svm.svm_predict(model, featureSvmNodes);
		return (int)classLabel;
	}

	private static svm_model loadModelFromFile(String modelPath) {
		BufferedReader reader;
		svm_model model = null;
		try {
			reader = new BufferedReader(new FileReader(modelPath));
			model = svm.svm_load_model(reader);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return model;
	}

	private static HashMap<Integer, Double[]> loadScalingHashMapFromFile(String scalingPath) {
		File file = new File(scalingPath);
		Scanner sc;

		HashMap<Integer, Double[]> scalingHashMap = null;
		try {
			sc = new Scanner(file);
			sc.useLocale(Locale.US);

			scalingHashMap = new HashMap<>();
			sc.nextLine();
			sc.nextLine();
			while (sc.hasNext()) {
				Integer featureIndex = sc.nextInt();
				double minFeatureValue = sc.nextDouble();
				double maxFeatureValue = sc.nextDouble();
				scalingHashMap.put(featureIndex, new Double[] {minFeatureValue, maxFeatureValue});
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return scalingHashMap;
	}

}
