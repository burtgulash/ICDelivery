package graph;

import java.util.LinkedList;

/**
 * Undirected graph extension of Graph
 *
 * Used solely by Dijkstra's algorithm
 * neighbors() method is O(MAXDEGREE) vs O(|E|) in regular Graph
 */
class UndirectedGraph extends Graph {
	UndirectedGraph(int numVertices) {
		super(numVertices);
	}


	static UndirectedGraph make(Graph g) {
		int numVertices = g.vertices();
		UndirectedGraph ug = new UndirectedGraph(numVertices);

		for (int i = 0; i < numVertices; i++)
			for (Edge iter = g.v[i].next; iter != null; iter = iter.next) {
				// insert edge in both directions
				ug.addEdge(i, iter.destination, iter.weight);
				ug.addEdge(iter.destination, i, iter.weight);
			}

		return ug;
	}
}
