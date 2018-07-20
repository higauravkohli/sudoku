package com.gk.sudoku.algo;

import org.springframework.stereotype.Component;

import com.gk.sudoku.algo.strategy.BoxHit;
import com.gk.sudoku.algo.strategy.BoxMuti;
import com.gk.sudoku.algo.strategy.ColHit;
import com.gk.sudoku.algo.strategy.ColMuti;
import com.gk.sudoku.algo.strategy.RowHit;
import com.gk.sudoku.algo.strategy.RowMuti;
import com.gk.sudoku.algo.strategy.SingleHit;
import com.gk.sudoku.model.Sudoku;

@Component
public class SudokuSolver {

	private Strategy[] strategies = {
		new SingleHit()	,
		new RowHit(),
		new ColHit(),
		new BoxHit(),
		new RowMuti(),
		new ColMuti(),
		new BoxMuti()
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
