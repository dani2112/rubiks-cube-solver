package de.dk_s.rubikscubesolver;

import java.io.ByteArrayInputStream;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.videoio.VideoCapture;

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

	private VideoCapture videoCapture = null;

	private Runnable frameGrabber = new Runnable() {

		@Override
		public void run() {
			Mat frame = new Mat();
			videoCapture.read(frame);
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

	private ScheduledExecutorService executor = null;

	@FXML
	protected void startCamera(ActionEvent event) {
		videoCapture = new VideoCapture(1);
		if (this.videoCapture.isOpened()) {
			executor = Executors.newSingleThreadScheduledExecutor();
			executor.scheduleAtFixedRate(frameGrabber, 0, 33, TimeUnit.MILLISECONDS);
		}
	}

	public void setClosed() {
		// TODO Auto-generated method stub

	}

	private Image mat2Image(Mat frame) {
		MatOfByte buffer = new MatOfByte();
		Imgcodecs.imencode(".png", frame, buffer);
		return new Image(new ByteArrayInputStream(buffer.toArray()));
	}

}
