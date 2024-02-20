package Question2;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class b_SharingSecret {

    public static List<Integer> findIndividualsWithSecret(int n, List<int[]> intervals, int firstPerson) {
        Set<Integer> knownIndividuals = new HashSet<>();
        knownIndividuals.add(firstPerson);

        for (int[] interval : intervals) {
            int start = interval[0];
            int end = interval[1];

            for (int i = start; i <= end; i++) {
                knownIndividuals.add(i);
            }
        }

        List<Integer> result = new ArrayList<>(knownIndividuals);
        result.sort(null);
        return result;
    }

    public static void main(String[] args) {
        int n = 5;
        List<int[]> intervals = List.of(new int[]{0, 2}, new int[]{1, 3}, new int[]{2, 4});
        int firstPerson = 0;

        List<Integer> result = findIndividualsWithSecret(n, intervals, firstPerson);
        System.out.println(result);
    }
}
