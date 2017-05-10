package de.dk_s.rubikscubesolver.views;

import de.dk_s.rubikscubesolver.domain.Cube;
import de.dk_s.rubikscubesolver.domain.CubeFace;
import javafx.application.ConditionalFeature;
import javafx.application.Platform;
import javafx.scene.Group;
import javafx.scene.PerspectiveCamera;
import javafx.scene.SubScene;
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

	private SubScene scene;

	private Cube cube;

	private final Rotate rotateX = new Rotate(-20, Rotate.X_AXIS);
	private final Rotate rotateY = new Rotate(-20, Rotate.Y_AXIS);

	public class Shape3DRectangle extends TriangleMesh {

		public Shape3DRectangle(float width, float height) {
			float[] points = { -width / 2, height / 2, 0,
					-width / 2, -height / 2, 0,
					width / 2, height / 2, 0,
					width / 2, -height / 2, 0 
			};
			float[] texCoords = { 1, 1,
					1, 0,
					0, 1,
					0, 0
			};

			 int[] faces = {
			 2, 2, 1, 1, 0, 0,
			 2, 2, 3, 3, 1, 1
			 };

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
					switch (color) {
					case 0:
						rectangle.setMaterial(new PhongMaterial(Color.GRAY));
						break;
					case 1:
						rectangle.setMaterial(new PhongMaterial(Color.BLUE));
						break;
					case 2:
						rectangle.setMaterial(new PhongMaterial(Color.RED));
						break;
					case 3:
						rectangle.setMaterial(new PhongMaterial(Color.GREEN));
						break;
					case 4:
						rectangle.setMaterial(new PhongMaterial(Color.ORANGE));
						break;
					case 5:
						rectangle.setMaterial(new PhongMaterial(Color.YELLOW));
						break;
					case 6:
						rectangle.setMaterial(new PhongMaterial(Color.WHITE));
						break;
					}
					rectangle.setTranslateX((-0.5 * size) + x * (size / 3));
					rectangle.setTranslateY((-0.5 * size) + y * (size / 3));
					getChildren().addAll(rectangle);
				}
			}

		}

	}

	public CubeRenderer(SubScene scene, Cube cube) {
		this.scene = scene;
		this.cube = cube;
		initialize();
	}

	private void initialize() {
		PerspectiveCamera camera = new PerspectiveCamera(true);
		float size = 10.0f;
		camera.getTransforms().addAll(rotateX, rotateY, new Translate(0, 0, -30));
		final MeshView rect = new MeshView(
                new Shape3DRectangle(10, 10)
        );
		rect.setMaterial(new PhongMaterial(Color.DARKGREEN));
		/* Yellow */
		CubeFaceGraphics frontFace = new CubeFaceGraphics(size, cube.getCubeFace(0));
		/* Orange */
		CubeFaceGraphics rightFace = new CubeFaceGraphics(size, cube.getCubeFace(1));
		rightFace.setRotationAxis(Rotate.Y_AXIS);
		rightFace.setRotate(-90.0f);
		rightFace.setTranslateX(size * 0.5);
		rightFace.setTranslateZ(size * 0.5);
		/* White */
		CubeFaceGraphics backFace = new CubeFaceGraphics(size, cube.getCubeFace(2));
		backFace.setRotationAxis(Rotate.X_AXIS);
		backFace.setRotate(180.0);
		backFace.setTranslateZ(size * 0.5);
		
		/* Red */
		CubeFaceGraphics leftFace = new CubeFaceGraphics(size, cube.getCubeFace(3));
		leftFace.setRotationAxis(Rotate.Y_AXIS);
		leftFace.setRotate(90.0f);
		leftFace.setTranslateX(-size * 0.5);
		leftFace.setTranslateZ(size * 0.5);
		
		/* Blue */
		CubeFaceGraphics topFace = new CubeFaceGraphics(size, cube.getCubeFace(4));
		topFace.setRotationAxis(Rotate.X_AXIS);
		topFace.setRotate(-90.0);
		topFace.setTranslateY(size * -0.5);
		topFace.setTranslateZ(size * 0.5);
		
		/* Green */
		CubeFaceGraphics bottomFace = new CubeFaceGraphics(size, cube.getCubeFace(5));
		bottomFace.setRotationAxis(Rotate.X_AXIS);
		bottomFace.setRotate(90.0);
		bottomFace.setTranslateY(size * -0.5);
		bottomFace.setTranslateZ(size * 0.5);
		
		
		Group root3D = new Group(camera, frontFace, rightFace, backFace, leftFace, topFace, bottomFace);
		scene.setRoot(root3D);
		scene.setFill(Color.AQUA);
		scene.setCamera(camera);
	}

}
