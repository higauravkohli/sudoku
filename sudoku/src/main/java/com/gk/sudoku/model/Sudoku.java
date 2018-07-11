package com.gk.sudoku.model;

import java.util.ArrayList;
import java.util.List;

import com.gk.sudoku.Util;

public class Sudoku {

	private Util util = new Util();

	private int[][] puzzle = new int[9][9];
	private int[][] unfit = new int[9][9];
	
	public Sudoku(int[][] puzzle) {
		this.puzzle = puzzle;
	}
	
	public Sudoku(Sudoku sudoku) {
		for(int x=0; x<9; x++) {
			for(int y=0;y<9;y++) {
				puzzle[x][y] = sudoku.puzzle[x][y];
				unfit[x][y] = sudoku.unfit[x][y];
			}
		}
	}
	
	public void unfit(int x, int y, int val) {
		unfit[x][y] = val;
	}
	
	public int[][] getPuzzle() {
		return puzzle;
	}

	public void setPuzzle(int[][] puzzle) {
		this.puzzle = puzzle;
	}

	public int get(int x, int y) {
		return puzzle[x][y];
	}
	
	public void set(int x, int y, int val) {
		if(!fit(x, y, val))
			throw new RuntimeException("Doesnt fit" + x + " " + y + " " + val);
		puzzle[x][y] = val;
	}
	
	private boolean fit(int x, int y, int val) {
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
	
	public boolean solved() {
		for(int x=0; x<9; x++) {
			for(int y=0; y<9; y++) {
				if(puzzle[x][y] == 0)
					return false;
			}
		}
		return true;
	}
	
	public int[][][] possibilities(){
		int[][][] possibilities = new int[9][9][9];
		for(int x=0; x<9; x++) {
			for(int y=0; y<9; y++) {
				if(puzzle[x][y] == 0 && unfit[x][y] == 0) {
					List<Integer> possibilitySet = new ArrayList<>();
					
					List<Integer> rows = rowPossibilities(x);
					List<Integer> cols = colPossibilities(y);
					List<Integer> boxes = boxPossibilities(x, y);
					
					for(Integer val : rows) {
						if(cols.contains(val) && boxes.contains(val))
							possibilitySet.add(val);
					}
					
					possibilities[x][y] = util.listToArray(possibilitySet);
				}
				else
					possibilities[x][y] = null;
			}
		}
		return possibilities;
	}
	
	@Override
	public String toString() {
		StringBuilder out = new StringBuilder();
		for(int x=0; x<9; x++) {
			out.append("\n");
			if(x > 0 && x%3 == 0)
				out.append("---+---+---\n");

			for(int y=0; y<9; y++){
				if(y > 0 && y%3 == 0)
					out.append("|");
				out.append(puzzle[x][y]);
			}
		}
		return out.toString();
	}
	
	private List<Integer> rowPossibilities(int x){
		List<Integer> possibilities = new ArrayList<>();
		for(int i=0; i<9; i++) {
			boolean found = false;
			for(int y=0; y<9; y++) {
				if(puzzle[x][y] == (i+1)) {
					found = true;
					break;
				}
			}
			if(!found)
				possibilities.add(i+1);
		}
		
		return possibilities;
	}

	private List<Integer> colPossibilities(int y){
		List<Integer> possibilities = new ArrayList<>();
		for(int i=0; i<9; i++) {
			boolean found = false;
			for(int x=0; x<9; x++) {
				if(puzzle[x][y] == (i+1)) {
					found = true;
					break;
				}
			}
			if(!found)
				possibilities.add(i+1);
		}
		
		return possibilities;
	}
	
	private List<Integer> boxPossibilities(int x, int y){
		List<Integer> possibilities = new ArrayList<>();
		for(int i=0; i<9; i++) {
			boolean found = false;
			for(int x1=0; x1<9; x1++) {
				if(x1/3 == x/3) {
					for(int y1=0; y1<9; y1++) {
						if(y1/3 == y/3) {
							if(puzzle[x1][y1] == (i+1)) {
								found = true;
								break;
							}
						}
					}
				}
			}
			if(!found)
				possibilities.add(i+1);
		}
		
		return possibilities;
	}
}
