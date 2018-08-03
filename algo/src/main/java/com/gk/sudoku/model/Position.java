package com.gk.sudoku.model;

public final class Position {

	private final int x;
	private final int y;

	public Position(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	public int x(){
		return x;
	}
	
	public int y(){
		return y;
	}
	
	@Override
	public int hashCode() {
		return (x+"_"+y).hashCode();
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof Position)
			return x == ((Position)obj).x && y == ((Position)obj).y;
		return super.equals(obj);
	}
}

