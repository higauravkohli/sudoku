package com.gk.sudoku.algo;

import com.gk.sudoku.model.Sudoku;

public interface Strategy {

	boolean hit(Sudoku sudoku, int[][][] possibilities);
	
}
