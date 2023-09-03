package joojle;

public class Result implements Comparable<Result> {

	private Function function;
	@SuppressWarnings("unused")
	private String query;
	private int score;
	
	public Result (Function function, String query) {
		this.function = function;
		this.query = query;
		this.score = Searcher.lev(query, function.getSignature());
	}
	
	public int getScore () {
		return score;
	}

	@Override
	public int compareTo(Result other) {
		return Integer.compare(score, other.score);
	}

	@Override
	public String toString() {
		return String.format("%s", function);
	}
	
}
