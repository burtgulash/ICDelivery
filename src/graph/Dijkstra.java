package graph;

import java.util.Map;
import java.util.TreeMap;

import priorityQueue.Queable;
import priorityQueue.PriorityQueue;


public class Dijkstra implements ShortestPaths {
	private final int INF = Integer.MAX_VALUE;

	private Map<Integer, Integer> prev;
	private PriorityQueue<NotSeen> queue;
	private UndirectedGraph graph;
	

	public Dijkstra(Graph graph) {
		this.graph = UndirectedGraph.make(graph);
	}

	private void init(int srcVertex) {
		queue = new PriorityQueue<NotSeen>();
		prev = new TreeMap<Integer, Integer>();

		int v = graph.vertices();
		for (int i = 0; i < v; i++) {
			Edge iter;
			if (i == srcVertex)
				for (iter = graph.v[i].next; iter != null; iter = iter.next)
					queue.insert(new NotSeen(iter.destination, iter.weight));
			else 
				for (iter = graph.v[i].next; iter != null; iter = iter.next)
					queue.insert(new NotSeen(iter.destination, INF));
		}
	}

	private Path traceBack(int src, int dst) {
		Path p = null;
		int oneBack;
		
		while (dst != src) {
			oneBack = prev.get(dst);
			p = Path.concat(new Path(dst, graph.cost(oneBack, dst), null), p);
			dst = oneBack;
		}
		return p;
	}

	@Override
	public Path shortestPath(int src, int dst) {
		init(src);

		Edge iter;
		int drawn, next, best;

		NotSeen seen;
		while (!queue.empty()) {
			seen   = queue.extractMin();
			drawn  = seen.dstVertex;
			best   = seen.bestWeight;
			assert(best != INF); // else the graph was not connected

			// decrease key for all neighbors
			for (iter = graph.v[drawn].next; iter != null; iter = iter.next) {
				int newWeight = best + iter.weight;
				next = iter.destination;

				// benchmark this conditional
				if (newWeight < queue.priority(next)) {
					queue.changePriority(next, best + iter.weight);
					prev.put(next, drawn);
				}
			}
			
		}

		return traceBack(src, dst);
	}



	private class NotSeen implements Queable {
		int dstVertex;
		int bestWeight;

		private NotSeen(int dst, int w) {
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
