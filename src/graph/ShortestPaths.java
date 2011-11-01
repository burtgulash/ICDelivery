package graph;

/**
 * Interface for shortest paths of graph.
 */
public interface ShortestPaths {
    /**
     * Computes shortest path from source to destination vertex.
     *
     * @param src source vertex
     * @param dst destination vertex
     */
    public Path shortestPath(int src, int dst);
}
