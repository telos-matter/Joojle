package hemmouda.joojle.api;

import hemmouda.joojle.api.core.MethodRecord;
import hemmouda.joojle.api.core.MethodScore;

import java.util.ArrayList;
import java.util.List;

/**
 * Handles the queries.
 */
public class QueryHandler {

    public static final String SIGNATURE_NAME_SEP = ":";

    /**
     * Process a query for the given
     * loaded methods and returns the result
     */
    public static List<MethodScore> handle (String query, List<MethodRecord> methods) {
        // Set the signature and name
        String signature = null;
        String name = null;
        if (query.contains(SIGNATURE_NAME_SEP)) {
            int index = query.indexOf(SIGNATURE_NAME_SEP);
            signature = query.substring(0, index).strip();
            name = query.substring(index +1).strip();
            // If signature is blank then searching for name alone
            if (signature.isBlank()) {
                signature = null;
            }
        } else {
            signature = query.strip();
        }

        // Set signature and name scores
        List <MethodScore> signatureScores = null;
        List <MethodScore> nameScores = null;
        if (signature != null) {
            signatureScores = Ranker.rankSignatures(signature, methods);
        }
        if (name != null) {
            nameScores = Ranker.rankNames(name, methods);
        }

        // Return either or best of the two if both are set
        if (signatureScores == null) {
            return  nameScores;
        } else if (nameScores == null) {
            return signatureScores;
        } else {
            List <MethodScore> bestScores = new ArrayList<>(signatureScores.size());
            for (MethodScore signatureScore : signatureScores) {
                MethodScore nameScore = getMethodScore(nameScores, signatureScore.method());
                MethodScore bestScore = (signatureScore.score() >= nameScore.score())? signatureScore : nameScore;
                bestScores.add(bestScore);
            }

            return bestScores;
        }
    }

    /**
     * Simplifies a signature
     * of a method by removing unnecessary stuff.
     * Useful in order to make the fuzzy search go
     * a little more zoom zoom.
     */
    public static String simplifySignature(String query) {
        // So far, all I can think about is removing white space
        // Changing the case is no go, because there could be
        // a class called FooBar and another class called
        // Foobar. Two different things.
        // Could also remove the parenthesis, but
        // it's fast enough as it is.
        return query.replace(" ", "");
    }

    /**
     * @return the existing methodScore of this methodRecord
     */
    private static MethodScore getMethodScore (List<MethodScore> methodScores, MethodRecord methodRecord) {
        for (MethodScore methodScore : methodScores) {
            if (methodScore.method().equals(methodRecord)) {
                return methodScore;
            }
        }

        throw new IllegalArgumentException("MethodScore does not exist!");
    }

}
