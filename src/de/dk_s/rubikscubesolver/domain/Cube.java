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
	
	
	
	
}
