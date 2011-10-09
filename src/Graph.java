public class Graph {
	private Edge[][] v;
	
	public Graph(int vertices) {
		for (int i = 0; i < vertices; i++)
			v[i] = new Edge[0];
	}
	

	public int vertices() {
		return v.length;
	}


	public Path edge(int src, int dst) {
		for (int i = 0; i < src; i++)
			if (v[src][i].destination == dst)
				return new Path(dst, v[src][i].weight, null);
		return null;
	}
}

class Edge {
	int destination, weight;
	Edge(int dst, int w) {
		destination = dst;
		weight = w;
	}
}
