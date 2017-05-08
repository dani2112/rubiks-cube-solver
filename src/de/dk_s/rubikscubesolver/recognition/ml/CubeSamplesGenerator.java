package de.dk_s.rubikscubesolver.recognition.ml;

import java.io.File;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.videoio.VideoCapture;

import de.dk_s.rubikscubesolver.recognition.CubeDetector;
import de.dk_s.rubikscubesolver.recognition.ShapeCubeDetector;
import de.dk_s.rubikscubesolver.recognition.ShapeCubeDetector.CubePosition;

public class CubeSamplesGenerator {

	private static String trainingSetPath = "C:\\Users\\Daniel\\Code\\ml_resources\\rubikscube";
	
	private static Random rnd = null;

	public static void main(String[] args) {
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);

		Scanner sc = new Scanner(System.in);

		System.out.println("Press Enter to take picture from webcam");
		while (!sc.nextLine().equals("q")) {
			System.out.println("Press Enter to take picture from webcam");
			VideoCapture videoCapture = new VideoCapture(1);
			if (!videoCapture.isOpened()) {
				return;
			}
			Mat frame = getNextFrame(videoCapture);
			videoCapture.release();

			CubeDetector detector = new ShapeCubeDetector();
			CubePosition cubePosition = detector.recognize(frame);

			rnd = new Random(System.currentTimeMillis());
			
			if (cubePosition != null && cubePosition.cubePositionsX != null && cubePosition.cubePositionsY != null) {
				List<Integer> cubePositionsY = cubePosition.cubePositionsY;
				List<Integer> cubePositionsX = cubePosition.cubePositionsX;
				for (int row = 0; row < cubePositionsY.size() - 1; row++) {
					for (int col = 0; col < cubePositionsX.size() - 1; col++) {
						Mat subCube = frame.submat(cubePositionsY.get(row), cubePositionsY.get(row + 1),
								cubePositionsX.get(col), cubePositionsX.get(col + 1));
						System.out.println("Enter Class Number (1=blue, 2=red, 3=green, 4=orange, 5=yellow, 6=white)");
						int classNumber = sc.nextInt();
						String classPath = trainingSetPath + "\\" + classNumber;
						saveSample(subCube, classPath);
					}
				}
			}

		}

	}
	
	
	private static void saveSample(Mat frame, String classPath) {
		String filePath = "";
		boolean fileExists = true;
		while(fileExists) {
			String fileName = generateString(rnd, "0123456789", 10);
			filePath = classPath + "\\" + fileName + ".png";
			File file = new File(filePath);
			if(!file.exists()) {
				fileExists = false;
			}
		}
		
		Imgcodecs.imwrite(filePath, frame);
	}

	private static String generateString(Random rnd, String characters, int length)
	{
	    char[] text = new char[length];
	    for (int i = 0; i < length; i++)
	    {
	        text[i] = characters.charAt(rnd.nextInt(characters.length()));
	    }
	    return new String(text);
	}
	
	private static Mat getNextFrame(VideoCapture inputCapture) {
		Mat frame = new Mat();
		if (inputCapture.isOpened()) {
			inputCapture.read(frame);
		}
		return frame;
	}

}
