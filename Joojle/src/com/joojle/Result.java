package com.joojle;

public class Result implements Comparable<Result> {

	private Function function;
	private String query;
	private int score;
	
	public Result (Function function, String query) {
		this.function = function;
		this.query = query;
		this.score = Searcher.lev(query, function.getSignature());
	}
	
	public int getScore () {
		return this.score;
	}

	@Override
	public int compareTo(Result other) {
		return Integer.compare(score, other.score);
	}

	@Override
	public String toString() {
		return String.format("%d: %s", score, function);
	}
	
}
