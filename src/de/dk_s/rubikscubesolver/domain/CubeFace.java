package de.dk_s.rubikscubesolver.domain;

public class CubeFace {

	private int cubeFaceId = -1;
	
	private int[][] subCubes = new int[3][3];
	
	public void setCubeFaceId(int id) {
		this.cubeFaceId = id;
	}
	
	public int getSubCubeColor(int x, int y) {
		return subCubes[x][y];
	}
	
	// Origin is left upper corner
	public void setSubCubeColor(int x, int y, int color) {
		subCubes[x][y] = color;
	}
	
}
