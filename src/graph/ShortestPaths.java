package graph;

/**
 * <p>Interface for shortest paths in graph.</p>
 *
 * <p>Graph is provided in constuctor in different implementations.</p>
 *
 * @author Tomas Marsalek
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
