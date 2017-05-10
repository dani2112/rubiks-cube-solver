package de.dk_s.rubikscubesolver.domain;

public class Cube {

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
		cubeFaces[index] = cubeFace;
	}
	
	
}
