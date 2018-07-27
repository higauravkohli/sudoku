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
		for(List<Integer> possibility : possibilities(possibles.size()))
			if(tryPossibility(sudoku, pos, possibles, possibility))
				return true;
		
		return false;
	}
	
	public List<List<Integer>> possibilities(int min, int max, int size){
		int[] A = new int[max-min];
		for(int i=0; i<A.length; i++)
			A[i] = min+i;
		
	    List<List<Integer>> set = new ArrayList<>();
	    for(Integer s : A)
	    {
	    	List<Integer> ss = new ArrayList<>();
	        ss.add(s);
	        set.add(ss);
	    }
	    
	    return getCombinations(A, set, size);
	}
	
	private List<List<Integer>> getCombinations(int[] A, List<List<Integer>> s, int f)
	{
	    if(f == 1)
	        return s;
	    List<List<Integer>> newSet = new ArrayList<>();
	    for (List<Integer> ss : s)
	    {
	        for(Integer elm : A)
	        {
	            if(ss.contains(elm))
	                continue;
	            List<Integer> sss = new ArrayList<>(ss);
	            sss.add(elm);
	            newSet.add(sss);
	        }
	    }
	    return getCombinations(A, newSet, f-1);
	}
	
	private List<List<Integer>> possibilities(int size) {
		return possibilities(0, size, size);
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

