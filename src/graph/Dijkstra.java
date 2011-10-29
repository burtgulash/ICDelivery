package graph;

import java.util.Map;
import java.util.TreeMap;

import priorityQueue.Queable;
import priorityQueue.PriorityQueue;


public class Dijkstra implements ShortestPaths {
    // number of remembered trees
    // the higher the higher memory consumption 
    // but faster shortestPath retrieval
    private final int MEMO_SIZE  = 10;
    private final int INF        = Integer.MAX_VALUE;
    private final int NIL        = -1;


    // result tree represented as map(vertex -> previous vertex)
    private int[] previous;
    private PriorityQueue<NotSeenPath> queue;
    private Graph graph;
    
    // cursor is current position in memo to save to
    private int cursor;
    private int[][] memo;
    private int[] memoVertex;


    public Dijkstra(Graph graph) {
        this.graph  = graph;
        cursor      = 0;
        memo        = new int[MEMO_SIZE][];
        memoVertex  = new int[MEMO_SIZE];

        for (int i = 0; i < MEMO_SIZE; i++)
            memoVertex[i] = NIL;
    }

    // Initialize Priority Queue and previous-tree for given source vertex
    private void init(int src) {
        queue = new PriorityQueue<NotSeenPath>();
        previous = new int[graph.vertices()];

        int v = graph.vertices();
        for (int i = 0; i < v; i++) {
            queue.insert(new NotSeenPath(i, INF));
            previous[i] = NIL;
        }

        for (Edge iter = graph.v[src].next; iter != null; iter = iter.next) {
            queue.changePriority(iter.destination, iter.weight);
            previous[iter.destination] = src;
        }
    }


    /**
     * Backtracks path from src to dst in 'previous' tree
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
     * Check if we have memoized previous-tree for given src vertex
     * @return memoized tree
     */
    private int[] inMemo(int src) {
        assert(src != NIL);

        for (int i = 0; i < MEMO_SIZE; i++)
            if (memoVertex[i] == src)
                return memo[i];
        return null;
    }



    @Override
    public Path shortestPath(int src, int dst) {
        // Path from self to self is known immediately
        if (src == dst)
            return new Path(dst, 0, null);

        // Check if path already precomputed
        int[] memoized = inMemo(src);
        if (memoized != null)
            return traceBack(src, dst, memoized);


        // initialize queue and prev-tree
        init(src);

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

                // benchmark this conditional
                if (newWeight < queue.priority(next)) {
                    queue.changePriority(next, curWeight + n.weight);
                    previous[next] = curVertex;
                }
            }
        }

        // store computed tree to memo
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
