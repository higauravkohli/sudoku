package com.gk.sudoku.algo.strategy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.gk.sudoku.Util;
import com.gk.sudoku.algo.Strategy;
import com.gk.sudoku.model.Position;
import com.gk.sudoku.model.Sudoku;

public class RowMuti implements Strategy{

	private Util util = new Util();
	
	@Override
	public boolean hit(Sudoku sudoku, int[][][] possibilities) {
		Map<Position, List<Integer>> possibles = new HashMap<>();
		for (int i = 2; i < 9; i++) {
			for (int x = 0; x < 9; x++) {
				for (int y = 0; y < 9; y++) {
					if (possibilities[x][y] != null && possibilities[x][y].length == i) {
						possibles.put(new Position(x, y), util.arrayTolist(possibilities[x][y]));
					}
				}
			}
			if(possibles.size() > 0) {
				for (Position pos : possibles.keySet()) {
					for (int y = pos.y() + 1; y < 9; y++) {
						Position newPos = new Position(pos.x(), y);
						if (possibles.get(newPos) != null && possibles.get(newPos).equals(possibles.get(pos))) {
							if (util.tryPossibilities(sudoku, new ArrayList<Position>() {{add(pos); add(newPos);}}, possibles.get(pos)))
								return true;
						}
					}
				}
			}
		}

		return false;
	}

}
