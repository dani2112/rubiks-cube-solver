package de.dk_s.rubikscubesolver.views;

import java.io.ByteArrayInputStream;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.annotation.Resources;
import javax.print.DocFlavor.URL;

import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.videoio.VideoCapture;

import de.dk_s.rubikscubesolver.domain.Cube;
import de.dk_s.rubikscubesolver.recognition.CubeRecognizer;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.PerspectiveCamera;
import javafx.scene.Scene;
import javafx.scene.SceneAntialiasing;
import javafx.scene.SubScene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Translate;

public class MainController {
	
	/* Cube Model */
	private Cube cube;
	
	/* Cube Scanning */
	@FXML
	private Button startScanButton;
	@FXML
	private ImageView currentFrameView;
	@FXML
	private ChoiceBox<String> colorChoiceBox;
	
	private int selectedFaceId = 0;
	
	/* Animation */
	@FXML
	private SubScene subScene;
	
	private CubeRenderer cubeRenderer;

	/* Video capture */
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

	private final Rotate rotateX = new Rotate(-20, Rotate.X_AXIS);
	private final Rotate rotateY = new Rotate(-20, Rotate.Y_AXIS);
	
	@FXML
	public void initialize()  {
		/* Initialize color selection ChoiceBox */
		colorChoiceBox.setItems(FXCollections.observableArrayList("Yellow", "Orange", "White", "Red", "Blue", "Green"));
		
		colorChoiceBox.getSelectionModel().selectedIndexProperty()
        .addListener(new ChangeListener<Number>() {
          public void changed(ObservableValue ov, Number value, Number newValue) {
        	  selectedFaceId = newValue.intValue();
          }
        });
		
		this.cube = new Cube();
		this.cubeRecognizer = new CubeRecognizer();
		this.cubeRenderer = new CubeRenderer(subScene, cube);
	}
	
	@FXML
	protected void startScan(ActionEvent event) {
		if(isInputStarted) {
			stopScan();
		} else {
			startScan();
		}
	}
	
	private void startScan() {
		videoCapture = new VideoCapture(1);
		if (this.videoCapture.isOpened()) {
			executor = Executors.newSingleThreadScheduledExecutor();
			executor.scheduleAtFixedRate(frameGrabber, 0, 33, TimeUnit.MILLISECONDS);
		}
		isInputStarted = true;
	}
	
	private void stopScan() {
		try {
			executor.shutdown();
			executor.awaitTermination(100, TimeUnit.MILLISECONDS);
		} catch (InterruptedException e) {
			System.out.println("Could not shutdown frame Capture!");
		}
		isInputStarted = false;
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
