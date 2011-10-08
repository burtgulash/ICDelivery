public class Path {
	int vertex, weight;
	Path rest;
	
	public Path(int v, Path rest) {
		vertex = v;
		this.rest = rest;
	}
}
