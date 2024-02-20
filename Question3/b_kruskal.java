package Question3;
import java.util.*;

class Edge implements Comparable<Edge> {
    int src, dest, weight;

    public Edge(int src, int dest, int weight) {
        this.src = src;
        this.dest = dest;
        this.weight = weight;
    }

    @Override
    public int compareTo(Edge other) {
        return Integer.compare(this.weight, other.weight);
    }
}

class DisjointSet {
    int[] parent, rank;

    public DisjointSet(int vertices) {
        parent = new int[vertices];
        rank = new int[vertices];
        for (int i = 0; i < vertices; i++) {
            parent[i] = i;
            rank[i] = 0;
        }
    }

    int find(int node) {
        if (node != parent[node]) {
            parent[node] = find(parent[node]);
        }
        return parent[node];
    }

    void union(int u, int v) {
        int rootU = find(u);
        int rootV = find(v);

        if (rootU != rootV) {
            if (rank[rootU] < rank[rootV]) {
                parent[rootU] = rootV;
            } else if (rank[rootU] > rank[rootV]) {
                parent[rootV] = rootU;
            } else {
                parent[rootV] = rootU;
                rank[rootU]++;
            }
        }
    }
}

public class b_kruskal {
    public static List<Edge> kruskal(List<Edge> edges, int vertices) {
        List<Edge> minSpanningTree = new ArrayList<>();
        Collections.sort(edges);

        DisjointSet disjointSet = new DisjointSet(vertices);

        for (Edge edge : edges) {
            int rootSrc = disjointSet.find(edge.src);
            int rootDest = disjointSet.find(edge.dest);

            if (rootSrc != rootDest) {
                minSpanningTree.add(edge);
                disjointSet.union(rootSrc, rootDest);
            }
        }

        return minSpanningTree;
    }

    public static void main(String[] args) {
        List<Edge> edges = new ArrayList<>();
        edges.add(new Edge(0, 1, 4));
        edges.add(new Edge(0, 7, 8));
        edges.add(new Edge(1, 2, 8));
        edges.add(new Edge(1, 7, 11));
        edges.add(new Edge(2, 3, 7));
        edges.add(new Edge(2, 5, 4));
        edges.add(new Edge(2, 8, 2));
        edges.add(new Edge(3, 4, 9));
        edges.add(new Edge(3, 5, 14));
        edges.add(new Edge(4, 5, 10));
        edges.add(new Edge(5, 6, 2));
        edges.add(new Edge(6, 7, 1));
        edges.add(new Edge(6, 8, 6));
        edges.add(new Edge(7, 8, 7));

        int vertices = 9;

        List<Edge> minSpanningTree = kruskal(edges, vertices);

        System.out.println("Minimum Spanning Tree:");
        for (Edge edge : minSpanningTree) {
            System.out.println(edge.src + " - " + edge.dest + " : " + edge.weight);
        }
    }
}

