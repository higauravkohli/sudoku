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
		int error = 0;
		int solved = 0;
		int total = 0;
		long gtime = System.currentTimeMillis();
		String line ;
		while((line = read()) != null) {
			total++;
			int[][] puzzle = new int[9][9];
			for(int x=0; x<9; x++) {
				for(int y=0; y<9; y++){
					puzzle[x][y] = Integer.parseInt(String.valueOf(line.charAt((x*9)+y)));
				}
			}
			long time = System.currentTimeMillis();

			Sudoku in = new Sudoku(puzzle);
			Sudoku out = null;
			
			try {
				out = solver.solve(new Sudoku(puzzle));
				if(!out.solved()) {
					print(in, out);
					System.out.print("\nUnsolved ");
				}
				else {
					solved++;
					System.out.print("\nSolved " + line);
				}

				System.out.println(" in " + (System.currentTimeMillis() - time) + " millis");
			}catch(Exception e) {
				e.printStackTrace();
				error++;
				System.out.println("\nError while solving" +  e.getMessage());
				print(in, out);
			}
		}
		System.out.println("Error - " + error + ", Solved - " + solved + " out of " + total + " in " + (System.currentTimeMillis()-gtime)/1000 + " seconds");
	}

	private void print(Sudoku in, Sudoku out) {
		System.out.println("\n\n***********\n");
		System.out.println("** Input **");
		System.out.println(in);
		System.out.println("\n***********\n");
		if(out != null) {
			System.out.println("** Output *");
			System.out.println(out);
			System.out.println("\n***********\n\n");
			if(!out.solved()) {
				int[][][] possibilities = out.possibilities();
				for(int x=0; x<9; x++) {
					System.out.println();
					if(x%3 == 0) {
						System.out.println();
						System.out.println(" + --------- | --------- | --------- +++ --------- | --------- | --------- +++ --------- | --------- | ---------");
						System.out.println();
					}

					for(int y=0; y<9; y++){
						System.out.print(" | ");
						if(y > 0 && y%3 == 0)
							System.out.print("| ");
						int chars = 9;
						for(int i=0; i<chars; i++) {
							boolean printed = false;
							if(possibilities[x][y] != null) {
								for(int poss : possibilities[x][y]) {
									if(poss == (i+1)) {
										System.out.print(poss);
										printed = true;
										break;
									}
								}
							}
							if(!printed)
								System.out.print(" ");
						}
					}
				}
			}
				
		}
	}
	
	private String read() {
		try {
			if(br == null)
				br = new BufferedReader(new InputStreamReader(puzzle.getInputStream()));
			return br.readLine();
		}catch(IOException e) {
			return null;
		}
	}
	
	private BufferedReader br = null;
}
