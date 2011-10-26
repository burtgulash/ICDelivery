package graph;

public class Path {
    // alow attributes to be visible for shortest paths algorithms
    int vertex, weight;
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
	 * get current town
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
    // TODO should not be static 
    static Path concat(Path p1, Path p2) {
        if (p1 == null)
            return p2;

        int restWeight = 0;
        if (p1.rest != null)
            restWeight = p1.rest.weight;
        return prepend(p1.vertex, p1.weight - restWeight, concat(p1.rest, p2));
    }
}
