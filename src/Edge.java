
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
	/**
	 * Ustaw najni�szy mo�liwy kolor mniejszy od max. 
	 * @param max 
	 * @return zwraca ustawiony kolor lub -1, je�li nie uda�o si� nada� koloru
	 */
		public int setMinCol(int max) {
			int minCol = 0; 
			while (minCol < max) {
				if(v1.missingColor(minCol) && v2.missingColor(minCol)) {
					setColor(minCol);
					v1.removeMissingColor(minCol);
					v2.removeMissingColor(minCol);
					return minCol;
				}
				++ minCol;
			}
			return -1;
		}
		
	public void setColor(int color) {
		this.color = color;
	}
	public int getColor() {
		return this.color;
	}
	public int getV1Id() {
		return v1.getId();
	}
	public int getV2Id() {
		return v2.getId();
	}
	
	public int getId() {
		return id;
	}

}
