package de.dk_s.rubikscubesolver.domain;

import java.util.Observable;
import java.util.Scanner;

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
	
	public void executeSequence(String sequence) {
		Scanner sc = new Scanner(sequence);
		while(sc.hasNext()) {
			String currentToken = sc.next();
			switch(currentToken) {
			case "F":
				turnF();
				break;
			case "F'":
				turnFI();
				break;
			case "F2":
				turnF2();
				break;
			case "Fw":
				turnFw();
				break;
			case "B":
				turnB();
				break;
			case "B'":
				turnBI();
				break;
			case "B2":
				turnB2();
				break;
			case "Bw":
				turnBw();
				break;
			case "R":
				turnR();
				break;
			case "R'":
				turnRI();
				break;
			case "R2":
				turnR2();
				break;
			case "Rw":
				turnRw();
				break;
			case "L":
				turnL();
				break;
			case "L'":
				turnLI();
				break;
			case "L2":
				turnL2();
				break;
			case "Lw":
				turnLw();
				break;
			case "U":
				turnU();
				break;
			case "U'":
				turnUI();
				break;
			case "U2":
				turnU2();
				break;
			case "Uw":
				turnUw();
				break;
			case "D":
				turnD();
				break;
			case "D'":
				turnDI();
				break;
			case "D2":
				turnD2();
				break;
			case "Dw":
				turnDw();
				break;
			}
		}
		
		sc.close();
	};
	
	
	public void flip90DegreesBackward() {
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
		
		getBackCubeFace().rotateClockWise();
		getBackCubeFace().rotateClockWise();
		
		getBottomCubeFace().rotateClockWise();
		getBottomCubeFace().rotateClockWise();
		
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
	
		getBackCubeFace().rotateClockWise();
		getBackCubeFace().rotateClockWise();
		
		getTopCubeFace().rotateClockWise();
		getTopCubeFace().rotateClockWise();
		
		
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
		
		getTopCubeFace().rotateCounterClockWise();
		getRightCubeFace().rotateCounterClockWise();
		getBottomCubeFace().rotateCounterClockWise();
		getLeftCubeFace().rotateCounterClockWise();
		
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
		
		getTopCubeFace().rotateClockWise();
		getRightCubeFace().rotateClockWise();
		getBottomCubeFace().rotateClockWise();
		getLeftCubeFace().rotateClockWise();
		setChanged();
		notifyObservers("facesUpdated");
	}
	
	public void rotateYAxis90Clockwise() {
		int frontFaceIndexTmp = frontFaceIndex;
		int leftFaceIndexTmp = leftFaceIndex;
		int backFaceIndexTmp = backFaceIndex;
		int rightFaceIndexTmp = rightFaceIndex;
		
		frontFaceIndex = rightFaceIndexTmp;
		leftFaceIndex= frontFaceIndexTmp;
		backFaceIndex = leftFaceIndexTmp;
		rightFaceIndex = backFaceIndexTmp;
		
		getTopCubeFace().rotateClockWise();
		getBottomCubeFace().rotateCounterClockWise();
		
		setChanged();
		notifyObservers("facesUpdated");
	}
	
	public void rotateYAxis90CounterClockwise() {
		int frontFaceIndexTmp = frontFaceIndex;
		int leftFaceIndexTmp = leftFaceIndex;
		int backFaceIndexTmp = backFaceIndex;
		int rightFaceIndexTmp = rightFaceIndex;
		
		frontFaceIndex = leftFaceIndexTmp;
		leftFaceIndex= backFaceIndexTmp;
		backFaceIndex = rightFaceIndexTmp;
		rightFaceIndex = frontFaceIndexTmp;
		
		getTopCubeFace().rotateCounterClockWise();
		getBottomCubeFace().rotateClockWise();
		
		setChanged();
		notifyObservers("facesUpdated");
	}
	

	public void turnF() {
		int[] rightCol = getRightCubeFace().getCol(0).clone();
		reverseArray(rightCol);

		int[] bottomRow = getBottomCubeFace().getRow(0).clone();

		int[] leftCol = getLeftCubeFace().getCol(2).clone();
		reverseArray(leftCol);

		int[] topRow = getTopCubeFace().getRow(2).clone();

		getBottomCubeFace().setRow(0, rightCol);
		getLeftCubeFace().setCol(2, bottomRow);
		getTopCubeFace().setRow(2, leftCol);
		getRightCubeFace().setCol(0, topRow);
		
		getFrontCubeFace().rotateClockWise();

		setChanged();
		notifyObservers("facesUpdated");
	}

	public void turnFI() {
		
		int[] rightCol = getRightCubeFace().getCol(0).clone();

		int[] bottomRow = getBottomCubeFace().getRow(0).clone();
		reverseArray(bottomRow);
		
		int[] leftCol = getLeftCubeFace().getCol(2).clone();

		int[] topRow = getTopCubeFace().getRow(2).clone();
		reverseArray(topRow);

		getBottomCubeFace().setRow(0, leftCol);
		getLeftCubeFace().setCol(2, topRow);
		getTopCubeFace().setRow(2, rightCol);
		getRightCubeFace().setCol(0, bottomRow);
		
		getFrontCubeFace().rotateCounterClockWise();

		setChanged();
		notifyObservers("facesUpdated");
	}

	public void turnF2() {
		int[] rightCol = getRightCubeFace().getCol(0).clone();
		reverseArray(rightCol);

		int[] bottomRow = getBottomCubeFace().getRow(0).clone();

		int[] leftCol = getLeftCubeFace().getCol(2).clone();
		reverseArray(leftCol);

		int[] topRow = getTopCubeFace().getRow(2).clone();

		getBottomCubeFace().setRow(0, rightCol);
		getLeftCubeFace().setCol(2, bottomRow);
		getTopCubeFace().setRow(2, leftCol);
		getRightCubeFace().setCol(0, topRow);
		
		rightCol = getRightCubeFace().getCol(0).clone();
		reverseArray(rightCol);

		bottomRow = getBottomCubeFace().getRow(0).clone();

		leftCol = getLeftCubeFace().getCol(2).clone();
		reverseArray(leftCol);

		topRow = getTopCubeFace().getRow(2).clone();

		getBottomCubeFace().setRow(0, rightCol);
		getLeftCubeFace().setCol(2, bottomRow);
		getTopCubeFace().setRow(2, leftCol);
		getRightCubeFace().setCol(0, topRow);
		
		getFrontCubeFace().rotateClockWise();
		getFrontCubeFace().rotateClockWise();
		
		setChanged();
		notifyObservers("facesUpdated");
	}
	
	public void turnFw() {
		turnB();
		this.flip90DegreesRight();
	}

	public void turnB() {
		flip90DegreesForward();
		flip90DegreesForward();
		
		turnF();
		
		flip90DegreesBackward();
		flip90DegreesBackward();
		

		setChanged();
		notifyObservers("facesUpdated");
	}

	public void turnBI() {
		flip90DegreesForward();
		flip90DegreesForward();
		
		turnFI();
		
		flip90DegreesBackward();
		flip90DegreesBackward();

		setChanged();
		notifyObservers("facesUpdated");
	}

	public void turnB2() {
		
		flip90DegreesForward();
		flip90DegreesForward();
		
		turnF2();
		
		flip90DegreesBackward();
		flip90DegreesBackward();
		
		setChanged();
		notifyObservers("facesUpdated");
	}
	
	public void turnBw() {
		turnF();
		flip90DegreesLeft();
	}

	public void turnL() {
		rotateYAxis90CounterClockwise();
		
		turnF();
		
		rotateYAxis90Clockwise();
		

		setChanged();
		notifyObservers("facesUpdated");
	}

	public void turnLI() {
		rotateYAxis90CounterClockwise();
		
		turnFI();
		
		rotateYAxis90Clockwise();
		
		
	}

	public void turnL2() {
		rotateYAxis90CounterClockwise();
		
		turnF2();
		
		rotateYAxis90Clockwise();
	}
	
	public void turnLw() {
		turnR();
		flip90DegreesForward();
	}

	public void turnR() {
		rotateYAxis90Clockwise();
		
		turnF();
		
		rotateYAxis90CounterClockwise();
		
		
	}

	public void turnRI() {
		rotateYAxis90Clockwise();
		
		turnFI();
		
		rotateYAxis90CounterClockwise();
	}

	public void turnR2() {
		rotateYAxis90Clockwise();
		
		turnF2();
		
		rotateYAxis90CounterClockwise();
	}

	public void turnRw() {
		turnL();
		flip90DegreesBackward();
	}
	
	public void turnU() {
		flip90DegreesForward();
		
		turnF();
		
		flip90DegreesBackward();
	}

	public void turnUI() {
		flip90DegreesForward();
		
		turnFI();
		
		flip90DegreesBackward();
	}

	public void turnU2() {
		flip90DegreesForward();
		
		turnF2();
		
		flip90DegreesBackward();
	}
	
	public void turnUw() {
		turnD();
		this.rotateYAxis90Clockwise();
	}

	public void turnD() {
		flip90DegreesBackward();
		
		turnF();
		
		flip90DegreesForward();
	}

	public void turnDI() {
		flip90DegreesBackward();
		
		turnFI();
		
		flip90DegreesForward();
	}

	public void turnD2() {
		flip90DegreesBackward();
		
		turnF2();
		
		flip90DegreesForward();
	}
	
	public void turnDw() {
		turnU();
		rotateYAxis90CounterClockwise();
	}

	private static void reverseArray(int[] data) {
		for (int i = 0; i < data.length / 2; i++) {
			int tmp = data[i];
			data[i] = data[data.length - i - 1];
			data[data.length - i - 1] = tmp;
		}
	}

}
