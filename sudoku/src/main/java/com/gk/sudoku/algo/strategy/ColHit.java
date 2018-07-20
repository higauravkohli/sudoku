package com.gk.sudoku.algo.strategy;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.gk.sudoku.Util;
import com.gk.sudoku.algo.Strategy;
import com.gk.sudoku.model.Sudoku;

public class ColHit implements Strategy {

	private Util util = new Util();

	@Override
	public boolean hit(Sudoku sudoku, int[][][] possibilities) {
		for (int y = 0; y < 9; y++) {
			Map<Integer, List<Integer>> colPossibilities = new HashMap<>();
			Map<Integer, Integer> possibilityCount = new HashMap<>();
			for (int x = 0; x < 9; x++) {
				if (possibilities[x][y] == null)
					continue;

				List<Integer> possibilityList = util.arrayTolist(possibilities[x][y]);
				if (possibilityList.size() > 0) {
					colPossibilities.put(x, possibilityList);
					for (Integer poss : possibilityList) {
						if (possibilityCount.get(poss) == null)
							possibilityCount.put(poss, 0);

						possibilityCount.put(poss, possibilityCount.get(poss) + 1);
					}
				}
			}
			for (int poss : possibilityCount.keySet()) {
				if (possibilityCount.get(poss) == 1) {
					for (int x : colPossibilities.keySet()) {
						if (colPossibilities.get(x).contains(poss)) {
							sudoku.set(x, y, poss);
							return true;
						}
					}
				}
			}
		}

		return false;
	}

}
