package de.dk_s.rubikscubesolver.domain;

public class CubeFace {

	private int cubeFaceId = -1;
	
	private int[][] subCubes;
	
	public CubeFace() {
		subCubes = new int[3][3];
	}
	
	public CubeFace(int initColor) {
		subCubes = new int[3][3];
		for(int i = 0; i < 3; i++) {
			for(int j = 0; j < 3; j++) {
				subCubes[i][j] = initColor;
			}
		}
		subCubes[0][0] = 5;
	}
	
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
	
	public int[] getCol(int x) {
		int[] col = new int[3];
		for(int i = 0; i < 3; i++) {
			col[i] = subCubes[x][i];
		}
		return col;
	}
	
	public void setCol(int x, int[] col) {
		for(int i = 0; i < 3; i++) {
			subCubes[x][i] = col[i];
		}
	}
	
	public int[] getRow(int y) {
		int[] row = new int[3];
		for(int i = 0; i < 3; i++) {
			row[i] = subCubes[i][y];
		}
		return row;
	}
	
	public void setRow(int y, int[] row) {
		for(int i = 0; i < 3; i++) {
			subCubes[i][y] = row[i];
		}
	}
	
	public void rotateClockWise() {
		subCubes = rotateClockwise(subCubes);
	}
	
	public void rotateCounterClockWise() {
		subCubes = rotateClockwise(subCubes);
		subCubes = rotateClockwise(subCubes);
		subCubes = rotateClockwise(subCubes);
	}
	
	static int[][] rotateClockwise(int[][] mat) {
	    final int m = mat.length;
	    final int n = mat[0].length;
	    int[][] ret = new int[n][m];
	    for (int r = 0; r < m; r++) {
	        for (int c = 0; c < n; c++) {
	            ret[c][m-1-r] = mat[r][c];
	        }
	    }
	    return ret;
	}
	
}
