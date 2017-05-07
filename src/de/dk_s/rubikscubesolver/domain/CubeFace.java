package de.dk_s.rubikscubesolver.domain;

public class CubeFace {

	private int cubeFaceId = -1;
	
	private int[][] subCubes = new int[3][3];
	
	public void setCubeFaceId(int id) {
		this.cubeFaceId = id;
	}
	
	// Origin is left upper corner
	public void setSubCube(int x, int y, int color) {
		subCubes[x][y] = color;
	}
	
}
