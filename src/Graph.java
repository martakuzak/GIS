import java.util.ArrayList;
import java.util.Collections;

public class Graph {
	private ArrayList<Vertex> vertices;
	private ArrayList<Edge> edges;
	private int colorNum; //liczba zuzytych kolorow
	
	public Graph() {
		vertices = new ArrayList<>();
		edges = new ArrayList<>();
		colorNum = 0;
	}
	
	public void addVertices(int vnumber) {
		for(int i = 0; i < vnumber; ++i)
			vertices.add(new Vertex(i));
	}
	public void addEdge(int v1, int v2, int id) {
		if(v1 < vertices.size() && v2 < vertices.size() && v1 != v2) {//jesli graf ma wierzcholki tej krawedzi , i nie jest to petla
			Edge e = new Edge(vertices.get(v1), vertices.get(v2), id);
			edges.add(e);
			vertices.get(v1).addNeigbour(vertices.get(v2), e);
			vertices.get(v2).addNeigbour(vertices.get(v1), e);

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
	
	public void colorNC() {
		int degree = getDegree();
		for(Vertex v : vertices)
			v.initMissingColors(2*degree - 1);
		Collections.shuffle(edges); //ustawienie krawedzi w losowej kolejnosci
		for(Edge e : edges) {
			if(colorNum > 0) {
				if(e.setMinCol(2*degree - 1) > 0)
					++ colorNum;
			}
			else
				e.setColor(colorNum ++);
		}
			
	}
	public void writeGraph() { //to tak do testow ;)
		colorNC();
		System.out.println("Num of vertices " + vertices.size());
		for (Edge e : edges)
			System.out.println(e.getV1Id() + " " + e.getV2Id() + " " + e.getColor());
	}
}
