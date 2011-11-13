package graph;

/**
 * Interface for shortest paths in graph.
 * Graph is provided in constuctor in different implementations.
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
