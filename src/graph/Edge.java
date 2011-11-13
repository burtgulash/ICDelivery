package graph;

/**
 * <p>Little data structure for graph edge.</p>
 *
 * @author Tomas Marsalek
 */
class Edge {
    int destination, weight;
    Edge next;


    /**
     * Create edge struct.
     * Source vertex must be remembered, it is not stored in this object.
     *
     * @param dst destination vertex of this edge
     * @param w weight of this edge
     */
    Edge(int dst, int w) {
        destination = dst;
        weight = w;
    }
}
