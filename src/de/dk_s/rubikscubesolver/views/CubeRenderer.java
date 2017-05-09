package de.dk_s.rubikscubesolver.views;

import de.dk_s.rubikscubesolver.domain.Cube;
import javafx.scene.Group;
import javafx.scene.PerspectiveCamera;
import javafx.scene.SubScene;
import javafx.scene.paint.Color;
import javafx.scene.paint.Material;
import javafx.scene.paint.Paint;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;
import javafx.scene.shape.DrawMode;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.TriangleMesh;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Translate;

public class CubeRenderer {

	private SubScene scene;

	private Cube cube;
    
    public static class CubeGraphics extends Group {
    	
    	public CubeGraphics() {
    		getChildren().addAll(createCubeFace(5, Color.BLUE, 0, 0, 0, 0, 0));
    	}
    	
    	
    	private static Rectangle createCubeFace(float size, Color color, float translateX, float translateY, float translateZ, float rotateXAxis, float rotateYAxis) {
    		Rectangle rectangle = new Rectangle();
    		rectangle.setWidth(size);
    		rectangle.setHeight(size);
    		rectangle.setFill(color);
    		rectangle.setTranslateX(translateX);
    		rectangle.setTranslateY(translateY);
    		rectangle.setTranslateZ(translateZ);
    		rectangle.setRotationAxis(Rotate.X_AXIS);
    		rectangle.setRotate(rotateXAxis);
    		rectangle.setRotationAxis(Rotate.Y_AXIS);
    		rectangle.setRotate(rotateYAxis);
    		
			return rectangle;
    		
    	}
    	
    	
    }

	public CubeRenderer(SubScene scene, Cube cube) {
		this.scene = scene;
		this.cube = cube;
		initialize();
	}

	private void initialize() {
//		Box box = new Box(5, 5, 5);
//		box.setMaterial(new PhongMaterial(Color.ORANGE));
		Rectangle rectangle = new Rectangle();
		rectangle.setWidth(5);
		rectangle.setHeight(5);
		rectangle.setFill(Color.ORANGE);
		PerspectiveCamera camera = new PerspectiveCamera(true);
		camera.getTransforms().addAll(new Translate(0, 0, -20));
		Group root3D = new Group(camera, new CubeGraphics());
		scene.setRoot(root3D);
		scene.setFill(Color.WHITE);
		scene.setCamera(camera);
	}

}
