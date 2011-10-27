package graph;

public class Graph {
	private Edge[][] v;
	
	
	/**
	 *  Constructs graph given Edge matrix
	 */
	public Graph(Edge[][] v) {
		this.v = v;
	}
	

	/**
	 * Number of vertices of graph
	 */
	public int vertices() {
		return v.length;
	}

	// testing purposes
	public int neighbors(int vertex) {
		if (0 <= vertex && vertex < vertices()) {
			assert (v[vertex] != null);
			return v[vertex].length;
		}
		return -1;
	}

	/**
	 * Returns weight from src to dst or +inf, if no edge
	 */
	int cost (int src, int dst) {
		assert(v[src] != null);
		int srcListLen = v[src].length;
		for (int i = 0; i < srcListLen; i++) {
			assert(v[src][i] != null);
			if (v[src][i].destination == dst)
				return v[src][i].weight;
		}

		// else try the edge in other direction
		assert(v[dst] != null);
		srcListLen = v[dst].length;
		for (int i = 0; i < srcListLen; i++) {
			assert(v[dst][i] != null);
			if (v[dst][i].destination == src)
				return v[dst][i].weight;
		}
		return Integer.MAX_VALUE;
	}
}
