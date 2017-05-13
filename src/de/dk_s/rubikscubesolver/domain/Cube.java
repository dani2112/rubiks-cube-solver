package de.dk_s.rubikscubesolver.domain;

import java.util.Observable;

public class Cube extends Observable {

	private CubeFace[] cubeFaces = new CubeFace[6];
	
	public Cube() {
		initializeCubeFaces();
	}
	
	private void initializeCubeFaces() {
		for(int i = 0; i < cubeFaces.length; i++) {
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
		int[] redRow = cubeFaces[1].getCol(0);
		cubeFaces[5].setRow(1, redRow);
	}
	
	public void turnFI() {
		
	}
	
	public void turnF2() {
		
	}
	
	public void turnB() {
		
	}
	
	public void turnBI() {
		
	}
	
	public void turnB2() {
		
	}
	
	public void turnL() {
		
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
	
	
	
	
}
