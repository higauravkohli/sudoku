package com.gk.sudoku.model;

public class Sudoku {

	private int[][] puzzle = new int[9][9];
	
	public Sudoku(int[][] puzzle) {
		this.puzzle = puzzle;
	}
	
	public int[][] getPuzzle() {
		return puzzle;
	}
	
	public int get(int x, int y) {
		return puzzle[x][y];
	}
	
	public void set(int x, int y, int val) {
		if(!fit(x, y, val))
			throw new RuntimeException("Doesnt fit" + x + " " + y + " " + val);
		puzzle[x][y] = val;
	}
	
	public boolean fit(int x, int y, int val) {
		return puzzle[x][y] == 0 && rowFit(x, val) && colFit(y, val);
	}
	
	private boolean rowFit(int x, int val) {
		for(int y=0; y<9; y++)
			if(puzzle[x][y] == val)
				return false;
		return true;
	}

	private boolean colFit(int y, int val) {
		for(int x=0; x<9; x++)
			if(puzzle[x][y] == val)
				return false;
		return true;
	}
}
