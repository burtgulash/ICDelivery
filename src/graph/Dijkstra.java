package graph;

import java.util.Map;
import java.util.TreeMap;

import priorityQueue.Queable;
import priorityQueue.PriorityQueue;


/**
 * Class Dijkstra
 *
 * Implementation of Dijkstra's shortest paths algorithm.
 * Stores previously computed shortest-paths trees as there are lots of
 * repeating requests for shortest paths in the simulation.
 */
public class Dijkstra implements ShortestPaths {
    // number of remembered trees
    // the higher the higher memory consumption 
    // but faster shortestPath retrieval
    private final int MEMO_SIZE  = 10;
    private final int INF        = Integer.MAX_VALUE;
    private final int NIL        = -1;


    // result tree represented as map(vertex -> previous vertex)
    private PriorityQueue<NotSeenPath> queue;
    private Graph graph;
    
    // cursor is current position in memo to save to
    private int cursor;
    // shortest-path trees are stored in memo
    private int[][] memo;
    private int[] memoVertex;
    
    // optimization: compute shortest-path tree for depot vertex as it will
    // be used most
    private int[] depot_memo;
    private final int DEPOT;



    /**
     * Constructs implementation of Dijkstra's algorithm.
     *
     * @param graph A graph to compute shortest paths from.
     * @param depotVertex compute shortest-path tree for depot vertex only once
     */
    public Dijkstra(Graph graph, int depotVertex) {
        this.graph  = graph;
        DEPOT       = depotVertex;
        cursor      = 0;
        memo        = new int[MEMO_SIZE][];
        memoVertex  = new int[MEMO_SIZE];

        for (int i = 0; i < MEMO_SIZE; i++)
            memoVertex[i] = NIL;

        depot_memo = computeShortestPathTree(DEPOT);
    }


    /**
     * Initialize priority queue and previous-tree for given source vertex
     *
     * @param src source vertex
     * @return NILed out shortest-path tree
     */
    private int[] init(int src) {
        int[] previous = new int[graph.vertices()];
        queue = new PriorityQueue<NotSeenPath>();

        int v = graph.vertices();
        for (int i = 0; i < v; i++) {
            queue.insert(new NotSeenPath(i, INF));
            previous[i] = NIL;
        }

        for (Edge iter = graph.v[src].next; iter != null; iter = iter.next) {
            queue.changePriority(iter.destination, iter.weight);
            previous[iter.destination] = src;
        }

        return previous;
    }


    /**
     * Constructs shortest path from computed shortest-paths tree
     *
     * @param src source vertex of shortestPath
     * @param dst destination vertex of shortestPath
     * @param prev shortest-path tree
     * @return Shortest path from src to dst.
     */
    private Path traceBack(int src, int dst, int[] prev) {
        Path p = null;
        int oneBack;
        
        while (dst != src) {
            oneBack = prev[dst];
            assert(oneBack != NIL);

            p = Path.concat(new Path(dst, graph.cost(oneBack, dst), null), p);
            dst = oneBack;
        }
        return p;
    }


    /**
     * Check if we have memoized previous-tree for given source vertex
     *
     * @param src source vertex
     * @return Memoized shortest-path tree represented as previous-map
     */
    private int[] inMemo(int src) {
        assert(src != NIL);

        for (int i = 0; i < MEMO_SIZE; i++)
            if (memoVertex[i] == src)
                return memo[i];
        return null;
    }



    /**
     * Dijkstra's algorithm, runs in O(V logV), fails if graph not connected
     *
     * @param src compute shortest-paths tree for this vertex
     * @return Shortest-paths tree.
     */
    private int[] computeShortestPathTree(int src) {
        // initialize queue and prev-tree
        int[] previous = init(src);

        // Compute whole tree and store to memo
        NotSeenPath current;
        while (!queue.empty()) {
            current        = queue.extractMin();
            int curVertex  = current.dstVertex;
            int curWeight  = current.bestWeight;
            assert(curWeight != INF); // else the graph was not connected

            // decrease key for all neighbors
            for (Edge n = graph.v[curVertex].next; n != null; n = n.next) {
                int newWeight = curWeight + n.weight;
                int next = n.destination;

                // benchmark this conditional later
                if (newWeight < queue.priority(next)) {
                    queue.changePriority(next, curWeight + n.weight);
                    previous[next] = curVertex;
                }
            }
        }
        
         return previous;
    }


    @Override
    /**
     * Computes shortest path between given vertices.
     *
     * @param src source vertex of the path
     * @param dst destination vertex of the path
     * @return shortest path from src to dst.
     */
    public Path shortestPath(int src, int dst) {
        // Path from self to self is known immediately
        if (src == dst)
            return new Path(dst, 0, null);

        // if path goes from DEPOT, we already have tree precomputed
        if (src == DEPOT)
            return traceBack(src, dst, depot_memo);

        // Check if path already precomputed
        int[] memoized = inMemo(src);
        if (memoized != null)
            return traceBack(src, dst, memoized);

        // if not, compute it
        int[] previous = computeShortestPathTree(src);

        // and store to memo
        cursor = (cursor + 1) % MEMO_SIZE;
        memo[cursor] = previous;
        memoVertex[cursor] = src;


        return traceBack(src, dst, previous);
    }





    /**
     * Helper data structure, represents path from src to dstVertex
     * with current best path-weight
     */
    private class NotSeenPath implements Queable {
        int dstVertex;
        int bestWeight;

        private NotSeenPath(int dst, int w) {
            dstVertex  = dst;
            bestWeight = w;
        }
        
        @Override
        public int id() {
            return dstVertex;
        }

        @Override
        public int priority() {
            return bestWeight;
        }

        @Override
        public void setPriority(int newPriority) {
            bestWeight = newPriority;
        }
    }
}
