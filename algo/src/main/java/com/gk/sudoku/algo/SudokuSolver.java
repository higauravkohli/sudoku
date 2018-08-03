package com.gk.sudoku.algo;

import org.springframework.stereotype.Component;

import com.gk.sudoku.algo.strategy.multi.DualStrategy;
import com.gk.sudoku.algo.strategy.single.SingleStrategy;
import com.gk.sudoku.model.Sudoku;

@Component
public class SudokuSolver {

	private Strategy[] strategies = {
		new SingleStrategy(),
		new DualStrategy()
	};

	public Sudoku solve(Sudoku sudoku) {
		boolean matched = false;
		do {
			matched = false;

			int[][][] possibilities = sudoku.possibilities();
			
			for(Strategy strategy : strategies) {
				matched = strategy.hit(sudoku, possibilities);
				if(matched)
					break;
			}

		} while (matched && !sudoku.solved());

		return sudoku;
	}

}
