package de.rubikscubesolver.recognition.ml;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.DecimalFormat;
import java.util.Scanner;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;

public class TrainingDataGenerator {

	private static String trainingSetPath = "C:\\Users\\Daniel\\Code\\ml_resources\\rubikscube";

	public static void main(String[] args) {
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);

		Scanner sc = new Scanner(System.in);
		System.out.println("Enter File name for training dataset");

		String filename = sc.nextLine();

		File file = new File(trainingSetPath);
		File[] directories = file.listFiles(new FilenameFilter() {
			@Override
			public boolean accept(File current, String name) {
				return new File(current, name).isDirectory();
			}
		});

		SubCubeFeatureExtractor extractor = new SubCubeFeatureExtractor();
		StringBuilder sb = new StringBuilder();
		DecimalFormat format = new DecimalFormat("+#,##0.00;-#");
		format.setMinimumFractionDigits(0);

		for (File classDirectory : directories) {
			int classLabel = Integer.parseInt(classDirectory.getName());

			File[] sampleFiles = classDirectory.listFiles();
			for (File sampleFile : sampleFiles) {
				Mat frame = Imgcodecs.imread(sampleFile.getAbsolutePath());
				double[] features = extractor.extractFeatures(frame);
				if (features == null) {
					continue;
				}
				sb.append(format.format(classLabel));
				for (int i = 0; i < features.length; i++) {
					sb.append(" " + String.valueOf(i + 1) + ":" + String.valueOf(features[i]));
				}
				sb.append("\n");
			}
		}
		String filePath = trainingSetPath + "\\" + filename;
		try {
			Files.write(Paths.get(filePath), sb.toString().getBytes());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
