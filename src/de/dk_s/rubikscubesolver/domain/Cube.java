package de.dk_s.rubikscubesolver.domain;

public class Cube {

	private CubeFace[] cubeFaces = new CubeFace[6];
	
	public Cube() {
		
	}
	
	public CubeFace getCubeFace(int index) {
		return cubeFaces[index];
	}
	
	public void setCubeFace(int index, CubeFace cubeFace) {
		cubeFaces[index] = cubeFace;
	}
	
	
}
