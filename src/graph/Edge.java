package graph;

/**
 * Little data structure for graph edge
 */
class Edge {
    int destination, weight;
    Edge next;

    Edge(int dst, int w) {
        destination = dst;
        weight = w;
    }
}
