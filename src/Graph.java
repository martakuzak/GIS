import java.util.ArrayList;

public class Graph {
	private ArrayList<Vertex> vertices;
	private ArrayList<Edge> edges;
	
	public Graph() {
		vertices = new ArrayList<>();
		edges = new ArrayList<>();
		
	}
	
	public void addVertices(int vnumber) {
		for(int i = 0; i < vnumber; ++i)
			vertices.add(new Vertex(i));
	}
	public void addEdge(int v1, int v2, int id) {
		if(v1 < vertices.size() && v2 < vertices.size()) {//jesli graf ma wierzcholki tej krawedzi 
			Edge e = new Edge(vertices.get(v1), vertices.get(v2), id);
			edges.add(e);
		}
	}
	public int getDegree() {
		int maxVertexDegree = 0;
		for(Vertex v : vertices) {
			if(v.getDegree() > maxVertexDegree)
				maxVertexDegree = v.getDegree();
		}
		return maxVertexDegree;
	}
	
	/*public void writeGraph() { //to tak do testow ;)
		System.out.println("Num of vertices " + vertices.size());
		for (Edge e : edges)
			System.out.println(e.getV1Id() + " " + e.getV2Id());
	}*/
}
