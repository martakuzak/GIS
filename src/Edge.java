
public class Edge {

	private Vertex v1; //nie wiem, czy jest sens trzymac wierzcholkow jako atrybuty, moze po prostu ich id
						//chyba to lekka nadmiarowosc, ale zobaczymy, jak to wyjdzie w praniu
	private Vertex v2;
	private int id;
	private int color;
	
	public Edge(Vertex v1, Vertex v2, int id) {
		this.v1 = v1;
		this.v2 = v2;
		this.id = id;
		color = -1;
	}
	
	public void setColor(int color) {
		this.color = color;
	}
	public int getV1Id() {
		return v1.getId();
	}
	public int getV2Id() {
		return v2.getId();
	}
}
