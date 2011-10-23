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


	/**
	 * Returns edge from src to dst as Path object of length 1
	 *  or null if the edge is not present
	 */
	public Path edge(int src, int dst) {
		assert(v[src] != null);
		int srcListLen = v[src].length;
		for (int i = 0; i < srcListLen; i++) {
			assert(v[src][i] != null);
			if (v[src][i].destination == dst)
				return new Path(dst, v[src][i].weight, null);
		}
		return null;
	}
}

