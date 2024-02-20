package Question1;
public class b_BuildEngine {
    public static int minTimeToBuildEngines(int[] engines, int splitCost) {
        int n = engines.length;

        // dp[i][j] represents the minimum time to build the first i engines with j engineers
        int[][] dp = new int[n + 1][n + 1];

        // Base case: with 0 engineers, time is 0
        for (int i = 0; i <= n; i++) {
            dp[i][0] = 0;
        }

        for (int i = 1; i <= n; i++) {
            for (int j = 1; j <= i; j++) {
                for (int k = 0; k < i; k++) {
                    int splitTime = (k > 0) ? splitCost : 0;
                    dp[i][j] = Math.min(dp[i][j], Math.max(dp[k][j - 1], sum(engines, k, i)) + splitTime);
                }
            }
        }

        return dp[n][n];
    }

    private static int sum(int[] engines, int start, int end) {
        int result = 0;
        for (int i = start; i < end; i++) {
            result += engines[i];
        }
        return result;
    }

    public static void main(String[] args) {
        int[] engines = {1, 2, 3};
        int splitCost = 1;
        int result = minTimeToBuildEngines(engines, splitCost);
        System.out.println(result);
    }
}
