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
	
	public void F() {
		
	}
	
	public void FI() {
		
	}
	
	public void F2() {
		
	}
	
	public void B() {
		
	}
	
	public void BI() {
		
	}
	
	public void B2() {
		
	}
	
	public void L() {
		
	}
	
	public void LI() {
		
	}
	
	public void L2() {
		
	}
	
	public void R() {
		
	}
	
	public void RI() {
		
	}
	
	public void R2() {
		
	}
	
	public void U() {
		
	}
	
	public void UI() {
		
	}
	
	public void U2() {
		
	}
	
	public void D() {
		
	}
	
	public void DI() {
		
	}
	
	public void D2() {
		
	}
	
	
	
	
}
