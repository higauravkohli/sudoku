package com.gk.sudoku.command;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import com.gk.sudoku.algo.SudokuSolver;
import com.gk.sudoku.model.Sudoku;

@Component
public class SudokuCommandLine implements CommandLineRunner{

	@Value("classpath:puzzle.txt")
	private Resource puzzle;

	@Autowired
	private SudokuSolver solver;
	
	@Override
	public void run(String... args) throws Exception {
		String line = read();
		int[][] puzzle = new int[9][9];
		for(int x=0; x<9; x++) {
			for(int y=0; y<9; y++){
				puzzle[x][y] = Integer.parseInt(String.valueOf(line.charAt((x*9)+y)));
			}
		}
		Sudoku sudoku = new Sudoku(puzzle);
		System.out.println("\n\n***********\n");
		System.out.println("** Input **");
		print(sudoku);
		System.out.println("\n***********\n");
		sudoku = solver.solve(sudoku);
		System.out.println("** Output *");
		print(sudoku);
		System.out.println("\n***********\n\n");
	}

	private void print(Sudoku sudoku) {
		for(int x=0; x<9; x++) {
			System.out.println();
			if(x > 0 && x%3 == 0)
				System.out.println("---+---+---");

			for(int y=0; y<9; y++){
				if(y > 0 && y%3 == 0)
					System.out.print("|");
				System.out.print(sudoku.get(x, y));
			}
		}
	}
	
	private String read() {
		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(puzzle.getInputStream()));
			return br.readLine();
		}catch(IOException e) {
			throw new RuntimeException(e);
		}
	}
}
