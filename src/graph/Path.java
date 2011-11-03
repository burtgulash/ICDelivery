package graph;


/**
 * Path data structure intended to be used as output of ShortestPaths interface
 * 
 * Last Path element is destination vertex. Weight is always Path weight from
 * previous vertex (not seen by instance of Path) to destination vertex.
 * First vertex is never seen, must be remembered by user of this 
 * data structure.
 *
 * Internal implementation is lisp-like list, with rest field being cdr of Path.
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
     *
     * @param v vertex of this Path element
     * @param w weight from vertex before to this Path element
     * @param rest rest of the Path to final destination
     */
    Path(int v, int w, Path rest) {
        vertex = v;
        weight = w;
        this.rest = rest;
    }


    /**
     * Computes length of the Path to destination vertex.
     *
     * @return Length of whole Path.
     */
    public int pathLength() {
        return weight;
    }


    /**
     * Computes distance to this vertex.
     *
     * @return Distance to current vertex from previous(not seen) vertex.
     */
    public int distanceToNext() {
        if (rest == null)
            return weight;
        return weight - rest.weight;
    }


    /**
     * Returns town/vertex this path element is pointing to.
     *
     * @return town/vertex of this path element.
     */
    public int to() {
        return vertex;
    }


    /**
     * Accessor for rest of the path.
     *
     * @return Rest of the path.
     */
    public Path rest() {
        return rest;
    }


    /**
     * Reverses Path.
     *
     * @param src source vertex must be provided since Path data structure 
     *            does not store first (src) vertex
      * @param p path to reverse
     * @return Reversed path.
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


    /**
     * Prepends new Path element to given Path.
     *
     * @param v vertex of new Path element
     * @param w weight of new Path element
     * @param p rest of the Path
     * @return Concatenation of (v, w) and Path p.
     */
    private static Path prepend(int v, int w, Path p) {
        int restWeight = 0;
        if (p != null)
            restWeight = p.weight;
        return new Path(v, w + restWeight, p);
    }


    /**
     * Concatenates two given Path data structures together.
     *
     * @param p1 Path that will be starting subPath
     * @param p2 Path that will be ending subPath
     * @return Concatenation p1 + p2.
     */
    static Path concat(Path p1, Path p2) {
        if (p1 == null)
            return p2;

        int restWeight = 0;
        if (p1.rest != null)
            restWeight = p1.rest.weight;
        return prepend(p1.vertex, p1.weight - restWeight, concat(p1.rest, p2));
    }



    // remove
    @Override
    public  String toString() {
        String res = "PATH: " + vertex + "";
        for (Path iter = rest; iter != null; iter = iter.rest)
            res += ", " + iter.vertex;
        return res;
    }
}
