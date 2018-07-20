package com.gk.sudoku.algo.strategy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.gk.sudoku.Util;
import com.gk.sudoku.algo.Strategy;
import com.gk.sudoku.model.Position;
import com.gk.sudoku.model.Sudoku;

public class BoxMuti implements Strategy{

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
					for (int x1 = 0; x1 < 9; x1++) {
						for (int y1 = 0; y1 < 9; y1++) {
							if (pos.x() / 3 == x1 / 3 && pos.y() / 3 == y1 / 3) {
								if (pos.x() != x1 && pos.y() != y1)
									continue;

								Position newPos = new Position(x1, y1);

								if (possibles.get(newPos) != null && possibles.get(newPos).equals(possibles.get(pos))) {
									if (util.tryPossibilities(sudoku, new ArrayList<Position>() {{add(pos); add(newPos);}}, possibles.get(pos)))
										return true;
								}
							}
						}
					}
				}
			}
		}

		return false;
	}

}
