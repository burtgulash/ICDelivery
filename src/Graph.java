public class Graph {
	private Edge[][] v;
	
	public Graph(int vertices) {
		for (int i = 0; i < vertices; i++)
			v[i] = new Edge[0];
	}
	
	public int vertices() {
		return v.length;
	}
}

class Edge {
	int destination, weight;
	Edge(int dst, int w) {
		destination = dst;
		weight = w;
	}
}
