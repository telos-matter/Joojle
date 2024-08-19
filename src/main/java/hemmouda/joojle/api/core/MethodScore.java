package hemmouda.joojle.api.core;

/**
 * A simple record that holds the score
 * of a {@link MethodRecord} with
 * respect to an unknown query.
 */
public record MethodScore (
		MethodRecord method,
		double score
) implements Comparable<MethodScore> {

	@Override
	public int compareTo(MethodScore other) {
		return Double.compare(score, other.score);
	}
}
