package graph;

/**
 * Little data structure for graph edge
 */
class Edge {
    int destination, weight;
    Edge next;

    /**
     * Create edge struct.
     * Source vertex must be remembered.
     *
     * @param dst destination vertex
     * @param w weight of the edge
     */
    Edge(int dst, int w) {
        destination = dst;
        weight = w;
    }
}
