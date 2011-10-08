public class Graph {
	private Edge[][] v;
	
	public Graph(int vertices) {
		v = new Edge[vertices][1];
	}
	
	public int vertices() {
		return v.length;
	}
	
	public void addEdge(int start, int dst,int w){
		
		v[start][v[start].length] = new Edge(dst,w);
	}
	
	public void addEdge(int start, Edge e){
		v[start][v[start].length-1] = e;
	}
}

class Edge {
	int destination, weight;
	Edge(int dst, int w) {
		destination = dst;
		weight = w;
	}
	
	public String toString(){
		return destination + " " + weight;
	}
}
