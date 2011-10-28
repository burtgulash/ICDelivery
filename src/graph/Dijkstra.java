package graph;

import java.util.Map;
import java.util.TreeMap;

import priorityQueue.Queable;
import priorityQueue.PriorityQueue;


public class Dijkstra implements ShortestPaths {
    private final int INF = Integer.MAX_VALUE;

    private Map<Integer, Integer> prev;
    private PriorityQueue<NotSeenPath> queue;
    private Graph graph;
    

    public Dijkstra(Graph graph) {
        this.graph = graph;
    }

    // Initialize Priority Queue and prev-tree for given source vertex
    private void init(int src) {
        queue = new PriorityQueue<NotSeenPath>();
        prev = new TreeMap<Integer, Integer>();

        int v = graph.vertices();
        for (int i = 0; i < v; i++)
            queue.insert(new NotSeenPath(i, INF));

        for (Edge iter = graph.v[src].next; iter != null; iter = iter.next) {
            queue.changePriority(iter.destination, iter.weight);
            prev.put(iter.destination, src);
        }
    }


    @Override
    public Path shortestPath(int src, int dst) {
        if (src == dst)
            return new Path(dst, 0, null);

        init(src);

        // neighbor iterator
        Edge n;
        int curVertex, curWeight, next;

        NotSeenPath current;
        while (!queue.empty()) {
            current    = queue.extractMin();
            curVertex  = current.dstVertex;
            curWeight  = current.bestWeight;
            assert(curWeight != INF); // else the graph was not connected

			// if not using caching, keep this
			if (curVertex == dst)
				break;

            // decrease key for all neighbors
            for (n = graph.v[curVertex].next; n != null; n = n.next) {
                int newWeight = curWeight + n.weight;
                next = n.destination;

                // benchmark this conditional
                if (newWeight < queue.priority(next)) {
                    queue.changePriority(next, curWeight + n.weight);
                    prev.put(next, curVertex);
                }
            }
            
        }

        return traceBack(src, dst);
    }


    /**
     * Backtraces path from src to dst in 'prev' tree
     */
    private Path traceBack(int src, int dst) {
        Path p = null;
        int oneBack;
        
        while (dst != src) {
            assert(prev.get(dst) != null);
            oneBack = prev.get(dst);
            p = Path.concat(new Path(dst, graph.cost(oneBack, dst), null), p);
            dst = oneBack;
        }
        return p;
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
