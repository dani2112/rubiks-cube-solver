package de.dk_s.rubikscubesolver.views;

import java.io.ByteArrayInputStream;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.videoio.VideoCapture;

import de.rubikscubesolver.recognition.CubeRecognizer;
import de.rubikscubesolver.recognition.ShapeCubeDetector;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class MainController {

	@FXML
	private Button startCameraButton;
	@FXML
	private ImageView currentFrameView;

	private boolean isInputStarted = false;
	
	private VideoCapture videoCapture = null;
	
	private ScheduledExecutorService executor = null;

	private CubeRecognizer cubeRecognizer = null;
	
	private Runnable frameGrabber = new Runnable() {

		@Override
		public void run() {
			Mat frame = getNextFrame(videoCapture);
			cubeRecognizer.recognize(frame);
			Image frameImage = mat2Image(frame);
			Platform.runLater(new Runnable() {
				public void run() {
					if (frameImage != null) {
						currentFrameView.setImage(frameImage);
					}
				}
			});
		}

	};

	public MainController() {
		this.cubeRecognizer = new CubeRecognizer();
	}
	
	@FXML
	protected void startCamera(ActionEvent event) {
		if(isInputStarted) {
			
			try {
				executor.shutdown();
				executor.awaitTermination(100, TimeUnit.MILLISECONDS);
			} catch (InterruptedException e) {
				System.out.println("Could not shutdown frame Capture!");
			}
			isInputStarted = false;
		} else {
			videoCapture = new VideoCapture(1);
			if (this.videoCapture.isOpened()) {
				executor = Executors.newSingleThreadScheduledExecutor();
				executor.scheduleAtFixedRate(frameGrabber, 0, 33, TimeUnit.MILLISECONDS);
			}
			isInputStarted = true;
		}
	}

	public void setClosed() {
		// TODO Auto-generated method stub

	}
	
	/* Helper functions for video capturing */
	private Mat getNextFrame(VideoCapture inputCapture) {
		Mat frame = new Mat();
		if (inputCapture.isOpened()) {
			inputCapture.read(frame);
		}
		return frame;
	}

	private Image mat2Image(Mat frame) {
		MatOfByte buffer = new MatOfByte();
		Imgcodecs.imencode(".png", frame, buffer);
		return new Image(new ByteArrayInputStream(buffer.toArray()));
	}

}
