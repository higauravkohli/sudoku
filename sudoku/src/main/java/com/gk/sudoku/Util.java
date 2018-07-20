package com.gk.sudoku;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.stereotype.Component;

import com.gk.sudoku.algo.SudokuSolver;
import com.gk.sudoku.model.Position;
import com.gk.sudoku.model.Sudoku;

@Component
public class Util {

	public int[] listToArray(Collection<Integer> values) {
		int[] valuesArr = new int[values.size()];
		int i=0;
		for(Integer vl : values)
			valuesArr[i++] = vl;
		
		return valuesArr;
	}
	
	public List<Integer> arrayTolist(int[] values) {
		List<Integer> valuesList = new ArrayList<>();
		for(int val : values)
			if(val != 0)
				valuesList.add(val);
		
		return valuesList;
	}
	
	public boolean tryPossibilities(Sudoku sudoku, List<Position> pos, List<Integer> possibles) {
		if(possibles.size() != 2)
			return false;
		
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
			SudokuSolver solver = new SudokuSolver();
			solver.solve(trySudoku);
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

}

