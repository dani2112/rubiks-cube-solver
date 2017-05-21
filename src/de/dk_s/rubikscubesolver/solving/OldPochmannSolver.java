package de.dk_s.rubikscubesolver.solving;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;

import de.dk_s.rubikscubesolver.domain.Cube;
import de.dk_s.rubikscubesolver.domain.CubeFace;

/* Solves a cube according to this tutorial https://www.youtube.com/watch?v=idRv29MhQ74&t=1034s
 * Algorithms provided in the description*/
public class OldPochmannSolver {

	private Cube cube;

	private boolean isInitialized = false;

	private boolean edgesSolved = false;

	private boolean cornersSolved = false;

	private int cornerSolvingSteps = 0;

	/* Algorithms */
	private String tPerm = "R U R' U' R' F R2 U' R' U' R U R' F'";

	private String jPerm = "R U2 R' U' R U2 L' U R' U' L";

	private String lPerm = "R' U2 R U R' U2 L U' R U L'";

	private String yPerm = "R U' R' U' R U R' F' R U R' U' R' F R";

	private String rPerm = "L U2 L' U2 L F' L' U' L U L F L2 U";

	/* Data structures for holding pieces */
	private List<EdgePiece> edgePieces = new ArrayList<>();

	private List<CornerPiece> cornerPieces = new ArrayList<>();

	private static class EdgePiece {

		public String name = "";

		public int xIndex = 0;

		public int yIndex = 0;

		public int color1 = 0;
		public int color2 = 0;

		public String setupMove = "";

		public String undoSetupMove = "";

		public String perm = "";

		public boolean isSolved = false;

	}

	private static class CornerPiece {
		public String name = "";

		public int xIndex = 0;
		public int yIndex = 0;

		public int color1 = 0;
		public int color2 = 0;
		public int color3 = 0;

		public String setupMove = "";

		public String undoSetupMove = "";

		public String perm = "";

		public boolean isSolved = false;
	}

	public OldPochmannSolver(Cube cube) {
		this.cube = cube;
	}

	/*
	 * Sets cube orientation expecting green is front white is top and red is
	 * right
	 */
	public void setupCubeOrientation() {
		cube.rotateYAxis90Clockwise();
		cube.flip90DegreesLeft();
	}

	private void initializeSolver() {
		/* First Initialize Edge Pieces */
		try {
			File edgeFile = new File("solving/edgepieces.txt");
			Scanner sc;
			sc = new Scanner(edgeFile);
			while (sc.hasNextLine()) {
				String line = sc.nextLine();
				String[] lineSplit = line.split(";");
				EdgePiece edgePiece = new EdgePiece();
				edgePiece.name = lineSplit[0];
				edgePiece.setupMove = lineSplit[1].equals("none") ? "" : lineSplit[1];
				edgePiece.perm = lineSplit[2].equals("none") ? "" : lineSplit[2];
				edgePiece.undoSetupMove = lineSplit[3].equals("none") ? "" : lineSplit[3];
				edgePiece.xIndex = Integer.parseInt(lineSplit[4]);
				edgePiece.yIndex = Integer.parseInt(lineSplit[5]);
				edgePiece.color1 = getFaceColorFromNotationString(edgePiece.name.substring(0, 1));
				edgePiece.color2 = getFaceColorFromNotationString(edgePiece.name.substring(1, 2));
				edgePieces.add(edgePiece);
			}
			sc.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		/* Second Initialize Corner Pieces */
		try {
			File cornerFile = new File("solving/cornerpieces.txt");
			Scanner sc;
			sc = new Scanner(cornerFile);
			while (sc.hasNextLine()) {
				String line = sc.nextLine();
				String[] lineSplit = line.split(";");
				CornerPiece cornerPiece = new CornerPiece();
				cornerPiece.name = lineSplit[0];
				cornerPiece.setupMove = lineSplit[1].equals("none") ? "" : lineSplit[1];
				cornerPiece.perm = lineSplit[2].equals("none") ? "" : lineSplit[2];
				cornerPiece.undoSetupMove = lineSplit[3].equals("none") ? "" : lineSplit[3];
				cornerPiece.xIndex = Integer.parseInt(lineSplit[4]);
				cornerPiece.yIndex = Integer.parseInt(lineSplit[5]);
				cornerPiece.color1 = getFaceColorFromNotationString(cornerPiece.name.substring(0, 1));
				cornerPiece.color2 = getFaceColorFromNotationString(cornerPiece.name.substring(1, 2));
				cornerPiece.color3 = getFaceColorFromNotationString(cornerPiece.name.substring(2, 3));
				cornerPieces.add(cornerPiece);
			}
			sc.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

	}

	private int getFaceColorFromNotationString(String face) {
		int color = 0;
		switch (face) {
		case "U":
			color = cube.getTopCubeFace().getCubeFaceColor();
			break;
		case "F":
			color = cube.getFrontCubeFace().getCubeFaceColor();
			break;
		case "L":
			color = cube.getLeftCubeFace().getCubeFaceColor();
			break;
		case "B":
			color = cube.getBackCubeFace().getCubeFaceColor();
			break;
		case "R":
			color = cube.getRightCubeFace().getCubeFaceColor();
			break;
		case "D":
			color = cube.getBottomCubeFace().getCubeFaceColor();
			break;
		default:
			System.out.println("Error while getting color of edge pieces");
			color = 0;
		}
		return color;
	}

	private CubeFace getFaceFromNotationString(String face) {
		CubeFace cubeFace = null;
		switch (face) {
		case "U":
			cubeFace = cube.getTopCubeFace();
			break;
		case "F":
			cubeFace = cube.getFrontCubeFace();
			break;
		case "L":
			cubeFace = cube.getLeftCubeFace();
			break;
		case "B":
			cubeFace = cube.getBackCubeFace();
			break;
		case "R":
			cubeFace = cube.getRightCubeFace();
			break;
		case "D":
			cubeFace = cube.getBottomCubeFace();
			break;
		default:
			System.out.println("Error while getting cubeface");
			cubeFace = null;
		}
		return cubeFace;
	}

	public void getNextMove() {

	}

	public void executeNextMove() {
		if (!isInitialized) {
			System.out.println("Initialize");
			/* Would be necessary to match example */
			//setupCubeOrientation();

			/* Only for testing! Remove scramble after full test */
			cube.executeSequence("F' L' D L2 R2 D' U' B R2 U B' U L2 R F' D2 R' L' U' D R2 L D2 L' R F L' F2 L2 D2 R' F2 U B' U2 B' U2 D F U' D2 B' D2 B D2 L2 U' L' U2 F B2 D R' U' B F' L F R' B2 F L' F' B2 U' R U2 R2 D2 L' F' B2 D' U' L2 D' U' R' L2 U2 R B2 U F2 L B' L F' L R B' D' F' D' L2 R' D U R2 D R");

			initializeSolver();
			isInitialized = true;
			return;
		}

		/* Solve edges */
		if (!edgesSolved && cornersSolved) {
			EdgePiece edgePiece = getBufferEdgePiece();
			if (edgePiece == null) {
				for (EdgePiece e : edgePieces) {
					if (!e.isSolved) {
						edgePiece = e;
					}
				}
				if (edgePiece == null) {
					edgesSolved = true;
					return;
				}
			}
			displayMoves("Solving Edge Piece");
			displayMoves("Setup Move: " + edgePiece.setupMove);
			cube.executeSequence(edgePiece.setupMove);
			displayMoves("Apply " + edgePiece.perm + " permutation: " + getPermutationMovesAsString(edgePiece.perm));
			executePermutation(edgePiece.perm);
			displayMoves("Undo Setup Move: " + edgePiece.undoSetupMove);
			cube.executeSequence(edgePiece.undoSetupMove);
			edgePiece.isSolved = true;

			/* Also mark corresponding pieces */
			String reverseEdgeName = new StringBuilder(edgePiece.name).reverse().toString();
			for (EdgePiece e : edgePieces) {
				if (e.name.equals(reverseEdgeName)) {
					e.isSolved = true;
				}
			}

			/* Check if edge solving is done */
			List<EdgePiece> flippedPieces = new ArrayList<>();
			edgesSolved = true;
			for (EdgePiece piece : edgePieces) {
				String reverseName = new StringBuilder(piece.name).reverse().toString();
				EdgePiece matchingPiece = null;
				for (EdgePiece mPiece : edgePieces) {
					if (reverseName.equals(mPiece.name)) {
						matchingPiece = mPiece;
					}

				}
				int currentColor1 = getFaceFromNotationString(piece.name.substring(0, 1)).getSubCubeColor(piece.xIndex,
						piece.yIndex);
				int currentColor2 = getFaceFromNotationString(matchingPiece.name.substring(0, 1))
						.getSubCubeColor(matchingPiece.xIndex, matchingPiece.yIndex);
				boolean solved = false;
				if (currentColor1 == piece.color1 && currentColor2 == piece.color2) {
					solved = true;
				}
				if (currentColor1 == piece.color2 && currentColor2 == piece.color1) {
					flippedPieces.add(piece);
					solved = true;
				}
				if (solved == false) {
					edgesSolved = false;
					break;
				}
			}
			/* Check which edges are flipped and correct them */
			if (edgesSolved) {
				displayMoves("Edges are now solved. Correction of flipped edges:");
				for (EdgePiece flippedPiece : flippedPieces) {
					displayMoves("Setup Move: " + edgePiece.setupMove);
					cube.executeSequence(flippedPiece.setupMove);
					displayMoves("Apply " + edgePiece.perm + " permutation: " + getPermutationMovesAsString(edgePiece.perm));
					executePermutation(flippedPiece.perm);
					displayMoves("Undo Setup Move: " + edgePiece.undoSetupMove);
					cube.executeSequence(flippedPiece.undoSetupMove);
				}
				return;
			}
		}

		if (!cornersSolved && !edgesSolved) {
			this.cornerSolvingSteps++;
			CornerPiece cornerPiece = getBufferCornerPiece();
			if (cornerPiece == null) {
				for (CornerPiece c : cornerPieces) {
					if (!c.isSolved) {
						cornerPiece = c;
					}
				}
				if (cornerPiece == null) {
					cornersSolved = true;
					return;
				}
			}
			displayMoves("Solving corner piece");
			displayMoves("Setup Move: " + cornerPiece.setupMove);
			cube.executeSequence(cornerPiece.setupMove);
			displayMoves("Apply " + cornerPiece.perm + " permutation: " + getPermutationMovesAsString(cornerPiece.perm));
			executePermutation(cornerPiece.perm);
			displayMoves("Undo Setup Move: " + cornerPiece.undoSetupMove);
			cube.executeSequence(cornerPiece.undoSetupMove);
			cornerPiece.isSolved = true;

			/* Also mark corressponding pieces */
			for (CornerPiece piece : cornerPieces) {
				if ((piece.color1 == cornerPiece.color1 && piece.color2 == cornerPiece.color2
						&& piece.color3 == cornerPiece.color3)
						|| (piece.color1 == cornerPiece.color1 && piece.color2 == cornerPiece.color3
								&& piece.color3 == cornerPiece.color2)) {
					piece.isSolved = true;
				}
			}

			/* Check if corner solving is done */
			List<CornerPiece> twistedCorners = new ArrayList<>();
			cornersSolved = true;
			for (CornerPiece piece : cornerPieces) {
				List<String> namePermutations = new ArrayList<>();
				generatePermutations("", piece.name, namePermutations);
				List<CornerPiece> matchingPieces = new ArrayList<>();
				for (CornerPiece matchingPiece : cornerPieces) {
					for (String namePermutation : namePermutations) {
						if (namePermutation.equals(matchingPiece.name) && !namePermutation.equals(piece.name)) {
							matchingPieces.add(matchingPiece);
						}
					}
				}
				int currentColor1 = getFaceFromNotationString(piece.name.substring(0, 1)).getSubCubeColor(piece.xIndex,
						piece.yIndex);
				int currentColor2 = getFaceFromNotationString(matchingPieces.get(0).name.substring(0, 1))
						.getSubCubeColor(matchingPieces.get(0).xIndex, matchingPieces.get(0).yIndex);
				int currentColor3 = getFaceFromNotationString(matchingPieces.get(1).name.substring(0, 1))
						.getSubCubeColor(matchingPieces.get(1).xIndex, matchingPieces.get(1).yIndex);
				
//				HashSet<Integer> shouldSet = new HashSet<>();
//				shouldSet.add(piece.color1);
//				shouldSet.add(piece.color2);
//				shouldSet.add(piece.color3);
//				HashSet<Integer> isSet = new HashSet<>();
//				isSet.add(currentColor1);
//				isSet.add(currentColor2);
//				isSet.add(currentColor3);
				//Normally condition should be !isSet.containsAll(shouldSet)
				if (piece.color1 != currentColor1) {
					cornersSolved = false;
				} else {
					/* Add Code to detect twisted corners here
					 * is not necessary but useful! */
					if(piece.color1 != currentColor1) {
						twistedCorners.add(piece);
					}
				}
			}
			if (cornersSolved) {
				displayMoves("Corners are now solved.");
				/* Correct twisted corners */
				//TODO
				
				/* If odd number of corners apply parity fix */
				if (cornerSolvingSteps % 2 != 0) {
					displayMoves("Needed odd number of moves. Parity fix necessary.");
					displayMoves("Rotate cube around Y-axis counterclockwise: y'");
					cube.rotateYAxis90CounterClockwise();
					displayMoves("Apply R-perm: " + rPerm);
					cube.executeSequence(rPerm);
					displayMoves("Rotate cube around Y-axis clockwise: y");
					cube.rotateYAxis90Clockwise();
				}
			}

		}

	}

	private static void generatePermutations(String prefix, String str, List<String> permutations) {
		int n = str.length();
		if (n == 0)
			permutations.add(prefix);
		else {
			for (int i = 0; i < n; i++)
				generatePermutations(prefix + str.charAt(i), str.substring(0, i) + str.substring(i + 1, n),
						permutations);
		}
	}

	private String getPermutationMovesAsString(String perm) {
		perm = perm.toLowerCase();
		String permString = "";
		switch (perm) {
		case "t-perm":
			permString = tPerm;
			break;
		case "j-perm":
			permString = jPerm;
			break;
		case "l-perm":
			permString = lPerm;
			break;
		case "y-perm":
			permString = yPerm;
			break;
		case "r-perm":
			permString = rPerm;
			break;
		default:
			permString = "Permutation unknown!";
		}
		return permString;
	}
	
	private void executePermutation(String perm) {
		perm = perm.toLowerCase();
		switch (perm) {
		case "t-perm":
			cube.executeSequence(tPerm);
			break;
		case "j-perm":
			cube.executeSequence(jPerm);
			break;
		case "l-perm":
			cube.executeSequence(lPerm);
			break;
		case "y-perm":
			cube.executeSequence(yPerm);
			break;
		case "r-perm":
			cube.executeSequence(rPerm);
			break;
		default:
			System.out.println("Permutation unknown!");
		}
	}

	private EdgePiece getBufferEdgePiece() {
		int bufferColor1 = cube.getTopCubeFace().getSubCubeColor(2, 1);
		int bufferColor2 = cube.getRightCubeFace().getSubCubeColor(1, 0);
		EdgePiece edgePiece = null;
		for (EdgePiece piece : edgePieces) {
			if (piece.color1 == bufferColor1 && piece.color2 == bufferColor2) {
				edgePiece = piece;
				break;
			}
		}
		return edgePiece;
	}

	private CornerPiece getBufferCornerPiece() {
		int bufferColor1 = cube.getTopCubeFace().getSubCubeColor(0, 0);
		int bufferColor2 = cube.getLeftCubeFace().getSubCubeColor(0, 0);
		int bufferColor3 = cube.getBackCubeFace().getSubCubeColor(2, 0);
		CornerPiece cornerPiece = null;
		for (CornerPiece piece : cornerPieces) {
			if ((piece.color1 == bufferColor1 && piece.color2 == bufferColor2 && piece.color3 == bufferColor3)
					|| (piece.color1 == bufferColor1 && piece.color2 == bufferColor3 && piece.color3 == bufferColor2)) {
				cornerPiece = piece;
				break;
			}
		}
		return cornerPiece;
	}
	
	private void displayMoves(String moves) {
		System.out.println(moves);
	}

}
