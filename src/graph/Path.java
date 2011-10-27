package graph;

public class Path {
    // alow attributes to be visible for shortest paths algorithms
	// vertex is NEXT vertex in Path
    int vertex;
	// Path weight to NEXT vertex, that is to this.vertex
	// first vertex of Path is stored implicitly
	int weight;
	// rest of the path
    Path rest;
    

    /**
     * Path constructor, only used in graph algorithms
     */
    Path(int v, int w, Path rest) {
        vertex = v;
        weight = w;
        this.rest = rest;
    }


    /**
     * return length of the Path
     */
    public int pathLength() {
        return weight;
    }


    /**
     * get //TODO(next) current town
     */
    public int town() {
        return vertex;
    }


    /**
     * Accessor for rest of the path
     */
    public Path rest() {
        return rest;
    }


    // prepend new Path element with vertex v and weight w to Path p
    private static Path prepend(int v, int w, Path p) {
        return new Path(v, w + p.weight, p);
    }

    // Concatenate two paths together
    static Path concat(Path p1, Path p2) {
        if (p1 == null)
            return p2;

        int restWeight = 0;
        if (p1.rest != null)
            restWeight = p1.rest.weight;
        return prepend(p1.vertex, p1.weight - restWeight, concat(p1.rest, p2));
    }
}
