package calculations;

import javax.print.attribute.standard.PresentationDirection;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class CalculationsHelperGFG {
    // str[i..j] are distinct, otherwise returns false
    Set<String> points;

    public  Boolean areDistinct(List<String> str,
                                      int i, int j, int max) {

        // Note : Default values in visited are false
        // boolean[] visited = new boolean[26];
        Set<String> visited = new HashSet<>();

        for (int k = i; k <= j; k++) {
            if (visited.contains(str.get(k)))
                return false;

            visited.add(str.get(k));
        }
        if ((j - i + 1) >= max) {
            points = visited;
        }
        return true;
    }

    // Returns length of the longest substring
    // with all distinct characters.
    public  String longestUniqueSubsttr(List<String> str) {
        int n = str.size();

        // Result
        int res = 0;

        for (int i = 0; i < n; i++)
            for (int j = i; j < n; j++)
                if (areDistinct(str, i, j, res))
                    res = Math.max(res, j - i + 1);

                StringBuilder stringBuilder = new StringBuilder();
        for (String point : points) {
            stringBuilder.append(point).append("---");
        }
        return stringBuilder.toString();
    }
}
