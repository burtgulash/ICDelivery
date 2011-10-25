package graph;

public class Path {
	int vertex, weight;
	Path rest;
	

	public Path(int v, int w, Path rest) {
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
	 * Accessor for rest of the path
	 */
	public Path rest() {
		return rest;
	}

	public String toString() {
		String res = "(" + this.vertex + ", " + this.weight + ")";
		for (Path iter = this.rest; iter != null; iter = iter.rest)
			res += ", (" + iter.vertex + ", " + iter.weight + ")";
		return res;
	}
	

	private static Path prepend(int v, int w, Path p) {
		return new Path(v, w + p.weight, p);
	}

	public static Path concat(Path p1, Path p2) {
		if (p1 == null)
			return p2;

		int restWeight = 0;
		if (p1.rest != null)
			restWeight = p1.rest.weight;
		return prepend(p1.vertex, p1.weight - restWeight, concat(p1.rest, p2));
	}
}
