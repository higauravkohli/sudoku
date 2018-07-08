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
		puzzle[x][y] = val;
	}
}
