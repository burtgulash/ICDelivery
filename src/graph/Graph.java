package graph;

/**
 * Class graph
 *
 * Stored as two-way directed graph.
 * Vertices are integers in sequence ranging from 0 to N.
 */
public class Graph {
	/** vertex array */
    Edge[] v;
    
    
    /**
     * Constructs graph with specified amount of vertices.
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
	 * @param src source vertex
	 * @param dst destination vertex
	 * @param weight weight of the edge
     */
    void addEdge(int src, int dst, int weight) {
        Edge dummy = v[src];
        Edge inserted = new Edge(dst, weight);
        inserted.next = dummy.next;
        dummy.next = inserted;
    }
    

    /**
     * Number of vertices of the graph.
	 * @return number of vertices.
     */
    public int vertices() {
        return v.length;
    }


    /**
     * Returns weight of an edge or +infinity, if the edge doesn't exist
	 * @param src source vertex of the edge
	 * @param dst destination vertex of the edge
     */
    int cost (int src, int dst) {
        for (Edge iter = v[src].next; iter != null; iter = iter.next)
            if (iter.destination == dst)
                return iter.weight;
        
        // if no edge found, return +INFINITY
        return Integer.MAX_VALUE;
    }
}
