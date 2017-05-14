package de.dk_s.rubikscubesolver.domain;

import java.util.Observable;

public class Cube extends Observable {

	private CubeFace[] cubeFaces = new CubeFace[6];

	public Cube() {
		initializeCubeFaces();
	}

	private void initializeCubeFaces() {
		for (int i = 0; i < cubeFaces.length; i++) {
			cubeFaces[i] = new CubeFace();
		}
	}

	public CubeFace getCubeFace(int index) {
		return cubeFaces[index];
	}

	public void setCubeFace(int index, CubeFace cubeFace) {
		setChanged();
		notifyObservers("facesUpdated");
		cubeFaces[index] = cubeFace;
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
