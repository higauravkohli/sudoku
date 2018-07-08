package com.gk.sudoku.algo;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Component;

import com.gk.sudoku.model.Sudoku;

@Component
public class SudokuSolver{

	public Sudoku solve(Sudoku sudoku) {
		return sudoku;
	}

	private void confirmedHit(Sudoku sudoku) {
		int[] next = next(sudoku);
		
		if(next != null) {
			for(int i=0; i<9; i++) {
				rowPossibilities(sudoku, next, i+1);
			}
		}
	}
	
	private int[] rowPossibilities(Sudoku sudoku, int[] xy, int val){
		List<Integer> possibilities = new ArrayList<>();
		for(int y=0; y<9; y++) {
			if(y==0)
				possibilities.add(y);
			if(sudoku.get(xy[0], y) == val)
				return null;
		}

		return listToArray(possibilities);
	}

	private int[] listToArray(List<Integer> values) {
		int[] valuesArr = new int[values.size()];
		for(int i=0; i<values.size(); i++)
			valuesArr[i] = values.get(i);
		
		return valuesArr;
	}
	
	private int[] next(Sudoku sudoku) {
		int[][] puzzle = sudoku.getPuzzle();
		for(int x=0; x<9; x++) {
			for(int y=0; y<9; y++) {
				if(puzzle[x][y] == 0) {
					return new int[] {x,y};
				}
			}
		}
		return null;
	}
}
