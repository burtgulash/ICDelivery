package graph;


public class FloydWarshall implements ShortestPaths {
	private Path[][] mat;


	/**
	 * Public constructor for ShortestPaths for graph g
	 */
	public FloydWarshall(Graph g) {
		initMat(g);
		allPairsShortestPaths();
	}

	
	// Initialize Matrix 
	//     mat[i][j] = null      if no edge exists, 
	//               = w[i][j]   if edge exists
	private void initMat(Graph g) {
		int v = g.vertices();
		mat = new Path[v][v];

		for (int i = 0; i < v; i++)
			for (int j = 0; j < v; j++)
				mat[i][j] = g.edge(i, j);

		// introduce self-loops
		// path from self to self is 0 in length
		for (int i = 0; i < v; i++)
			mat[i][i] = new Path(i, 0, null);
	}
	
	// Floyd-Warshall O(V^3) algorithm for all pair shortest paths.
	private void allPairsShortestPaths() {
		int v = mat.length;
		for (int k = 0; k < v; k ++)
			for (int i = 0; i < v; i++)
				for (int j = 0; j < v; j++) {
					if (mat[i][k] == null || mat[k][j] == null)
						continue;
					int alternatePath = mat[i][k].weight + mat[k][j].weight;
					if (mat[i][j] == null || alternatePath < mat[i][j].weight) {
						mat[i][j] = Path.concat(mat[i][k], mat[k][j]);
					}
				}
	}


	@Override
	/**
	 * Returns shortest path from src to dst or null if the path does not exist;
	 */
	public Path shortestPath(int src, int dst) {
		return mat[src][dst];
	}
}
