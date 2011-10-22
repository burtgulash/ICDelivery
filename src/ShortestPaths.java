package graph;

/**
 * Interface for shortest paths of graph
 * Implemented by Floyd-Warshal
 */
public interface ShortestPaths {
	/**
	 * Returns shortest path from src to dst as Path object
	 */
	public Path shortestPath(int src, int dst);
}
