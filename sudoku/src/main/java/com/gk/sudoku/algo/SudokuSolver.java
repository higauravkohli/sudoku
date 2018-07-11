package com.gk.sudoku.algo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.gk.sudoku.Util;
import com.gk.sudoku.model.Position;
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
		Map<Position, List<Integer>> possibles = new HashMap<>();
		for (int i = 2; i < 3; i++) {
			for (int x = 0; x < 9; x++) {
				for (int y = 0; y < 9; y++) {
					if (possibilities[x][y] != null && possibilities[x][y].length == i) {
						possibles.put(new Position(x, y), util.arrayTolist(possibilities[x][y]));
					}
				}
			}
			if(possibles.size() > 0) {
				for (Position pos : possibles.keySet()) {
					if (rowRandom(sudoku, pos, possibles))
						return true;
					if (colRandom(sudoku, pos, possibles))
						return true;
					if (boxRandom(sudoku, pos, possibles))
						return true;
				}
			}
		}

		return false;
	}

	private boolean rowRandom(Sudoku sudoku, Position pos, Map<Position, List<Integer>> possibles) {
		for (int y = pos.y() + 1; y < 9; y++) {
			Position newPos = new Position(pos.x(), y);
			if (possibles.get(newPos) != null && possibles.get(newPos).equals(possibles.get(pos))) {
				if (tryPossibilities(sudoku, new ArrayList<Position>() {{add(pos); add(newPos);}}, possibles.get(pos)))
					return true;
			}
		}
		return false;
	}

	private boolean boxRandom(Sudoku sudoku, Position pos, Map<Position, List<Integer>> possibles) {
		for (int x1 = 0; x1 < 9; x1++) {
			for (int y1 = 0; y1 < 9; y1++) {
				if (pos.x() / 3 == x1 / 3 && pos.y() / 3 == y1 / 3) {
					if (pos.x() != x1 && pos.y() != y1)
						continue;

					Position newPos = new Position(x1, y1);

					if (possibles.get(newPos) != null && possibles.get(newPos).equals(possibles.get(pos))) {
						if (tryPossibilities(sudoku, new ArrayList<Position>() {{add(pos); add(newPos);}}, possibles.get(pos)))
							return true;
					}
				}
			}
		}
		return false;
	}

	private boolean colRandom(Sudoku sudoku, Position pos, Map<Position, List<Integer>> possibles) {
		for (int x = pos.x() + 1; x < 9; x++) {
			Position newPos = new Position(x, pos.y());
			if (possibles.get(newPos) != null && possibles.get(newPos).equals(possibles.get(pos))) {
				if (tryPossibilities(sudoku, new ArrayList<Position>() {{add(pos); add(newPos);}}, possibles.get(pos)))
					return true;
			}
		}
		return false;
	}

	private boolean tryPossibilities(Sudoku sudoku, List<Position> pos, List<Integer> possibles) {

		for(List<Integer> possibility : possibilities(possibles.size()))
			if(tryPossibility(sudoku, pos, possibles, possibility))
				return true;
		
		return false;
	}
	
	private List<List<Integer>> possibilities(int size) {
		List<List<Integer>> result = new ArrayList<>();
	 
		result.add(new ArrayList<Integer>());
	 
		for (int i = 0; i < size; i++) {
			List<List<Integer>> current = new ArrayList<>();
	 
			for (List<Integer> l : result) {
				for (int j = 0; j < l.size()+1; j++) {
					l.add(j, i);
	 
					List<Integer> temp = new ArrayList<>(l);
					current.add(temp);
					l.remove(j);
				}
			}
	 
			result = new ArrayList<>(current);
		}
	 
		return result;
	}
	
	private boolean tryPossibility(Sudoku sudoku, List<Position> pos, List<Integer> possibles, List<Integer> possibility) {
		Sudoku trySudoku = new Sudoku(sudoku);

		try {
			for(int i=0; i<possibility.size(); i++) {
				trySudoku.set(pos.get(i), possibles.get(possibility.get(i)));
			}
			solve(trySudoku);
		} catch (Exception e) {
			for(int i=0; i<possibility.size(); i++) {
				sudoku.unfit(pos.get(i), possibles.get(possibility.get(i)));
			}
		}
		if (trySudoku.solved()) {
			sudoku.setPuzzle(trySudoku.getPuzzle());
			return true;
		}
		for(int i=0; i<possibility.size(); i++) {
			sudoku.unfit(pos.get(i), possibles.get(possibility.get(i)));
		}
		
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
