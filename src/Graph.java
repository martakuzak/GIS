import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;

public class Graph {
	private ArrayList<Vertex> vertices;
	private ArrayList<Edge> edges;
	private int colorNum; // liczba zuzytych kolorow
	private int degree;
/**
 * 
 */
	public Graph() {
		vertices = new ArrayList<>();
		edges = new ArrayList<>();
		colorNum = 0;
		degree = 0;
	}
/**
 * Dodaje wierzcholki: 0, 1, 2, ..., vnumber - 1
 * @param vnumber liczba wierzcholkow
 */
	public void addVertices(int vnumber) {
		for (int i = 0; i < vnumber; ++i)
			vertices.add(new Vertex(i));
	}
/**
 * 
 * @param v1 jeden koniec krawedzi
 * @param v2 drugi koniec krawedzi
 * @param id id krawedzi
 */
	public void addEdge(int v1, int v2, int id) {
		if (v1 < vertices.size() && v2 < vertices.size() && v1 != v2) {
			Edge e = new Edge(vertices.get(v1), vertices.get(v2), id);
			edges.add(e);
			vertices.get(v1).addNeigbour(vertices.get(v2), e);
			vertices.get(v2).addNeigbour(vertices.get(v1), e);

		}
	}
/**
 * 
 * @return stopien grafu
 */
	public int getDegree() {
		if (degree == 0) {
			for (Vertex v : vertices) {
				if (v.getDegree() > degree)
					degree = v.getDegree();
			}
		}
		return degree;
	}
	public int getVerticesSize() {
		return this.vertices.size();
	}
public ArrayList<Vertex> getVertices() {
		return vertices;
	}
	public void setVertices(ArrayList<Vertex> vertices) {
		this.vertices = vertices;
	}
	public ArrayList<Edge> getEdges() {
		return edges;
	}
	public void setEdges(ArrayList<Edge> edges) {
		this.edges = edges;
	}
	public int getColorNum() {
		return colorNum;
	}
	public void setColorNum(int colorNum) {
		this.colorNum = colorNum;
	}
	public void setDegree(int degree) {
		this.degree = degree;
	}
/**
 * Algorytm NC
 */
	public void colorNC() {
		long lStartTime = System.nanoTime();
		int degree = getDegree(); //O(|V|)
		Collections.shuffle(edges); // ustawienie krawedzi w losowej kolejnosci,
									// O(|E|)
		for (Edge e : edges) { //|E| razy
			int tmp = e.setMinCol(2 * degree - 1);
			if (colorNum > 0) {
				if (tmp < 0)  {//O(2*degree - 2 ) 
					System.err.println("Przekroczono limit kolorow");
					break;
				}
				
				else if (tmp >= colorNum )
					++ colorNum;
			}
			else
				e.setColor(colorNum ++);
		}
		long lEndTime = System.nanoTime();
		long difference = lEndTime - lStartTime;
		System.out.println("Elapsed nanoseconds: " + difference);

	}
/**
 * Algorytm NTL
 */
	public void colorNTL() {
		
	}
	
	

}
