package graph;

/**
 * Class graph
 *
 * Stored as unidirectional graph but responds to queries as if it was
 * bidirectional.
 */
public class Graph {
    Edge[] v;
    
    
    /**
     *  Constructs graph with numVertices vertices
     */
    public Graph(int numVertices) {
        v = new Edge[numVertices];

        // add dummy edges
        for (int i = 0; i < v.length; i++)
            v[i] = new Edge(i, -1);
    }
    
    
    /**
     * Adds edge from src to dst with given weight
     */
    void addEdge(int src, int dst, int weight) {
        Edge dummy = v[src];
        Edge inserted = new Edge(dst, weight);
        inserted.next = dummy.next;
        dummy.next = inserted;
    }
    

    /**
     * Number of vertices of graph
     */
    public int vertices() {
        return v.length;
    }


    /**
     * Returns weight from src to dst or +inf, if no edge
     */
    int cost (int src, int dst) {
        for (Edge iter = v[src].next; iter != null; iter = iter.next)
            if (iter.destination == dst)
                return iter.weight;
        
        // if no edge found, return +INFINITY
        return Integer.MAX_VALUE;
    }
}
