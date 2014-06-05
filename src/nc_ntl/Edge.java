package nc_ntl;


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
	 * Ustaw najnizszy mozliwy kolor mniejszy od max. 
	 * @param max 
	 * @return zwraca ustawiony kolor lub -1, jezli nie udalo sie nadac koloru
	 */
		public int setMinCol(int max) {
			// zmieniam minCol na 1 z 0
			int minCol = 1; 
			// zmieniam na <=max z <max
			while (minCol <= max) {
				if(v1.missingColor(minCol) && v2.missingColor(minCol)) {
					//System.out.println(" if minCol "+minCol);
					setColor(minCol);
					v1.removeMissingColor(minCol);
					v2.removeMissingColor(minCol);
					return minCol;
				}
				++ minCol;
				//System.out.println("minCol iteracja "+minCol);
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

	// musze dostawac sie do wierzcholkow a nie tylk do ich numerow...
	public Vertex getV1() {
		return this.v1;
	}
	public Vertex getV2() {
		return this.v2;
	}
	
	public int getId() {
		return id;
	}
	
	//ustaw brakujace kolory dla obu wierzcholkow siezki
	public void setActualMissingColor(int degree)
	{
		int colorTmp=0;
		// ewentualna zmiana ustalonego brakujacego koloru wierzcholka V1
		if (v1.getMissingColor()==color)
		{
			//System.out.println("Brakujacy wierzch "+v1.getId()+" to "+v1.getMissingColor());
			colorTmp = color;
			while(!v1.missingColor(colorTmp))
			{
				colorTmp++;
				// pytanie czy kolory numerujemy od zera??
				if (colorTmp == degree+2) colorTmp=1;
			}
			v1.setMissingColor(colorTmp);
			//System.out.println("Brakujacy wierzch "+v1.getId()+" to "+v1.getMissingColor());
		}
		// ewentualna zmiana brakujacego koloru wierzcholka V2
		if (v2.getMissingColor()==color)
		{
			//System.out.println("Brakujacy wierzch "+v2.getId()+" to "+v2.getMissingColor());
			colorTmp = color;
			while(!v2.missingColor(colorTmp))
			{
				colorTmp++;
				// pytanie czy kolory numerujemy od zera??
				if (colorTmp == degree+2) colorTmp=1;
			}
			v2.setMissingColor(colorTmp);
			//System.out.println("Brakujacy wierzch "+v2.getId()+" to "+v2.getMissingColor());
		}
	}

}