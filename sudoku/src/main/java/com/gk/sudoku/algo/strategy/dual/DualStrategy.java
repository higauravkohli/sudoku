package com.gk.sudoku.algo.strategy.dual;

import com.gk.sudoku.algo.Strategy;
import com.gk.sudoku.model.Sudoku;

public class DualStrategy implements Strategy{

	private Strategy[] strategies = {
		new Row(),
		new Col(),
		new Box(),
	};

	@Override
	public boolean hit(Sudoku sudoku, int[][][] possibilities) {
		for(Strategy strategy : strategies) {
			if(strategy.hit(sudoku, possibilities))
				return true;
		}

		return false;
	}

}
