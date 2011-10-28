package graph;


/**
 * Path data structure intended to be used as output of ShortestPaths interface
 * 
 * Last Path element is destination vertex, weight is always Path weight from
 * previous vertex (not seen by instance of Path) to destination vertex.
 * First vertex is never seen, must be remembered by user of this 
 * data structure.
 *
 * Internal implementation is lisp-like list, with rest field being cdr of Path
 */
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
     * return length of the Path from src(not in path) to end of path
     */
    public int pathLength() {
        return weight;
    }


	/**
	 * Returns distance to next(that is to 'vertex') town
	 */
	public int distanceToNext() {
		if (rest == null)
			return weight;
		return weight - rest.weight;
	}

    /**
     * next town vertex
     */
    public int to() {
        return vertex;
    }


    /**
     * Accessor for rest of the path
     */
    public Path rest() {
        return rest;
    }


	/**
	 * Reverses Path
	 *
	 * src vertex must be provided since Path does not store first (src) vertex
	 */
	public static Path reversed(int src, Path p) {
		Path rev = null;
		int restWeight;
		while (p != null) {
			if (p.rest != null)
				restWeight = p.rest.weight;
			else
				restWeight = 0;
			rev = prepend(src, p.weight - restWeight, rev);
			src = p.vertex;
			p = p.rest;
		}
		return rev;
	}


    // prepend new Path element with vertex v and weight w to Path p
    private static Path prepend(int v, int w, Path p) {
		int restWeight = 0;
		if (p != null)
			restWeight = p.weight;
        return new Path(v, w + restWeight, p);
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
