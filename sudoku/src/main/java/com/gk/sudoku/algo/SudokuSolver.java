package com.gk.sudoku.algo;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.gk.sudoku.Util;
import com.gk.sudoku.model.Sudoku;

@Component
public class SudokuSolver{
	
	@Autowired
	private Util util;

	public Sudoku solve(Sudoku sudoku) {
		boolean matched = false;
		do {
			matched = false;
			
			int[][][] possibilities = sudoku.possibilities();
			if(confirmedHit(sudoku, possibilities))
				matched = true;
			
		}while(matched && !sudoku.solved());
		
		return sudoku;
	}

	private boolean confirmedHit(Sudoku sudoku, int[][][] possibilities) {
		boolean matched = singleHit(sudoku, possibilities);
		if(!matched)
			matched = rowHit(sudoku, possibilities);
		if(!matched)
			matched = colHit(sudoku, possibilities);
		if(!matched)
			matched = boxHit(sudoku, possibilities);
		return matched;
	}

	private boolean rowHit(Sudoku sudoku, int[][][] possibilities) {
		for(int x=0; x<9; x++) {
			Map<Integer, List<Integer>> rowPossibilities = new HashMap<>();
			Map<Integer, Integer> possibilityCount = new HashMap<>();
			for(int y=0; y<9; y++) {
				if(possibilities[x][y] == null)
					continue;
				List<Integer> possibilityList = util.arrayTolist(possibilities[x][y]);
				if(possibilityList.size() > 0) {
					rowPossibilities.put(y, possibilityList);
					for(Integer poss : possibilityList) {
						if(possibilityCount.get(poss) == null)
							possibilityCount.put(poss, 0);
							
						possibilityCount.put(poss, possibilityCount.get(poss)+1);
					}
				}
			}
			for(int poss : possibilityCount.keySet()) {
				if(possibilityCount.get(poss) == 1) {
					for(int y : rowPossibilities.keySet()) {
						if(rowPossibilities.get(y).contains(poss)) {
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
		for(int y=0; y<9; y++) {
			Map<Integer, List<Integer>> colPossibilities = new HashMap<>();
			Map<Integer, Integer> possibilityCount = new HashMap<>();
			for(int x=0; x<9; x++) {
				if(possibilities[x][y] == null)
					continue;
				
				List<Integer> possibilityList = util.arrayTolist(possibilities[x][y]);
				if(possibilityList.size() > 0) {
					colPossibilities.put(x, possibilityList);
					for(Integer poss : possibilityList) {
						if(possibilityCount.get(poss) == null)
							possibilityCount.put(poss, 0);
							
						possibilityCount.put(poss, possibilityCount.get(poss)+1);
					}
				}
			}
			for(int poss : possibilityCount.keySet()) {
				if(possibilityCount.get(poss) == 1) {
					for(int x : colPossibilities.keySet()) {
						if(colPossibilities.get(x).contains(poss)) {
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
		for(int x=0; x<3; x++) {
			for(int y=0; y<3; y++) {
				Map<String, List<Integer>> boxPossibilities = new HashMap<>();
				Map<Integer, Integer> possibilityCount = new HashMap<>();
				for(int x1=0; x1<3; x1++) {
					for(int y1=0; y1<3; y1++) {
						if(possibilities[x*3+x1][y*3+y1] == null)
							continue;
						List<Integer> possibilityList = util.arrayTolist(possibilities[x*3+x1][y*3+y1]);
						if(possibilityList.size() > 0) {
							boxPossibilities.put((x*3+x1)+"_"+(y*3+y1), possibilityList);
							for(Integer poss : possibilityList) {
								if(possibilityCount.get(poss) == null)
									possibilityCount.put(poss, 0);
									
								possibilityCount.put(poss, possibilityCount.get(poss)+1);
							}
						}
					}
				}
				for(int poss : possibilityCount.keySet()) {
					if(possibilityCount.get(poss) == 1) {
						for(String box : boxPossibilities.keySet()) {
							if(boxPossibilities.get(box).contains(poss)) {
								sudoku.set(Integer.parseInt(box.split("_")[0]), Integer.parseInt(box.split("_")[1]), poss);
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
		for(int x=0; x<9; x++) {
			for(int y=0; y<9; y++) {
				if(possibilities[x][y] != null && possibilities[x][y].length == 1) {
					sudoku.set(x, y, possibilities[x][y][0]);
					hit = true;
				}
			}
		}
		return hit;
	}
	
	
	
}
