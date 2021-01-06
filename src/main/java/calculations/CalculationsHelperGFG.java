package calculations;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class CalculationsHelperGFG {
    // str[i..j] are distinct, otherwise returns false
    public static Boolean areDistinct(List<String> str,
                                      int i, int j) {

        // Note : Default values in visited are false
        // boolean[] visited = new boolean[26];
        Set<String> visited = new HashSet<>();

        for (int k = i; k <= j; k++) {
            if (visited.contains(str.get(k)))
                return false;

            visited.add(str.get(k));
        }
        return true;
    }

    // Returns length of the longest substring
    // with all distinct characters.
    public static int longestUniqueSubsttr(List<String> str) {
        int n = str.size();

        // Result
        int res = 0;

        for (int i = 0; i < n; i++)
            for (int j = i; j < n; j++)
                if (areDistinct(str, i, j))
                    res = Math.max(res, j - i + 1);

        return res;
    }
}
