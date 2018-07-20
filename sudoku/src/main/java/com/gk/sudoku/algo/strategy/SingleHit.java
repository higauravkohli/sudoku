package com.gk.sudoku.algo.strategy;

import com.gk.sudoku.algo.Strategy;
import com.gk.sudoku.model.Sudoku;

public class SingleHit implements Strategy{

	@Override
	public boolean hit(Sudoku sudoku, int[][][] possibilities) {
		boolean hit = false;
		for (int x = 0; x < 9; x++) {
			for (int y = 0; y < 9; y++) {
				if (possibilities[x][y] != null && possibilities[x][y].length == 1) {
					sudoku.set(x, y, possibilities[x][y][0]);
					hit = true;
				}
			}
		}
		return hit;
	}


}
