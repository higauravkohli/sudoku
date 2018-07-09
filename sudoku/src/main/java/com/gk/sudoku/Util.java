package com.gk.sudoku;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.stereotype.Component;

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
}

