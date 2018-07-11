package com.gk.sudoku.algo;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.gk.sudoku.Util;
import com.gk.sudoku.model.Sudoku;

@Component
public class SudokuSolver {

	@Autowired
	private Util util;

	public Sudoku solve(Sudoku sudoku) {
		boolean matched = false;
		do {
			matched = false;

			int[][][] possibilities = sudoku.possibilities();
			matched = confirmedHit(sudoku, possibilities);
			if (!matched)
				matched = pickRandom(sudoku, possibilities);

		} while (matched && !sudoku.solved());

		return sudoku;
	}

	private boolean pickRandom(Sudoku sudoku, int[][][] possibilities) {
		Map<String, String> possibles = new HashMap<>();
		for (int x = 0; x < 9; x++) {
			for (int y = 0; y < 9; y++) {
				if (possibilities[x][y] != null && possibilities[x][y].length == 2) {
					possibles.put(x + "_" + y, possibilities[x][y][0] + "_" + possibilities[x][y][1]);
				}
			}
		}

		for (String possible : possibles.keySet()) {
			int x = Integer.parseInt(possible.split("_")[0]);
			int y = Integer.parseInt(possible.split("_")[1]);

			if (rowRandom(sudoku, x, y, possibles))
				return true;
			if (colRandom(sudoku, x, y, possibles))
				return true;
			if (boxRandom(sudoku, x, y, possibles))
				return true;
		}

		return false;
	}

	private boolean rowRandom(Sudoku sudoku, int x, int y, Map<String, String> possibles) {
		for (int y1 = 0; y1 < 9; y1++) {
			if (y1 > y) {
				if (possibles.get(x + "_" + y1) != null
						&& possibles.get(x + "_" + y1).equals(possibles.get(x + "_" + y))) {
					if (tryPossibilities(sudoku, x, y, x, y1,
							Integer.parseInt(possibles.get(x + "_" + y).split("_")[0]),
							Integer.parseInt(possibles.get(x + "_" + y).split("_")[1])))
						return true;
				}
			}
		}
		return false;
	}

	private boolean boxRandom(Sudoku sudoku, int x, int y, Map<String, String> possibles) {
		for (int x1 = 0; x1 < 9; x1++) {
			for (int y1 = 0; y1 < 9; y1++) {
				if (x / 3 == x1 / 3 && y / 3 == y1 / 3) {
					if (x != x1 && y != y1)
						continue;

					if (possibles.get(x1 + "_" + y1) != null
							&& possibles.get(x1 + "_" + y1).equals(possibles.get(x + "_" + y))) {
						if (tryPossibilities(sudoku, x, y, x1, y1,
								Integer.parseInt(possibles.get(x + "_" + y).split("_")[0]),
								Integer.parseInt(possibles.get(x + "_" + y).split("_")[1])))
							return true;
					}
				}
			}
		}
		return false;
	}

	private boolean colRandom(Sudoku sudoku, int x, int y, Map<String, String> possibles) {
		for (int x1 = 0; x1 < 9; x1++) {
			if (x1 > x) {
				if (possibles.get(x1 + "_" + y) != null
						&& possibles.get(x1 + "_" + y).equals(possibles.get(x + "_" + y))) {
					if (tryPossibilities(sudoku, x, y, x1, y,
							Integer.parseInt(possibles.get(x + "_" + y).split("_")[0]),
							Integer.parseInt(possibles.get(x + "_" + y).split("_")[1])))
					return true;
				}
			}
		}
		return false;
	}

	private boolean tryPossibilities(Sudoku sudoku, int x, int y, int x1, int y1, int poss1, int poss2) {
		Sudoku trySudoku = new Sudoku(sudoku);

		try {
			trySudoku.set(x, y, poss1);
			trySudoku.set(x1, y1, poss2);
			solve(trySudoku);
		} catch (Exception e) {
			sudoku.unfit(x, y, poss1);
			sudoku.unfit(x1, y1, poss2);
		}
		if (trySudoku.solved()) {
			sudoku.setPuzzle(trySudoku.getPuzzle());
			return true;
		} 
		sudoku.unfit(x, y, poss1);
		sudoku.unfit(x1, y1, poss2);

		trySudoku = new Sudoku(sudoku);

		try {
			trySudoku.set(x, y, poss2);
			trySudoku.set(x1, y1, poss1);
			solve(trySudoku);
		} catch (Exception e) {
			sudoku.unfit(x, y, poss2);
			sudoku.unfit(x1, y1, poss1);
		}

		if (trySudoku.solved()) {
			sudoku.setPuzzle(trySudoku.getPuzzle());
			return true;
		} 
		
		sudoku.unfit(x, y, poss2);
		sudoku.unfit(x1, y1, poss1);
		
		return false;
	}

	private boolean confirmedHit(Sudoku sudoku, int[][][] possibilities) {
		if (singleHit(sudoku, possibilities))
			return true;

		if (rowHit(sudoku, possibilities))
			return true;

		if (colHit(sudoku, possibilities))
			return true;

		if (boxHit(sudoku, possibilities))
			return true;

		return false;
	}

	private boolean rowHit(Sudoku sudoku, int[][][] possibilities) {
		for (int x = 0; x < 9; x++) {
			Map<Integer, List<Integer>> rowPossibilities = new HashMap<>();
			Map<Integer, Integer> possibilityCount = new HashMap<>();
			for (int y = 0; y < 9; y++) {
				if (possibilities[x][y] == null)
					continue;
				List<Integer> possibilityList = util.arrayTolist(possibilities[x][y]);
				if (possibilityList.size() > 0) {
					rowPossibilities.put(y, possibilityList);
					for (Integer poss : possibilityList) {
						if (possibilityCount.get(poss) == null)
							possibilityCount.put(poss, 0);

						possibilityCount.put(poss, possibilityCount.get(poss) + 1);
					}
				}
			}
			for (int poss : possibilityCount.keySet()) {
				if (possibilityCount.get(poss) == 1) {
					for (int y : rowPossibilities.keySet()) {
						if (rowPossibilities.get(y).contains(poss)) {
							sudoku.set(x, y, poss);
							return true;
						}
					}
				}
			}
		}

		return false;
	}

	private boolean colHit(Sudoku sudoku, int[][][] possibilities) {
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

	private boolean boxHit(Sudoku sudoku, int[][][] possibilities) {
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

	private boolean singleHit(Sudoku sudoku, int[][][] possibilities) {
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
