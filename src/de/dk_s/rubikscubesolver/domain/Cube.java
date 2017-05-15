package de.dk_s.rubikscubesolver.domain;

import java.util.Observable;

public class Cube extends Observable {

	private CubeFace[] cubeFaces = new CubeFace[6];

	int frontFaceIndex = 0;
	int rightFaceIndex = 1;
	int backFaceIndex = 2;
	int leftFaceIndex = 3;
	int topFaceIndex = 4;
	int bottomFaceIndex = 5;
	
	public Cube() {
		initializeCubeFaces();
	}

	private void initializeCubeFaces() {
		setFrontCubeFace(new CubeFace(3));
		setRightCubeFace(new CubeFace(2));
		setBackCubeFace(new CubeFace(1));
		setLeftCubeFace(new CubeFace(4));
		setTopCubeFace(new CubeFace(6));
		setBottomCubeFace(new CubeFace(5));
	}

	public CubeFace getFrontCubeFace() {
		return cubeFaces[frontFaceIndex];
	}
	
	public void setFrontCubeFace(CubeFace cubeFace) {
		cubeFaces[frontFaceIndex] = cubeFace;
		setChanged();
		notifyObservers("facesUpdated");
	}
	
	public CubeFace getRightCubeFace() {
		return cubeFaces[rightFaceIndex];
	}
	
	public void setRightCubeFace(CubeFace cubeFace) {
		cubeFaces[rightFaceIndex] = cubeFace;
		setChanged();
		notifyObservers("facesUpdated");
	}
	
	public CubeFace getBackCubeFace() {
		return cubeFaces[backFaceIndex];
	}
	
	public void setBackCubeFace(CubeFace cubeFace) {
		cubeFaces[backFaceIndex] = cubeFace;
		setChanged();
		notifyObservers("facesUpdated");
	}
	
	public CubeFace getLeftCubeFace() {
		return cubeFaces[leftFaceIndex];
	}
	
	public void setLeftCubeFace(CubeFace cubeFace) {
		cubeFaces[leftFaceIndex] = cubeFace;
		setChanged();
		notifyObservers("facesUpdated");
	}
	
	public CubeFace getTopCubeFace() {
		return cubeFaces[topFaceIndex];
	}
	
	public void setTopCubeFace(CubeFace cubeFace) {
		cubeFaces[topFaceIndex] = cubeFace;
		setChanged();
		notifyObservers("facesUpdated");
	}
	
	public CubeFace getBottomCubeFace() {
		return cubeFaces[bottomFaceIndex];
	}
	
	public void setBottomCubeFace(CubeFace cubeFace) {
		cubeFaces[bottomFaceIndex] = cubeFace;
		setChanged();
		notifyObservers("facesUpdated");
	}
	
	public void flip90DegreesBackwards() {
		int frontFaceIndexTmp = frontFaceIndex;
		int topFaceIndexTmp = topFaceIndex;
		int backFaceIndexTmp = backFaceIndex;
		int bottomFaceIndexTmp = bottomFaceIndex;
		
		frontFaceIndex = bottomFaceIndexTmp;
		topFaceIndex = frontFaceIndexTmp;
		backFaceIndex = topFaceIndexTmp;
		bottomFaceIndex = backFaceIndexTmp;
		getRightCubeFace().rotateClockWise();
		getLeftCubeFace().rotateCounterClockWise();
		setChanged();
		notifyObservers("facesUpdated");
	}
	
	public void flip90DegreesForward() {
		int frontFaceIndexTmp = frontFaceIndex;
		int topFaceIndexTmp = topFaceIndex;
		int backFaceIndexTmp = backFaceIndex;
		int bottomFaceIndexTmp = bottomFaceIndex;
		
		frontFaceIndex = topFaceIndexTmp;
		topFaceIndex = backFaceIndexTmp;
		backFaceIndex = bottomFaceIndexTmp;
		bottomFaceIndex = frontFaceIndexTmp;
		getRightCubeFace().rotateCounterClockWise();
		getLeftCubeFace().rotateClockWise();
		setChanged();
		notifyObservers("facesUpdated");
	}
	
	public void flip90DegreesLeft() {
		int leftFaceIndexTmp = leftFaceIndex;
		int bottomFaceIndexTmp = bottomFaceIndex;
		int rightFaceIndexTmp = rightFaceIndex;
		int topFaceIndexTmp = topFaceIndex;
		
		leftFaceIndex = topFaceIndexTmp;
		bottomFaceIndex = leftFaceIndexTmp;
		rightFaceIndex = bottomFaceIndexTmp;
		topFaceIndex = rightFaceIndexTmp;
		getFrontCubeFace().rotateCounterClockWise();
		getBackCubeFace().rotateClockWise();
		setChanged();
		notifyObservers("facesUpdated");
	}
	
	public void flip90DegreesRight() {
		int leftFaceIndexTmp = leftFaceIndex;
		int bottomFaceIndexTmp = bottomFaceIndex;
		int rightFaceIndexTmp = rightFaceIndex;
		int topFaceIndexTmp = topFaceIndex;
		
		leftFaceIndex = bottomFaceIndexTmp;
		bottomFaceIndex = rightFaceIndexTmp;
		rightFaceIndex = topFaceIndexTmp;
		topFaceIndex = leftFaceIndexTmp;
		getFrontCubeFace().rotateClockWise();
		getBackCubeFace().rotateCounterClockWise();
		setChanged();
		notifyObservers("facesUpdated");
	}
	

	public void turnF() {
		int[] redCol = cubeFaces[1].getCol(0).clone();
		reverseArray(redCol);

		int[] yellowRow = cubeFaces[5].getRow(0).clone();

		int[] orangeCol = cubeFaces[3].getCol(2).clone();
		reverseArray(orangeCol);

		int[] whiteRow = cubeFaces[4].getRow(2).clone();

		cubeFaces[5].setRow(0, redCol);
		cubeFaces[3].setCol(2, yellowRow);
		cubeFaces[4].setRow(2, orangeCol);
		cubeFaces[1].setCol(0, whiteRow);

		setChanged();
		notifyObservers("facesUpdated");
	}

	public void turnFI() {
		int[] redCol = cubeFaces[1].getCol(0).clone();

		int[] yellowRow = cubeFaces[5].getRow(0).clone();
		reverseArray(yellowRow);

		int[] orangeCol = cubeFaces[3].getCol(2).clone();

		int[] whiteRow = cubeFaces[4].getRow(2).clone();
		reverseArray(whiteRow);

		cubeFaces[5].setRow(0, orangeCol);
		cubeFaces[3].setCol(2, whiteRow);
		cubeFaces[4].setRow(2, redCol);
		cubeFaces[1].setCol(0, yellowRow);

		setChanged();
		notifyObservers("facesUpdated");
	}

	public void turnF2() {
		int[] redCol = cubeFaces[1].getCol(0).clone();
		reverseArray(redCol);

		int[] yellowRow = cubeFaces[5].getRow(0).clone();

		int[] orangeCol = cubeFaces[3].getCol(2).clone();
		reverseArray(orangeCol);

		int[] whiteRow = cubeFaces[4].getRow(2).clone();

		cubeFaces[5].setRow(0, redCol);
		cubeFaces[3].setCol(2, yellowRow);
		cubeFaces[4].setRow(2, orangeCol);
		cubeFaces[1].setCol(0, whiteRow);

		redCol = cubeFaces[1].getCol(0).clone();
		reverseArray(redCol);

		yellowRow = cubeFaces[5].getRow(0).clone();

		orangeCol = cubeFaces[3].getCol(2).clone();
		reverseArray(orangeCol);

		whiteRow = cubeFaces[4].getRow(2).clone();

		cubeFaces[5].setRow(0, redCol);
		cubeFaces[3].setCol(2, yellowRow);
		cubeFaces[4].setRow(2, orangeCol);
		cubeFaces[1].setCol(0, whiteRow);
		setChanged();
		notifyObservers("facesUpdated");
	}

	public void turnB() {
		int[] redCol = cubeFaces[1].getCol(2).clone();
		reverseArray(redCol);

		int[] yellowRow = cubeFaces[5].getRow(2).clone();

		int[] orangeCol = cubeFaces[3].getCol(0).clone();
		reverseArray(orangeCol);

		int[] whiteRow = cubeFaces[4].getRow(0).clone();

		cubeFaces[5].setRow(2, redCol);
		cubeFaces[3].setCol(0, yellowRow);
		cubeFaces[4].setRow(0, orangeCol);
		cubeFaces[1].setCol(2, whiteRow);

		setChanged();
		notifyObservers("facesUpdated");
	}

	public void turnBI() {
		int[] redCol = cubeFaces[1].getCol(2).clone();

		int[] yellowRow = cubeFaces[5].getRow(2).clone();
		reverseArray(yellowRow);

		int[] orangeCol = cubeFaces[3].getCol(0).clone();

		int[] whiteRow = cubeFaces[4].getRow(0).clone();
		reverseArray(whiteRow);

		cubeFaces[5].setRow(2, orangeCol);
		cubeFaces[3].setCol(0, whiteRow);
		cubeFaces[4].setRow(0, redCol);
		cubeFaces[1].setCol(2, yellowRow);

		setChanged();
		notifyObservers("facesUpdated");
	}

	public void turnB2() {
		int[] redCol = cubeFaces[1].getCol(2).clone();
		reverseArray(redCol);

		int[] yellowRow = cubeFaces[5].getRow(2).clone();

		int[] orangeCol = cubeFaces[3].getCol(0).clone();
		reverseArray(orangeCol);

		int[] whiteRow = cubeFaces[4].getRow(0).clone();

		cubeFaces[5].setRow(2, redCol);
		cubeFaces[3].setCol(0, yellowRow);
		cubeFaces[4].setRow(0, orangeCol);
		cubeFaces[1].setCol(2, whiteRow);

		redCol = cubeFaces[1].getCol(2).clone();
		reverseArray(redCol);

		yellowRow = cubeFaces[5].getRow(2).clone();

		orangeCol = cubeFaces[3].getCol(0).clone();
		reverseArray(orangeCol);

		whiteRow = cubeFaces[4].getRow(0).clone();

		cubeFaces[5].setRow(2, redCol);
		cubeFaces[3].setCol(0, yellowRow);
		cubeFaces[4].setRow(0, orangeCol);
		cubeFaces[1].setCol(2, whiteRow);

		setChanged();
		notifyObservers("facesUpdated");
	}

	public void turnL() {
		int[] greenCol = cubeFaces[0].getCol(0).clone();

		int[] yellowCol = cubeFaces[5].getCol(0).clone();
		
		int[] blueCol = cubeFaces[2].getCol(2).clone();

		int[] whiteCol = cubeFaces[4].getCol(0).clone();
		
		cubeFaces[5].setCol(0, greenCol);
		cubeFaces[2].setCol(2, yellowCol);
		cubeFaces[4].setCol(0, blueCol);
		cubeFaces[0].setCol(0, whiteCol);

		setChanged();
		notifyObservers("facesUpdated");
	}

	public void turnLI() {

	}

	public void turnL2() {

	}

	public void turnR() {

	}

	public void turnRI() {

	}

	public void turnR2() {

	}

	public void turnU() {

	}

	public void turnUI() {

	}

	public void turnU2() {

	}

	public void turnD() {

	}

	public void turnDI() {

	}

	public void turnD2() {

	}

	private static void reverseArray(int[] data) {
		for (int i = 0; i < data.length / 2; i++) {
			int tmp = data[i];
			data[i] = data[data.length - i - 1];
			data[data.length - i - 1] = tmp;
		}
	}

}
