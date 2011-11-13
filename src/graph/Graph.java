package graph;

/**
 * <p>Graph data structure.</p>
 *
 * <p>
 * Stored as two-way directed graph.
 * Vertices are integers in sequence ranging from 0 to N.
 * </p>
 *
 *
 * @author Tomas Marsalek
 */
public class Graph {
    /** vertex array */
    Edge[] v;


    /**
     * Constructs graph with specified amount of vertices.
     *
     * @param vertexCount
     */
    public Graph(int vertexCount) {
        v = new Edge[vertexCount];

        // add dummy edges
        for (int i = 0; i < v.length; i++)
            v[i] = new Edge(i, -1);
    }


    /**
     * Adds an edge into the graph.
     *
     * @param src source vertex of added edge
     * @param dst destination vertex of added edge
     * @param weight weight of this edge
     */
    void addEdge(int src, int dst, int weight) {
        Edge dummy = v[src];
        Edge inserted = new Edge(dst, weight);
        inserted.next = dummy.next;
        dummy.next = inserted;
    }


    /**
     * Number of vertices of the graph.
     *
     * @return number of vertices.
     */
    public int vertices() {
        return v.length;
    }


    /**
     * Gets weight of an edge.
     *
     * @param src source vertex of the edge
     * @param dst destination vertex of the edge
     * @return Weight of the edge between src and dst or +Inf, in the edge
     *         does not exist.
     */
    int cost (int src, int dst) {
        for (Edge iter = v[src].next; iter != null; iter = iter.next)
            if (iter.destination == dst)
                return iter.weight;

        // if no edge found, return +INFINITY
        return Integer.MAX_VALUE;
    }
}
