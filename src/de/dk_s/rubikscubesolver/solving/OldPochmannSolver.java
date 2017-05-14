package de.dk_s.rubikscubesolver.solving;

import de.dk_s.rubikscubesolver.domain.Cube;

public class OldPochmannSolver {

	private Cube cube;
	
	public OldPochmannSolver(Cube cube) {
		this.cube = cube;
	}
	
	public void getNextMove() {
		
	}
	
	public void executeNextMove() {
		cube.turnFI();
	}
	
}
