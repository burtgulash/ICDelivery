package graph;

/**
 * Optimized Floyd-Warshall implementation of ShortestPaths interface
 * Does not operate on Path data structures, the complexity is truly Theta(n^3)
 * and not O(n^4) as in naive implementation.
 */
public class OptimizedFloydWarshall implements ShortestPaths {
	private final int NO_INTERMEDIATE = -1;

	// intermediate vertex half-matrix
	private int[][] kMat;
	// weight half-matrix
	private int[][] wMat;

	public OptimizedFloydWarshall(Graph graph) {
		initialize(graph);
		allPairsShortestPaths();
	}

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
			}
		}
	}
	

	// Floyd-Warshall O(n^3) algorithm loop optimized
	// Performs about 2.5x faster than without loop optimizations
	private void allPairsShortestPaths() {
		int v = wMat.length;
		int half;
		int alternate;
		int k, i, j;

		int [] kIth, wIth, wKth;

		for (k = 0; k < v; k++) {
			for (i = 0; i < k; i++) {
				wIth = wMat[i];
				wKth = wMat[k];
				kIth = kMat[i];
				half = wKth[i];

				for (j = 0; j <= i; j++) {
					alternate = half + wKth[j];
					if (alternate < wIth[j]) {
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
					if (alternate < wIth[j]) {
						wIth[j] = alternate;
						kIth[j] = k;
					}
				}
				for (; j <= i; j++) {
					alternate = half + wMat[j][k];
					if (alternate < wIth[j]) {
						wIth[j] = alternate;
						kIth[j] = k;
					}
				}
			}
		}
	}

	// Add weights to non-weighted Path
	private void addWeights(int src, Path p) {
		if (p == null)
			return;
		int restWeight = 0;
		if (p.rest != null)
			restWeight = p.rest.weight;

		addWeights(p.vertex, p.rest);
		p.weight = wMat[src][p.vertex] + restWeight;
	}

	@Override
	public Path shortestPath(int src, int dst) {
		int v = wMat.length;
		assert(0 <= src && src < v);
		assert(0 <= dst && dst < v);


		// find min and max indices and find the values in lower triangles
		// of matrices
		int min = Math.min(src, dst);
		int max = Math.max(src, dst);
		int intermediate = kMat[max][min];
		int weight = wMat[max][min];

		if (weight == Integer.MAX_VALUE)
			return null;
		if (intermediate == NO_INTERMEDIATE)
			return new Path(dst, weight, null);

		
		// Construct the Path as Path(i -> k) + k + Path(k -> j).
		// At this stage we don't know the last vertex of Path(i -> k)
		// that's why we add the weights later.
		Path resultPath = Path.concat(shortestPath(src, intermediate), 
							Path.concat(new Path(intermediate, 0, null), 
								shortestPath(intermediate, dst)));

		// add weights to zero-weighted resultPath
		addWeights(src, resultPath);
		return resultPath;
	}
}
