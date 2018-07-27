package com.gk.sudoku.algo.strategy.multi;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.gk.sudoku.Util;
import com.gk.sudoku.algo.Strategy;
import com.gk.sudoku.model.Position;
import com.gk.sudoku.model.Sudoku;

public class Box implements Strategy{

	private Util util = new Util();
	
	@Override
	public boolean hit(Sudoku sudoku, int[][][] possibilities) {
		for(int i=2; i<9; i++) {
			Map<Position, List<Integer>> possibles = new HashMap<>();
			for (int x = 0; x < 9; x++) {
				for (int y = 0; y < 9; y++) {
					if (possibilities[x][y] != null && possibilities[x][y].length == 2) {
						possibles.put(new Position(x, y), util.arrayTolist(possibilities[x][y]));
					}
				}
			}
			if(possibles.size() > 0) {
				Position[] possArr = new Position[possibles.keySet().size()];
				possArr = possibles.keySet().toArray(possArr);
				
				List<List<Integer>> combinations = util.possibilities(0, possibles.size(), i);
				for (List<Integer> combination : combinations) {
					List<Position> poss = new ArrayList<>();
					
					
					for(int comb : combination) {
						Position pos = possArr[comb];
						if(poss.size() == 0)
							poss.add(pos);
						else {
							if (pos.x()/3 == poss.get(0).x()/3 && pos.y()/3 == poss.get(0).y()/3 && possibles.get(pos) != null && possibles.get(pos).equals(possibles.get(poss.get(0)))) {
								poss.add(pos);
							}
						}
					}
						
					if(poss.size()  == i) {
						if (util.tryPossibilities(sudoku, poss, possibles.get(poss.get(0))))
							return true;
					}
				}
			}
		}
		
		return false;
	}

}
