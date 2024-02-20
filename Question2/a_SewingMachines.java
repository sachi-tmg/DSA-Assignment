package Question2;
public class a_SewingMachines {

    public static int equalizeDresses(int[] machines) {
        int n = machines.length;
        int total = 0;
        for (int dress : machines) {
            total += dress;
        }

        if (total % n != 0) {
            return -1; 
        }

        int target = total / n;
        int moves = 0;

        for (int i = 0; i < n; i++) {
            int diff = target - machines[i];
            if (diff > 0) {
                
                moves += Math.min(diff, machines[i + 1]);
                machines[i + 1] -= Math.min(diff, machines[i + 1]);
            } else if (diff < 0) {
                
                moves += Math.min(-diff, machines[i - 1]);
                machines[i - 1] -= Math.min(-diff, machines[i - 1]);
            }
        }

        return moves;
    }

    public static void main(String[] args) {
        int[] machines = {1, 0, 5};
        int moves = equalizeDresses(machines);
        System.out.println("Minimum moves required: " + moves);
    }
}
