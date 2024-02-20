package Question4;
import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Stack;

class TreeNode {
    int val;
    TreeNode left, right;

    public TreeNode(int val) {
        this.val = val;
    }
}

public class b_BinarySearchTree {
    public static List<Integer> closestValues(TreeNode root, double k, int x) {
        List<Integer> result = new ArrayList<>();
        Stack<TreeNode> stack = new Stack<>();
        PriorityQueue<Pair> closestHeap = new PriorityQueue<>((a, b) -> Double.compare(b.diff, a.diff));

        while (root != null || !stack.isEmpty()) {
            while (root != null) {
                stack.push(root);
                root = root.left;
            }

            root = stack.pop();
            
            // Calculate the absolute difference between the current node value and the target
            double diff = Math.abs(root.val - k);

            // Add the current node to the max heap
            closestHeap.offer(new Pair(root.val, diff));

            // If the heap size exceeds x, remove the element with the largest difference
            if (closestHeap.size() > x) {
                closestHeap.poll();
            }

            root = root.right;
        }

        // Extract the values from the max heap
        while (!closestHeap.isEmpty()) {
            result.add(closestHeap.poll().value);
        }

        return result;
    }

    public static void main(String[] args) {
        // Example usage:
        // Construct the binary search tree
        TreeNode root = new TreeNode(4);
        root.left = new TreeNode(2);
        root.left.left = new TreeNode(1);
        root.left.right = new TreeNode(3);
        root.right = new TreeNode(5);

        // Given target and value of x
        double targetK = 3.8;
        int xValue = 2;

        // Find x number of values closest to the target
        List<Integer> result = closestValues(root, targetK, xValue);

        // Output the result
        System.out.println(result);
    }

    // Pair class to represent the node value and its difference from the target
    static class Pair {
        int value;
        double diff;

        public Pair(int value, double diff) {
            this.value = value;
            this.diff = diff;
        }
    }
}
