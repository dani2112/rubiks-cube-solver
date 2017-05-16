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
		
		cube.executeSequence("B' U' R B F U' L' F D B L2 D2 B2 U2 L' R B D2 L' U' B R F' L2 B2 R' B2 U2 L' F2 U' B' L2 D L' U2 R' D2 F D' U2 B R2 B L2 B' L2 B' R2 D2 R' U' R2 U D2 F' D2 B' L2 R2 U' D B R F U R F' B2 L2 B R F2 R2 D' L R2 F B2 D F' B D' F U2 B L U F L R' B R' B2 L2 F2 U B2 F R'");
	}
	
}
