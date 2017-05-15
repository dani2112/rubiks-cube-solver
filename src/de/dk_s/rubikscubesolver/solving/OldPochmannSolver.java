package de.dk_s.rubikscubesolver.solving;

import de.dk_s.rubikscubesolver.domain.Cube;

public class OldPochmannSolver {

	private Cube cube;
	
	private int currentStep = 0;
	
	public OldPochmannSolver(Cube cube) {
		this.cube = cube;
	}
	
	/* Implements http://www.chessandpoker.com/rubiks-cube-solution.html */
	public void getNextMove() {
		
	}
	
	public void executeNextMove() {
		cube.turnFI();
	}
	
}
