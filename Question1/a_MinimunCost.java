package Question1;
public class a_MinimunCost {

    public static int findMinCost(int[][] costs) {
        if (costs == null || costs.length == 0 || costs[0].length == 0) {
            return 0;
        }

        int n = costs.length;
        int k = costs[0].length;

        // Initialize a 2D DP array to store the minimum cost for each venue and theme combination
        int[][] dp = new int[n][k];

        // Initialize the first row with the costs of decorating the first venue
        dp[0] = costs[0];

        // Iterate through each venue
        for (int i = 1; i < n; i++) {
            // Iterate through each theme for the current venue
            for (int j = 0; j < k; j++) {
                // Find the minimum cost by considering the costs of the previous venue
                int minPrevCost = Integer.MAX_VALUE;
                for (int l = 0; l < k; l++) {
                    if (l != j) {
                        minPrevCost = Math.min(minPrevCost, dp[i - 1][l]);
                    }
                }
                dp[i][j] = costs[i][j] + minPrevCost;
            }
        }

        // Find the minimum cost in the last row of the DP array
        int minCost = Integer.MAX_VALUE;
        for (int j = 0; j < k; j++) {
            minCost = Math.min(minCost, dp[n - 1][j]);
        }

        return minCost;
    }

    public static void main(String[] args) {
        int[][] costMatrix = {
                {1, 3, 2},
                {4, 6, 8},
                {3, 1, 5}
        };

        int result = findMinCost(costMatrix);
        System.out.println(result);  // Output should be 7
    }
}
