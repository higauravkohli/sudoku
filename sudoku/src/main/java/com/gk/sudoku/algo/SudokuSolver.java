package com.gk.sudoku.algo;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.stereotype.Component;

import com.gk.sudoku.model.Sudoku;

@Component
public class SudokuSolver{

	public Sudoku solve(Sudoku sudoku) {
		boolean matched = false;
		do {
			matched = false;
			
			int[][][] possibilities = possibilities(sudoku);
			if(confirmedHit(sudoku, possibilities))
				matched = true;
			
		}while(matched);
		
		return sudoku;
	}

	private int[][][] possibilities(Sudoku sudoku){
		int[][] puzzle = sudoku.getPuzzle();
		int[][][] possibilities = new int[9][9][9];
		for(int x=0; x<9; x++) {
			for(int y=0; y<9; y++) {
				if(puzzle[x][y] == 0) {
					List<Integer> possibilitySet = new ArrayList<>();
					
					List<Integer> rows = rowPossibilities(sudoku, y);
					List<Integer> cols = colPossibilities(sudoku, x);
					List<Integer> boxes = boxPossibilities(sudoku, x, y);
					
					for(Integer val : rows) {
						if(cols.contains(val) && boxes.contains(val))
							possibilitySet.add(val);
					}
					
					possibilities[x][y] = listToArray(possibilitySet);
				}
			}
		}
		return possibilities;
	}
	
	private boolean confirmedHit(Sudoku sudoku, int[][][] possibilities) {
		return singleHit(sudoku, possibilities);
	}

	private boolean singleHit(Sudoku sudoku, int[][][] possibilities) {
		boolean hit = false;
		for(int x=0; x<9; x++) {
			for(int y=0; y<9; y++) {
				if(possibilities[x][y].length == 1) {
					sudoku.set(x, y, possibilities[x][y][0]);
					hit = true;
				}
			}
		}
		return hit;
	}
	
	private List<Integer> rowPossibilities(Sudoku sudoku, int y){
		List<Integer> possibilities = new ArrayList<>();
		for(int i=0; i<9; i++) {
			boolean found = false;
			for(int x=0; x<9; x++) {
				if(sudoku.get(x, y) == (i+1)) {
					found = true;
					break;
				}
			}
			if(!found)
				possibilities.add(i+1);
		}
		
		return possibilities;
	}

	private List<Integer> colPossibilities(Sudoku sudoku, int x){
		List<Integer> possibilities = new ArrayList<>();
		for(int i=0; i<9; i++) {
			boolean found = false;
			for(int y=0; y<9; y++) {
				if(sudoku.get(x, y) == (i+1)) {
					found = true;
					break;
				}
			}
			if(!found)
				possibilities.add(i+1);
		}
		
		return possibilities;
	}
	
	private List<Integer> boxPossibilities(Sudoku sudoku, int x, int y){
		List<Integer> possibilities = new ArrayList<>();
		for(int i=0; i<9; i++) {
			boolean found = false;
			for(int x1=0; x1<9; x1++) {
				if(x1/3 == x/3) {
					for(int y1=0; y1<9; y1++) {
						if(y1/3 == y/3) {
							if(sudoku.get(x1, y1) == (i+1)) {
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

	private int[] listToArray(Collection<Integer> values) {
		int[] valuesArr = new int[values.size()];
		int i=0;
		for(Integer vl : values)
			valuesArr[i++] = vl;
		
		return valuesArr;
	}
	
}
