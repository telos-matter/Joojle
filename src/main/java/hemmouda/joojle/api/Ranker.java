package hemmouda.joojle.api;

import hemmouda.joojle.api.core.MethodRecord;
import hemmouda.joojle.api.core.MethodScore;

import java.util.List;

public class Ranker {

	/**
	 * Performs a fuzzy search using levenshtein distance on the
	 * name (that is assumed to have already been treated)
	 * and the names of the {@link MethodRecord}s
	 *
	 * @return a new ordered list of {@link MethodScore}
	 */
	public static List<MethodScore> rankNames(String name, List<MethodRecord> methods) {
		return methods.stream()
				.map(method -> getMethodNameScore(name, method))
				.sorted()
				.toList();
	}

	/**
	 * @return the score
	 * of the given method with respect
	 * to the given name.
	 */
	private static MethodScore getMethodNameScore(String name, MethodRecord method) {
		var score = lev(name, method.getName());
		return new MethodScore(method, score);
	}

	/**
	 * Performs a fuzzy search using levenshtein distance on the
	 * signature (that is assumed to have already been treated)
	 * and the signatures of the {@link MethodRecord}s
	 * 
	 * @return a new ordered list of {@link MethodScore}
	 */
	public static List<MethodScore> rankSignatures(String signature, List<MethodRecord> methods) {

		return methods.stream()
				.map(method -> getMethodSignatureScore(signature, method))
				.sorted()
				.toList();
	}

	/**
	 * @return the score
	 * of the given method with respect
	 * to the given signature.
	 */
	private static MethodScore getMethodSignatureScore(String signature, MethodRecord method) {
		var score = lev(signature, method.getSignature());
		return new MethodScore(method, score);
	}
	
	/**
	 * Returns the levenshtein distance between the two strings.
	 */
	private static int lev (String a, String b) {
		// If any are empty, then the
		// lev distance is the length
		// of the other string
		if (a.isEmpty()) {
			return b.length();
		} else if (b.isEmpty()) {
			return a.length();
		}

		// Construct the table that will hold the lev distances
		int lenA = a.length() +1;
		int lenB = b.length() +1;
		int [][] distance = new int[lenA][lenB];

		distance[0][0] = 0;
		for (int i = 1; i < lenA; i++) {
			distance[i][0] = i;
		}
		for (int j = 1; j < lenB; j++) {
			distance[0][j] = j;
		}

		// Compute
		for (int i = 1; i < lenA; i++) {
			char aHead = a.charAt(i -1); // As to not repeatedly retrieve it
			for (int j = 1; j < lenB; j++) {

				// If heads are equal
				if (aHead == b.charAt(j -1)) {
					// Then distance is same as without the head
					distance[i][j] = distance[i -1][j -1];

				// Otherwise
				} else {
					// The distance is 1 + minimum of del, add, or sub
					distance[i][j] = 1 + min(
							distance[i -1][j],
							distance[i][j -1],
							distance[i -1][j -1]);
				}
			}
		}

		// Finally, the result is in the corner
		return distance[lenA -1][lenB -1];
	}

	/**
	 * @return minimum of 3 values
	 */
	private static int min (int a, int b, int c) {
		return Math.min(a, Math.min(b, c));
	}

}
