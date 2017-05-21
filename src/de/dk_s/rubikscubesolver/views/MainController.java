package de.dk_s.rubikscubesolver.views;

import java.io.ByteArrayInputStream;
import java.util.Observable;
import java.util.Observer;
import java.util.Optional;
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
import de.dk_s.rubikscubesolver.domain.CubeFace;
import de.dk_s.rubikscubesolver.recognition.CubeRecognizer;
import de.dk_s.rubikscubesolver.solving.OldPochmannSolver;
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
import javafx.scene.control.TextInputDialog;
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

	@FXML
	private Button pickButton;

	private int selectedFaceId = 0;

	/* Solving */
	@FXML
	private Button showStepButton;

	private Button executeStepButton;

	private OldPochmannSolver solver;

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

			try {
				System.out.println("Frame");
				cubeRecognizer.recognize(frame);
				Image frameImage = mat2Image(frame);
				Platform.runLater(new Runnable() {
					public void run() {
						if (frameImage != null) {
							currentFrameView.setImage(frameImage);
						}
					}
				});
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	};

	private final Rotate rotateX = new Rotate(-20, Rotate.X_AXIS);
	private final Rotate rotateY = new Rotate(-20, Rotate.Y_AXIS);

	@FXML
	public void initialize() {
		this.cube = new Cube();
		this.cubeRecognizer = new CubeRecognizer(cube);
		cubeRecognizer.addObserver(new Observer() {

			@Override
			public void update(Observable arg0, Object cmd) {
				String command = (String) cmd;
				stopScan();
			}

		});

		this.solver = new OldPochmannSolver(cube);

		this.cubeRenderer = new CubeRenderer(subScene, cube);

		/* Initialize color selection ChoiceBox */
		colorChoiceBox.setItems(FXCollections.observableArrayList("Green", "Red", "Blue", "Orange", "White", "Yellow"));

		colorChoiceBox.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
			public void changed(ObservableValue ov, Number value, Number newValue) {
				selectedFaceId = newValue.intValue();
				cubeRecognizer.setCurrentCubeFaceIndex(selectedFaceId);
				cubeRecognizer.reset();
			}
		});

	}

	@FXML
	protected void startScan(ActionEvent event) {
		if (isInputStarted) {
			stopScan();
		} else {
			startScan();
			cubeRecognizer.setCurrentCubeFaceIndex(selectedFaceId);
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
			videoCapture.release();
		} catch (InterruptedException e) {
			System.out.println("Could not shutdown frame Capture!");
		}
		isInputStarted = false;
	}

	@FXML
	public void pickColor() {
		cubeRecognizer.pick();
	}

	@FXML
	public void correctFace() {
		TextInputDialog dialog = new TextInputDialog(faceToString());
		dialog.setTitle("Correct");
		dialog.setHeaderText("Correct the current face");
		dialog.setContentText("");

		// Traditional way to get the response value.
		Optional<String> result = dialog.showAndWait();
		if (result.isPresent()) {
			String correctedFace = result.get();
			CubeFace face = null;
			switch (this.selectedFaceId) {
			case 0:
				face = cube.getFrontCubeFace();
				break;
			case 1:
				face = cube.getRightCubeFace();
				break;
			case 2:
				face = cube.getBackCubeFace();
				break;
			case 3:
				face = cube.getLeftCubeFace();
				break;
			case 4:
				face = cube.getTopCubeFace();
				break;
			case 5:
				face = cube.getBottomCubeFace();
				break;
			}
			for (int y = 0; y < 3; y++) {
				for (int x = 0; x < 3; x++) {
					int index = y*3+x;
					int color = getColorIdByString(correctedFace.substring(index, index+1));
					face.setSubCubeColor(x, y, color);
				}
			}
		}
		cubeRenderer.render();
	}
	

	private String faceToString() {
		CubeFace face = null;
		switch (this.selectedFaceId) {
		case 0:
			face = cube.getFrontCubeFace();
			break;
		case 1:
			face = cube.getRightCubeFace();
			break;
		case 2:
			face = cube.getBackCubeFace();
			break;
		case 3:
			face = cube.getLeftCubeFace();
			break;
		case 4:
			face = cube.getTopCubeFace();
			break;
		case 5:
			face = cube.getBottomCubeFace();
			break;
		}
		StringBuilder str = new StringBuilder();
		for (int y = 0; y < 3; y++) {
			for (int x = 0; x < 3; x++) {
				int color = face.getSubCubeColor(x, y);
				str.append(getColorStringById(color));
			}
		}
		return str.toString();
	}

	private String getColorStringById(int colorId) {
		String colorName = "";
		switch (colorId) {
		case 1:
		colorName = "B";
			break;
		case 2:
			colorName = "R";
			break;
		case 3:
			colorName = "G";
			break;
		case 4:
			colorName = "O";
			break;
		case 5:
			colorName = "Y";
			break;
		case 6:
			colorName = "W";
			break;
		}
		return colorName;
	}
	
	private int getColorIdByString(String colorName) {
		int colorId = 0;
		switch(colorName) {
		case "B":
			colorId = 1;
			break;
		case "R":
			colorId = 2;
			break;
		case "G":
			colorId = 3;
			break;
		case "O":
			colorId = 4;
			break;
		case "Y":
			colorId = 5;
			break;
		case "W":
			colorId = 6;
			break;
		}
		return colorId;
	}

	@FXML
	public void showNextStep() {
		System.out.println("Show");
		solver.getNextMove();
	}

	@FXML
	public void executeNextStep() {
		solver.executeNextMove();
	}

	@FXML
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
