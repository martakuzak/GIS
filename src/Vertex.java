import java.util.ArrayList;

public class Vertex {

	private ArrayList<Vertex> adjacencyList;
	private int id;
	/**
	 * 
	 * @param id
	 */
	public Vertex(int id) {
		adjacencyList = new ArrayList<>();
		this.id = id;
	}
	public boolean addNeigbour (Vertex v) {
		return adjacencyList.add(v);
	}
	public int getDegree() {
		return adjacencyList.size();
	}
	public int getId() {
		return id;
	}
}
