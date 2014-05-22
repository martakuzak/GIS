import java.util.ArrayList;
import java.util.HashMap;

public class Vertex {

	private ArrayList<Vertex> adjacencyList;
	private ArrayList<Edge> incidentEdges;
	private HashMap<Integer,Boolean> missingColors; //hashmapa, bo jej operacje put, get, remove itp maja zlozonosc O(1)
	private int id;
	/**
	 * 
	 * @param id
	 */
	public Vertex(int id) {
		adjacencyList = new ArrayList<>();
		incidentEdges = new ArrayList<>();
		//missingColors = null;
		this.id = id;
	}
	public boolean addNeigbour (Vertex v, Edge e) {
		return adjacencyList.add(v) && incidentEdges.add(e);
	}
	public int getDegree() {
		return adjacencyList.size();
	}
	public int getId() {
		return id;
	}
	/*!public boolean missingColor(int index) {
		return missingColors[index];
	}
	public void setMissingColor(int index, boolean value) {
		missingColors[index] = value;
	}*/
	public void removeMissingColor(int color) {
		missingColors.remove(color);
	}
	public void addMissingColor(int color) {
		missingColors.put(color, true);
	}
	public Boolean missingColor(int color) {
		if(missingColors.get(color) == null)
			return false;
		return true;
	}
	/**
	 * 
	 * @param num zalozona maksymalna liczba kolorow w grafie
	 */
	public void initMissingColors(int num) {
		missingColors = new HashMap<>();
		for(int i = 0; i < num; ++i)
			missingColors.put(i, true);
	}
}
