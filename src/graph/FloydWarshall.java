package graph;

/**
 * Optimized Floyd-Warshall implementation of ShortestPaths interface
 * Does not operate on Path data structures, the complexity is truly Theta(n^3)
 * and not O(n^4) as in naive implementation.
 */
public class FloydWarshall implements ShortestPaths {
    private final int NO_INTERMEDIATE = -1;

    // intermediate vertex half-matrix
    private int[][] kMat;
    // weight half-matrix
    private int[][] wMat;

    public FloydWarshall(Graph graph) {
        initialize(graph);
        allPairsShortestPaths();
    }

	// Create weight half-matrix and half-matrix storing decisions of k vertex
    private void initialize(Graph graph) {
        int v = graph.vertices();
        kMat = new int[v][];
        wMat = new int[v][];

        for (int i = 0; i < v; i++) {
            kMat[i] = new int[i + 1];
            wMat[i] = new int[i + 1];
            for (int j = 0; j <= i; j++) {
                // intermediate vertex initialized to None
                kMat[i][j] = NO_INTERMEDIATE;
                // vertices i and j adjacent? w_0 = a[i, j]
                wMat[i][j] = graph.cost(i, j);

                // Path cost from self to self is free
                if (j == i)
                    wMat[i][j] = 0;
            }
        }
    }
    

    // Floyd-Warshall O(n^3) algorithm loop optimized
    // Performs about 2.5x faster than without loop optimizations
    private void allPairsShortestPaths() {
        int v = wMat.length;

        // weight of path (i -> k)
        int half;
        // weight of path (i -> k) + path (k -> j)
        int alternate;
        int k, i, j;

        // save matrix arrays that get used a lot
        int [] kIth, wIth, wKth;

        for (k = 0; k < v; k++) {
            for (i = 0; i < k; i++) {
                wIth = wMat[i];
                wKth = wMat[k];
                kIth = kMat[i];
                half = wKth[i];

                for (j = 0; j <= i; j++) {
                    alternate = half + wKth[j];
                    // check for overflow first
                    if (alternate >= half && alternate < wIth[j]) {
                        wIth[j] = alternate;
                        kIth[j] = k;
                    }
                }
            }
            for (; i < v; i++) {
                wIth = wMat[i];
                wKth = wMat[k];
                kIth = kMat[i];
                half = wIth[k];

                for (j = 0; j < k; j++) {
                    alternate = half + wKth[j];
                    if (alternate >= half && alternate < wIth[j]) {
                        wIth[j] = alternate;
                        kIth[j] = k;
                    }
                }
                for (; j <= i; j++) {
                    alternate = half + wMat[j][k];
                    if (alternate >= half && alternate < wIth[j]) {
                        wIth[j] = alternate;
                        kIth[j] = k;
                    }
                }
            }
        }
    }


    @Override
    public Path shortestPath(int src, int dst) {
        int v = wMat.length;
        assert(0 <= src && src < v);
        assert(0 <= dst && dst < v);

        // if shortestPath not in lower triangle of matrix
        // return reversed Path of reverse query
        if (src < dst)
            return Path.reversed(dst, shortestPath(dst, src));

        int intermediate = kMat[src][dst];
        int weight       = wMat[src][dst];

        // Path does not exist
        if (weight == Integer.MAX_VALUE)
            return null;

        // Path from src to dst is direct
        if (intermediate == NO_INTERMEDIATE)
            return new Path(dst, weight, null);

        
        // Construct the Path as Path(i -> k) + Path(k -> j).
        Path src_to_k = shortestPath(src, intermediate);
        Path k_to_dst = shortestPath(intermediate, dst);

        // if one part of Path is null, Path does not exist
        if (src_to_k == null || k_to_dst == null)
            return null;

        // Concatenate both parts together and return
        return Path.concat(src_to_k, k_to_dst);
    }
}
