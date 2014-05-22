import java.util.ArrayList;
import java.util.HashMap;

public class Vertex {

	private ArrayList<Vertex> adjacencyList;
	private ArrayList<Edge> incidentEdges;
	private HashMap<Integer, Boolean> missingColors; 
	private int id;

	/**
	 * 
	 * @param id
	 */
	public Vertex(int id) {
		adjacencyList = new ArrayList<>();
		incidentEdges = new ArrayList<>();
		missingColors = new HashMap<>();
		this.id = id;
	}

	public boolean addNeigbour(Vertex v, Edge e) {
		return adjacencyList.add(v) && incidentEdges.add(e);
	}

	public int getDegree() {
		return adjacencyList.size();
	}

	public int getId() {
		return id;
	}

	public void removeMissingColor(int color) {
		missingColors.put(color, true);
	}

	public void addMissingColor(int color) {
		missingColors.remove(color);
	}

	public Boolean missingColor(int color) {
		if (missingColors.get(color) == null)
			return true;
		return false;
	}


}
