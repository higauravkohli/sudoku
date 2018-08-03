package com.gk.sudoku.algo.strategy.single;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.gk.sudoku.Util;
import com.gk.sudoku.algo.Strategy;
import com.gk.sudoku.model.Sudoku;

public class Box implements Strategy{

	private Util util = new Util();
	
	@Override
	public boolean hit(Sudoku sudoku, int[][][] possibilities) {
		for (int x = 0; x < 3; x++) {
			for (int y = 0; y < 3; y++) {
				Map<String, List<Integer>> boxPossibilities = new HashMap<>();
				Map<Integer, Integer> possibilityCount = new HashMap<>();
				for (int x1 = 0; x1 < 3; x1++) {
					for (int y1 = 0; y1 < 3; y1++) {
						if (possibilities[x * 3 + x1][y * 3 + y1] == null)
							continue;
						List<Integer> possibilityList = util.arrayTolist(possibilities[x * 3 + x1][y * 3 + y1]);
						if (possibilityList.size() > 0) {
							boxPossibilities.put((x * 3 + x1) + "_" + (y * 3 + y1), possibilityList);
							for (Integer poss : possibilityList) {
								if (possibilityCount.get(poss) == null)
									possibilityCount.put(poss, 0);

								possibilityCount.put(poss, possibilityCount.get(poss) + 1);
							}
						}
					}
				}
				for (int poss : possibilityCount.keySet()) {
					if (possibilityCount.get(poss) == 1) {
						for (String box : boxPossibilities.keySet()) {
							if (boxPossibilities.get(box).contains(poss)) {
								sudoku.set(Integer.parseInt(box.split("_")[0]), Integer.parseInt(box.split("_")[1]),
										poss);
								return true;
							}
						}
					}
				}
			}
		}

		return false;
	}

}
