package de.dk_s.rubikscubesolver.views;

import java.util.Observable;
import java.util.Observer;

import de.dk_s.rubikscubesolver.domain.Cube;
import de.dk_s.rubikscubesolver.domain.CubeFace;
import javafx.application.ConditionalFeature;
import javafx.application.Platform;
import javafx.scene.Group;
import javafx.scene.PerspectiveCamera;
import javafx.scene.PointLight;
import javafx.scene.SubScene;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.paint.Material;
import javafx.scene.paint.Paint;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;
import javafx.scene.shape.DrawMode;
import javafx.scene.shape.MeshView;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.TriangleMesh;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Translate;

public class CubeRenderer {

	/* Rendering variables */
	private SubScene scene;

	private Group root3D;
	
	private final Rotate rotateX = new Rotate(0, Rotate.X_AXIS);
	private final Rotate rotateY = new Rotate(0, Rotate.Y_AXIS);

	private double mousePosX, mousePosY;
	private double mouseOldX, mouseOldY;
	
	private CubeFaceGraphics frontFace;

	private CubeFaceGraphics rightFace;

	private CubeFaceGraphics backFace;

	private CubeFaceGraphics leftFace;

	private CubeFaceGraphics topFace;

	private CubeFaceGraphics bottomFace;

	/* Domain variables */
	private Cube cube;

	public class Shape3DRectangle extends TriangleMesh {

		public Shape3DRectangle(float width, float height) {
			float[] points = { -width / 2, height / 2, 0, -width / 2, -height / 2, 0, width / 2, height / 2, 0,
					width / 2, -height / 2, 0 };
			float[] texCoords = { 1, 1, 1, 0, 0, 1, 0, 0 };

			int[] faces = { 2, 2, 1, 1, 0, 0, 2, 2, 3, 3, 1, 1 };

			this.getPoints().setAll(points);
			this.getTexCoords().setAll(texCoords);
			this.getFaces().setAll(faces);
		}
	}

	public class CubeFaceGraphics extends Group {

		CubeFace cubeFace;
		float size;

		public CubeFaceGraphics(float size, CubeFace cubeFace) {
			this.cubeFace = cubeFace;
			this.size = size;
			createCubeFace();
		}

		private void createCubeFace() {
			// Rectangle rectangle = new Rectangle();
			// rectangle.setWidth(size);
			// rectangle.setHeight(size);
			// rectangle.setFill(color);
			// rectangle.setTranslateX(translateX);
			// rectangle.setTranslateY(translateY);
			// rectangle.setTranslateZ(translateZ);
			// rectangle.setRotationAxis(Rotate.X_AXIS);
			// rectangle.setRotate(rotateXAxis);
			// rectangle.setRotationAxis(Rotate.Y_AXIS);
			// rectangle.setRotate(rotateYAxis);
			for (int y = 0; y < 3; y++) {
				for (int x = 0; x < 3; x++) {
					int color = cubeFace.getSubCubeColor(x, y);
					MeshView rectangle = new MeshView(new Shape3DRectangle(size / 3, size / 3));
					PhongMaterial material = new PhongMaterial();
					//material.setSpecularColor(Color.WHITE);
					switch (color) {
					case 0:
						material.setDiffuseColor(Color.GRAY);
						rectangle.setMaterial(material);
						break;
					case 1:
						material.setDiffuseColor(Color.BLUE);
						rectangle.setMaterial(material);
						break;
					case 2:
						material.setDiffuseColor(Color.RED);
						rectangle.setMaterial(material);
						break;
					case 3:
						material.setDiffuseColor(Color.GREEN);
						rectangle.setMaterial(material);
						break;
					case 4:
						material.setDiffuseColor(Color.ORANGE);
						rectangle.setMaterial(material);
						break;
					case 5:
						material.setDiffuseColor(Color.YELLOW);
						rectangle.setMaterial(material);
						break;
					case 6:
						material.setDiffuseColor(Color.WHITE);
						rectangle.setMaterial(material);
						break;
					}
					rectangle.setTranslateX((-(size / 3)) + x * (size / 3));
					rectangle.setTranslateY((-(size / 3)) + y * (size / 3));
					getChildren().addAll(rectangle);
				}
			}

		}

	}

	public CubeRenderer(SubScene scene, Cube cube) {
		this.scene = scene;
		this.cube = cube;
		cube.addObserver(new Observer() {

			@Override
			public void update(Observable o, Object arg) {

				Platform.runLater(new Runnable() {

					@Override
					public void run() {
						render();
					}
				});

			}

		});
		initialize();
		render();
	}

	private void render() {
		root3D.getChildren().removeAll(frontFace, rightFace, backFace, leftFace, topFace, bottomFace);

		float size = 10.0f;
		/* Green */
		frontFace = new CubeFaceGraphics(size, cube.getCubeFace(0));
		frontFace.setTranslateZ(-size*0.5);
		
		/* Red */
		rightFace = new CubeFaceGraphics(size, cube.getCubeFace(1));
		rightFace.setRotationAxis(Rotate.Y_AXIS);
		rightFace.setRotate(-90.0f);
		rightFace.setTranslateX(size * 0.5);
		rightFace.setTranslateZ(0);
		/* Blue */
		backFace = new CubeFaceGraphics(size, cube.getCubeFace(2));
		backFace.setRotationAxis(Rotate.Y_AXIS);
		backFace.setRotate(180.0);
		backFace.setTranslateZ(size * 0.5);

		/* Orange */
		leftFace = new CubeFaceGraphics(size, cube.getCubeFace(3));
		leftFace.setRotationAxis(Rotate.Y_AXIS);
		leftFace.setRotate(90.0f);
		leftFace.setTranslateX(-size * 0.5);
		leftFace.setTranslateZ(0);

		/* White */
		topFace = new CubeFaceGraphics(size, cube.getCubeFace(4));
		topFace.setRotationAxis(Rotate.X_AXIS);
		topFace.setRotate(-90.0);
		topFace.setTranslateY(size * -0.5);
		topFace.setTranslateZ(0);

		/* Yellow */
		bottomFace = new CubeFaceGraphics(size, cube.getCubeFace(5));
		bottomFace.setRotationAxis(Rotate.X_AXIS);
		bottomFace.setRotate(90.0);
		bottomFace.setTranslateY(size * 0.5);
		bottomFace.setTranslateZ(0);

		root3D.getChildren().addAll(frontFace, rightFace, backFace, leftFace, topFace, bottomFace);
	}

	private void initialize() {
		PerspectiveCamera camera = new PerspectiveCamera(true);
		float size = 10.0f;
		camera.getTransforms().addAll(rotateX, rotateY, new Translate(0, 0, -30));

//		PointLight light = new PointLight(Color.WHITE);
//		light.setTranslateZ(0f);
//		light.setTranslateX(20.0f);

		root3D = new Group(camera);
		scene.setRoot(root3D);
		scene.setFill(Color.WHITE);
		scene.setCamera(camera);
		
		 scene.setOnMousePressed((MouseEvent me) -> {
		        mouseOldX = me.getSceneX();
		        mouseOldY = me.getSceneY();
		    });
		    scene.setOnMouseDragged((MouseEvent me) -> {
		        mousePosX = me.getSceneX();
		        mousePosY = me.getSceneY();
		        rotateX.setAngle(rotateX.getAngle()-(mousePosY - mouseOldY));
		        rotateY.setAngle(rotateY.getAngle()+(mousePosX - mouseOldX));
		        mouseOldX = mousePosX;
		        mouseOldY = mousePosY;
		    });
	}

}
