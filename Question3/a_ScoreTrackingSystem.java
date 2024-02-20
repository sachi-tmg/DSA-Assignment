package Question3;
import java.util.PriorityQueue;

public class a_ScoreTrackingSystem {
    private PriorityQueue<Double> maxHeap; // stores the smaller half of the scores
    private PriorityQueue<Double> minHeap; // stores the larger half of the scores

    public a_ScoreTrackingSystem() {
        maxHeap = new PriorityQueue<>((a, b) -> Double.compare(b, a)); // max heap
        minHeap = new PriorityQueue<>(); // min heap
    }

    public void addScore(double score) {
        if (maxHeap.isEmpty() || score <= maxHeap.peek()) {
            maxHeap.offer(score);
        } else {
            minHeap.offer(score);
        }

        // Balance the heaps
        if (maxHeap.size() > minHeap.size() + 1) {
            minHeap.offer(maxHeap.poll());
        } else if (minHeap.size() > maxHeap.size()) {
            maxHeap.offer(minHeap.poll());
        }
    }

    public double getMedianScore() {
        if (maxHeap.isEmpty() && minHeap.isEmpty()) {
            throw new IllegalStateException("No scores available.");
        }

        if (maxHeap.size() == minHeap.size()) {
            return (maxHeap.peek() + minHeap.peek()) / 2.0;
        } else {
            return maxHeap.peek();
        }
    }

    public static void main(String[] args) {
        a_ScoreTrackingSystem scoreTracker = new a_ScoreTrackingSystem();

        scoreTracker.addScore(85.5);
        System.out.println("Stream: " + scoreTracker); // [85.5]

        scoreTracker.addScore(92.3);
        System.out.println("Stream: " + scoreTracker); // [85.5, 92.3]

        scoreTracker.addScore(77.8);
        System.out.println("Stream: " + scoreTracker); // [77.8, 85.5, 92.3]

        scoreTracker.addScore(90.1);
        System.out.println("Stream: " + scoreTracker); // [77.8, 85.5, 90.1, 92.3]

        double median1 = scoreTracker.getMedianScore();
        System.out.println("Median 1: " + median1); // Output: 87.8

        scoreTracker.addScore(81.2);
        System.out.println("Stream: " + scoreTracker); // [77.8, 81.2, 85.5, 90.1, 92.3]

        scoreTracker.addScore(88.7);
        System.out.println("Stream: " + scoreTracker); // [77.8, 81.2, 85.5, 88.7, 90.1, 92.3]

        double median2 = scoreTracker.getMedianScore();
        System.out.println("Median 2: " + median2); // Output: 87.1
    }
}
