package nc_ntl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.TreeSet;

public class Graph {
	private ArrayList<Vertex> vertices;
	private ArrayList<Edge> edges;
	private int colorNum; // liczba zuzytych kolorow
	private int degree;
	private ArrayList<ArrayList<Integer>> neighbourhoodMatrix; //macierz sasiedztwa
/**
 * 
 */
	public Graph() {
		vertices = new ArrayList<>();
		edges = new ArrayList<>();
		colorNum = 0;
		degree = 0;
		neighbourhoodMatrix = new ArrayList<ArrayList<Integer>>();
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
	//pobieranie macierzy sasiedztwa
	public ArrayList<ArrayList<Integer>> getNeighbourhoodMatrix() {
	return this.neighbourhoodMatrix;
	}
/**
 * Algorytm NC 
 * @return ile nanosekund trwalo wykonywanie algorytmu
 */
	public long colorNC() {
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
				e.setColor(++ colorNum); //M: zmieni�am, �eby na pocz�tku nadawany by� kolor 1, a nie 0
		}
		long lEndTime = System.nanoTime();
		long difference = lEndTime - lStartTime;
		//System.out.println("Elapsed nanoseconds: " + difference);
		test();

		return difference;

	}
/**
 * Algorytm NTL
 * @return ile nanosekund trwalo wykonywanie algorytmu
 */
	public long colorNTL() {

		long lStartTime = System.nanoTime();
		int degree = getDegree(); //O(|V|)
		//Collections.shuffle(edges); // ustawienie krawedzi w losowej kolejnosci,
									// O(|E|)
			if(degree<=2)
			{
				colorNC();			
			}
			else
			{
				int q = degree+1;
										// O(|E|)
				for (Edge e : edges) { //|E| razy
					
					//System.out.println("Sciezka nr "+e.getId()+" od "+e.getV1Id()+" do "+e.getV2Id());
					
					int kolor = e.setMinCol(q);//O(2*degree - 2 ) 
					if (kolor<0)
					{
						Recolor(e.getV1(), e.getV2()); // wywolanie procedury Recolor
					}
					else
					{
						e.setColor(kolor);
					}
					
					// wypelnianie macierzy sasiedztwa faktycznymi wartosciami
					neighbourhoodMatrix.get(e.getV1Id()).set(e.getV2Id(), e.getColor());
					neighbourhoodMatrix.get(e.getV2Id()).set(e.getV1Id(), e.getColor());
					
					//wypisywanie macierzy sasiedztwa dla testow
					/*for(int i=0; i<vertices.size();i++)
						for(int j=0; j<vertices.size();j++)
							{
								if(e.getV1().getId()==11 && e.getV2().getId()==13) {
								//if(neighbourhoodMatrix.get(6).get(17)!=neighbourhoodMatrix.get(6).get(15))
									System.out.print(neighbourhoodMatrix.get(i).get(j)+"\t");
								if(j==vertices.size()-1) System.out.println();
								}
							}*/
					/*for(Edge ex: edges)
							{
								//System.out.print(ex.getId()+":("+ex.getV1Id()+","+ex.getV2Id()+")-k:"+ex.getColor()+"; ");
							}*/
//					//System.out.println();
							
					e.setActualMissingColor(degree);
					e.getV1().removeMissingColor(e.getColor());//uaktualniania missingColor
					e.getV2().removeMissingColor(e.getColor());//uaktualniania missingColor
					e.getV1().setActualMissingColor(degree);
					e.getV2().setActualMissingColor(degree);
					//test2();
				}
				
			}

		long lEndTime = System.nanoTime();
		long difference = lEndTime - lStartTime;
//		//System.out.println("Elapsed nanoseconds: " + difference);
		
		//sprawdzenie, jaki jest najwiekszy kolor
		colorNum = 0;
		for (Edge e : edges) {
			if (e.getColor() > colorNum)
				colorNum = e.getColor();
		}
		
		if( !test() ) 
			return -1;

		return difference;
	}
	/**
	 * Algorytm Recolor - przekolorowywanie krawedzi w Algorytmie NTL
	 * @return ile nanosekund trwalo wykonywanie algorytmu
	 */	
	public void Recolor(Vertex v1, Vertex v2) {
		
		
		System.out.println("Recolor "+v1.getId()+" "+v2.getId());

		// Biore macierz sasiedztwa i kolumne dotyczaca v1.	
		// z kazdym wierzcholkiem kojarzymy mozliwie najmniejszy kolor brakujacy
		
	//1. Tworzymy wachlarza dla krawedzi V1-V2	
		ArrayList<Integer> FanV1 = new ArrayList<>();
	//2. Wachlarz rozpoczyna sie od wierzcholka v2
		FanV1.add(v2.getId());
	//3. Tworzymy HashMape w celu sprawdzenia czy dany wierzcholek nalezy juz do wachlarza (zeby nie duplikowac)
		HashMap<Integer, Boolean> FanV1Tmp = new HashMap<>(); //najpierw dodaje do TreeSetu, a potem przepisuje do listy
															//TreeSety nie dodaja duplikujacych sie elementow
	//4. Do HashMapy dodajemy pierwszy wierzcholek wachlarza V2
		FanV1Tmp.put(v2.getId(), true);
	//5. Definiujemy kolor alfa, jako kolor brakujacy wierzcholka V1
		int alfa = v1.getMissingColor();
	//6. szukamy wierzcholka ktory jest polaczony z V1 krawedzia o kolorze, ktory jest rowny kolorowi brakujacemu wierzcholka V2
		Vertex tmpVertex=v2;
		//System.out.println("Wachlarz. dodaje " + tmpVertex.getId() + " " + tmpVertex.getMissingColor());
		boolean ifEnd = false;
		int zliczanie=0;
//t		//System.out.println("kolor brak v2: "+tmpVertex.getMissingColor());
	//7. W petli szukamy kolejnych wierzcholkow, ktore sa polaczone z V1 krawedzia, o kolorze brakujacym poprzedniego wierzcholka wachlarza
		/*System.out.println("v1 m = " + alfa + " v2 m = " + v2.getMissingColor());
		/*System.out.println("Adjacency colors  " + v1.getId());
		ArrayList<Vertex > tmpList = v1.getAdjacencyList();
		for (Vertex v : tmpList) {
			System.out.println(v.getId() + "\t" + v1.getEdge(v.getId()).getColor() + "\t");
		}
		System.out.println("A w macierzy...");
		ArrayList <Integer> tmp2List = neighbourhoodMatrix.get(v1.getId());
		for (int i = 0; i < vertices.size(); ++i) {
			System.out.println(i + "\t" + tmp2List.get(i));
		}*/
		while(!ifEnd && zliczanie<edges.size())
		{
//t			System.out.println("tmpVertex: "+tmpVertex.getId());
			ifEnd=true;
	//8. Przechodzimy po wszystkich sasiadach V1....
			//for(int i=0; i< vertices.size(); i++)
			for(Vertex v : v1.getAdjacencyList())
			{
//t				////System.out.println("Fan? "+neighbourhoodMatrix.get(v1.getId()).get(i)+" =? "+tmpVertex.getMissingColor()+" missing "+Fan.get(i));
	//9. Jezeli sasiad V1 jest z nim polaczony kolorem brakujacym poprzedniego poprzedniego wierzchlka wachlarza... 
				
				//if(neighbourhoodMatrix.get(v1.getId()).get(i)==tmpVertex.getMissingColor() )
				if(v1.getEdge(v.getId()).getColor() == tmpVertex.getMissingColor())
				{
					int i = v.getId();
	//10. I je�eli dany wierzcholek nie nalezy jeszcze do wachlarza...
					if(FanV1Tmp.get(v.getId()) == null ) {
	//11. To zmieniamy tmpVertex...
						tmpVertex=vertices.get(i);	

						//System.out.println("dodaje " + i + " o kolorze brak. " + tmpVertex.getMissingColor());
	//12. Oraz dodajemy do wachlarza oraz HashMapy ten wierzcholek
						FanV1.add(i);
						FanV1Tmp.put(i, true);
						ifEnd=false;
						break;
					}
				}
			}
	//13. Poprzez zliczanie kontrolujemy czy nasz wachlarz nie sklada sie z wiekszej liczby krawedzi niz istnieje w grafie
			zliczanie++;
		}
		
		//System.out.println("Fan size " + FanV1.size()); //Fan size nie moze byc rowne 1, bo skoro nie mozna
										//dac wspolnego koloru v1 i v2, to jakas krawedz incydenta do v1 ma
										//kolor brakujacy v2 
	//14. Wachlarz zostal skonstruowany. TmpVertex oznacza teraz ostatni wierzcholek wachlarza -Xs
//t		//System.out.println("Wachlarz skonstruowany.");
	//15. Definiujemy kolor beta jako kolor brakujacy ostatniego wierzcholka wachlarz - Xs
		int beta = tmpVertex.getMissingColor();
		//System.out.println("alfa="+alfa+" ; beta="+beta);
		
	//16. Jezeli kolor brakujacy Xs jest rowniez kolorem brakujacym V1, to mozemy przesunac wachlarz, a V1-XS pokolorowac kolorem beta
		if(v1.missingColor(beta))
		{
	//17. Przesuwamy wachlarz: teraz krawedz v1-v2 jest pokolorowane, a krawedz v1-xs(tmpVertex) nie
			for (int i=0; i<FanV1.size()-1; i++)
			{
	//18. W macierzach sasiedztwa zmieniamy kolory krawedzi (przesuniecie wachlarza)	
				neighbourhoodMatrix.get(v1.getId()).set(FanV1.get(i), neighbourhoodMatrix.get(v1.getId()).get(FanV1.get(i+1)));
				neighbourhoodMatrix.get(FanV1.get(i)).set(v1.getId(), neighbourhoodMatrix.get(v1.getId()).get(FanV1.get(i+1)));
//t				//System.out.println(FanV1.get(i) + " " + neighbourhoodMatrix.get(v1.getId()).get(FanV1.get(i+1)));
//t				//System.out.println(v1.getId() + " " + neighbourhoodMatrix.get(v1.getId()).get(FanV1.get(i+1)));
	//19. Dodajemy wierzcholkom wachlarz nowy kolor brakujacy (ten, ktory jeszcze chwilowo, przed przesunieciem, okresla krawedz, ktora przesuwamy)
				vertices.get(FanV1.get(i)).addMissingColor(v1.getEdge(FanV1.get(i)).getColor());
	//20. Zmieniamy kolor krawdzi od V1 do aktualnego wierzcholka wachlarza, na kolor nastepnej krawedzi	
				v1.getEdge(FanV1.get(i)).setColor(neighbourhoodMatrix.get(v1.getId()).get(FanV1.get(i+1)));
	//21. Usuwamy niepotrzebny kolor brakujacy wierzcholka wachlarza, ktory przesunelismy
				vertices.get(FanV1.get(i)).removeMissingColor(v1.getEdge(FanV1.get(i)).getColor());
//t				//System.out.println("Zmieniam kolor krawedzi v1, v2 na "+neighbourhoodMatrix.get(v1.getId()).get(FanV1.get(i+1)));
			}
	//22. ...Ustalamy kolor krawedzi miedzy v1 a xs(tmpVertex) na 0 (czyli usuwamy ta krawedz)
			v1.getEdge(tmpVertex.getId()).setColor(0);
	//23. Na podstawie macierzy sasiedztwa (jeszcze nie aktualnej) dodajemy odpowiedni kolor brakujacy do xs (tmpVertex) - spowodowane jest to przesunieciem wachlarza
			tmpVertex.addMissingColor(neighbourhoodMatrix.get(v1.getId()).get(tmpVertex.getId()));
	//24. Dla v1 i wszystkich wiercholkow nalezacych do wachlarza aktualizujemy kolory brakujace
			v1.setActualMissingColor(degree);
			for (int i=0; i<FanV1.size(); i++)
			{
				vertices.get(FanV1.get(i)).setActualMissingColor(degree);
				v1.getEdge(FanV1.get(i)).setActualMissingColor(degree);
			}	
	// 25. Poniewaz beta jest kolorem brakujacym V1 i XS, mozemy pokolorowac krawedz x1-xs kolorem beta
	// 26. Aktualizujemy macierz sasiedztwa
			neighbourhoodMatrix.get(v1.getId()).set(tmpVertex.getId(), beta);
			neighbourhoodMatrix.get(tmpVertex.getId()).set(v1.getId(), beta);
			v1.getEdge(tmpVertex.getId()).setColor(beta);
	// 27. Dodajemy beta do kolorow brakujacych V1 i XS (tmpVertex)
			v1.removeMissingColor(beta);//!!
			tmpVertex.removeMissingColor(beta);
			v1.setActualMissingColor(degree);
			tmpVertex.setActualMissingColor(degree);

// to bez sensu! nie kolorujemy v1->v2 kolorem beta! juz ja pokolorowalismy przesuwajac wachlarz...

//			for (Edge e: edges)
//			{
//				if((e.getV1Id()==v1.getId() && e.getV2Id()==tmpVertex.getId()) || (e.getV2Id()==v1.getId() && e.getV1Id()==tmpVertex.getId()))
//				{
////0					//System.out.println("id v1:"+v1.getId()+" id tmpVertex: "+tmpVertex.getId());
//					e.getV1().removeMissingColor(beta);
//					e.getV2().removeMissingColor(beta);
//					e.setColor(beta);
////0					//System.out.println("pokolorowalem krawedz "+e.getV1Id()+","+e.getV2Id());
//					e.getV1().setActualMissingColor(degree);
//					e.getV2().setActualMissingColor(degree);
////0					//System.out.println("Zmieniam kolor krawedzi v1, xs na "+beta);
//					break;
//				}
//			}

		}
	// 28. Jezeli beta (kolor brakujacy XS (tmpVertex) nie jest kolorem brakujacym V1,  
		else
		{
	// 29. Tworzymy sciezke o poczatku w XS i skladajaca sie z krawedzi na przemian kolorow ALFA i BETA
			ArrayList<Integer> Path = new ArrayList<Integer>();
	// 30. Dodajemy pierwszy wezel sciazki XS (tmpVertex)
			Path.add(tmpVertex.getId());
	// 31. beta jest kolorem brakujacym XS, a zatem sciezka nie moze zaczynac sie od BETA. Zatem sciezka, jezeli istnieje, zacyzna sie od ALFA
	// 32. Jako tmptmpVertex oznaczamy wierzcholek, nalezacy do sciezki, ktory aktualnie przetwarzamy (na start: XS czyli tmpVertex)
			Vertex tmptmpVertex = tmpVertex;
	// 33. Zmienna licznik pomaga nam kontrolowac, czy nie przegladamy wiecej sciezek niz jest w grafie
			int licznik=0;
			boolean koniec=false;
	// 34. Zmienna parzystosc, pozwala kontrolowac czy nastepna krawedz powinna byc pokolorowana kolorem ALFA czy BETA
			int parzystosc=0;
	// 35. W petli szukamy kolejnych krawedzi sciezki Path. W zaleznosci szukamy krawedzi koloru ALFA lub BETA
			while(licznik<edges.size() && !koniec)
			{
				koniec=true;
	// 36. Jezeli jestesmy w kroku parzystym (zaczynamy od 0) szukamy krawedzi koloru ALFA (pierwszy krok musi byc koloru ALFA, bo BETA jest kolorem brakujacym XS)
				if(parzystosc%2==0)
				{
	// 37. Dla wszystkich wierzcholkow sprawdzamy czy....
					for(int i=0; i<vertices.size(); i++)
					{
	// 38. ...sa poloczone z koncem dotychczasowej sciezki kolorem alfa.
						if(neighbourhoodMatrix.get(tmptmpVertex.getId()).get(i)==alfa && Path.indexOf(i)<0)
						{
							//System.out.println("3. Dodajemy do sciezki krawedz miedzy "+tmptmpVertex.getId()+" a "+vertices.get(i).getId()+" koloru "+alfa);
	// 39. Jezeli tak, dodajemy kolejny wierzcholek do sciezki i zmieniamy tmptmpVertex na dodany wierzcholek
							tmptmpVertex = vertices.get(i);
							Path.add(i);
							koniec=false;
							break;
						}
					}
				}
	// 40. Jezeli jestesmy w kroku nieparzystym, szukamy krawedzi koloru BETA
				else
				{
	// 41. Dla wszystkich wierzcholkow sprawdzamy czy....
					for(int i=0; i<vertices.size(); i++)
					{
	// 42. ...sa poloczone z koncem dotychczasowej sciezki kolorem beta.
						if(neighbourhoodMatrix.get(tmptmpVertex.getId()).get(i)==beta && Path.indexOf(i)<0)
						{
						//System.out.println("4. Dodajemy do sciezki krawedz miedzy "+tmptmpVertex.getId()+" a "+vertices.get(i).getId()+" koloru "+beta);
	// 43. Jezeli tak, dodajemy kolejny wierzcholek do sciezki i zmieniamy tmptmpVertex na dodany wierzcholek
							tmptmpVertex = vertices.get(i);
							Path.add(i);
							koniec=false;
							break;
						}
					}	
				}
	// 44. Przed przejsciem doszukania koljnego wierzcholka sciezki zmieniamy parzystosc.
				parzystosc++;
				licznik++;
			}
			
	// 45. Sciezka Path zostala skonstrowana. Sciezka moze nie istniec.
//t			//System.out.println("Sciezka skonstruowana.");
			
	// 46. Jezeli sciezka nie PRZECINA v1.... //nie mo�e przecina�, bo alfa jest kolorem brakujacym v1...
//t		if(Path.get(Path.size()-1) !=v1.getId())//
			//System.out.println("Sciezka konczy sie w: "+vertices.get(Path.get(Path.size()-1)).getId()+" jej brak.kolor to "+vertices.get(Path.get(Path.size()-1)).getMissingColor());
			//System.out.println("oraz "+FanV1.indexOf(vertices.get(Path.get(Path.size()-1)).getId()));
			
			if(Path.get(Path.size() - 1) != v1.getId()/* && !(vertices.get(Path.get(Path.size()-1)).getMissingColor()==beta && FanV1.indexOf(vertices.get(Path.get(Path.size()-1)).getId())>=0)*/)//
			{
				//System.out.println("Sciezka NIE konczy sie w V1.");
				// przesuniecie wachlarza
				// byla niepokolorowane pierwsza krawedz v1,v2
				// teraz niepokolorowana jest ostatnia czyli v1,tmpVertex
//t				//System.out.println("Rozmiar wachlarza: "+FanV1.size());
//t				//System.out.println("Przed przesunieciem wachlarza:");
				//wypisywanie macierzy sasiedztwa dla testow
				/*if(v1.getId()==30 && v2.getId()==44)
				{for(int m=0; m<vertices.size();m++)
					for(int j=0; j<vertices.size();j++)
						{
//t							//System.out.print(neighbourhoodMatrix.get(m).get(j)+"\t");
//t							if(j==vertices.size()-1)//System.out.println();
						}
				}*/
				
	// 47. Przesuwamy wachlarz, teraz v1->v2 jest pokolorowana, a v1->xs (tmpVertex) nie				
				for (int i=0; i<FanV1.size()-1; i++)
				{
	// 48. Zmieniamy kolory sciezek w macierzysasiedztwa
					neighbourhoodMatrix.get(v1.getId()).set(FanV1.get(i), neighbourhoodMatrix.get(v1.getId()).get(FanV1.get(i+1)));
					neighbourhoodMatrix.get(FanV1.get(i)).set(v1.getId(), neighbourhoodMatrix.get(v1.getId()).get(FanV1.get(i+1)));
//t					//System.out.println(FanV1.get(i) + " " + neighbourhoodMatrix.get(v1.getId()).get(FanV1.get(i+1)));
//t					//System.out.println(v1.getId() + " " + neighbourhoodMatrix.get(v1.getId()).get(FanV1.get(i+1)));
	// 49. Zmieniamy kolory odpowiednich sciezek i dodajemy/usuwamy kolory brakujace
					vertices.get(FanV1.get(i)).addMissingColor(v1.getEdge(FanV1.get(i)).getColor());		
					v1.getEdge(FanV1.get(i)).setColor(neighbourhoodMatrix.get(v1.getId()).get(FanV1.get(i+1)));
					vertices.get(FanV1.get(i)).removeMissingColor(v1.getEdge(FanV1.get(i)).getColor());
//t					//System.out.println("Zmieniam kolor krawedzi v1, v2 na "+neighbourhoodMatrix.get(v1.getId()).get(FanV1.get(i+1)));					
				}
	// 50. ...Ustalamy kolor krawedzi miedzy v1 a xs(tmpVertex) na 0 (czyli usuwamy ta krawedz)
				v1.getEdge(tmpVertex.getId()).setColor(0);
	// 51. Dodajemy kolor brakujacy tmpVertex (jeszcze nie zmienilismy macierzy sasiedztwa, wiec mozemy w ten sposob)
				tmpVertex.addMissingColor(neighbourhoodMatrix.get(tmpVertex.getId()).get(v1.getId()));
	// 52. Dla v1 i wszystkich wiercholkow nalezacych do wachlarza aktualizujemy kolory brakujace
				v1.setActualMissingColor(degree);
				for (int i=0; i<FanV1.size(); i++)
				{
					vertices.get(FanV1.get(i)).setActualMissingColor(degree);
					v1.getEdge(FanV1.get(i)).setActualMissingColor(degree);
				}
	// 53. Aktualizujemy macierz sasiedztwa
				neighbourhoodMatrix.get(v1.getId()).set(tmpVertex.getId(), 0);
				neighbourhoodMatrix.get(tmpVertex.getId()).set(v1.getId(), 0);
	// 54. Zakonczylismy przesuwanie wachlarza.
				//System.out.println("Po przesunieciu wachlarza:");
//tt
//				if(v1.getId()==15 && v2.getId()==16)
//				{for(int m=0; m<vertices.size();m++)
//					for(int j=0; j<vertices.size();j++)
//						{
//							//System.out.print(neighbourhoodMatrix.get(m).get(j)+"\t");
//							if(j==vertices.size()-1)//System.out.println();
//						}
//				}
//tt
	// 55. Odwracamy kolory utworzonej sciezki Path
				for(int i=0;i<Path.size()-1;i++)
				{
	// 56. Jezeli dana krawedz sciezki jest koloru alfa, przekolorowywujemy ja na kolor beta
					if(neighbourhoodMatrix.get(Path.get(i)).get(Path.get(i+1))==alfa)
					{
						//System.out.println("Przekolorowujemy krawedz " + Path.get(i) + " " + Path.get(i + 1) + " na kolor " + beta);
						neighbourhoodMatrix.get(Path.get(i)).set(Path.get(i+1), beta);
						neighbourhoodMatrix.get(Path.get(i+1)).set(Path.get(i), beta);
						vertices.get(Path.get(i)).getEdge(Path.get(i + 1)).setColor(beta);
	// 57. Dla pierwszego wierzcholka sciezki musimy uaktualnic kolory brakujace: dodac kolor alfa, usunac kolor beta
						if(i==0 )
						{
							//System.out.println("alfa i = 0 " );
							vertices.get(Path.get(i)).addMissingColor(alfa);//uaktualniania missingColor
							vertices.get(Path.get(i)).removeMissingColor(beta);//uaktualniania missingColor
							vertices.get(Path.get(i)).setActualMissingColor(degree);//nowe
						}
	// 58. Jezeli bierzemy ostatni wierzcholek, rowniez musimy uaktualnic kolory brakujace
						if (i==(Path.size()- 2) ) {
							vertices.get(Path.get(i + 1)).addMissingColor(alfa);//uaktualniania missingColor
							vertices.get(Path.get(i + 1)).removeMissingColor(beta);//uaktualniania missingColor
							vertices.get(Path.get(i + 1)).setActualMissingColor(degree);//nowe
						}
	// 59. Jezeli dana krawedz sciezki jest koloru beta, przekolorowywujemy ja na kolor alfa	
					}
					else {
						//System.out.println("Przekolorowujemy krawedz " + Path.get(i) + " " + Path.get(i + 1) + " na kolor " + alfa);
						neighbourhoodMatrix.get(Path.get(i)).set(Path.get(i+1), alfa);
						neighbourhoodMatrix.get(Path.get(i+1)).set(Path.get(i), alfa);
						vertices.get(Path.get(i)).getEdge(Path.get(i + 1)).setColor(alfa);
	// 60. Dla pierwszego wierzcholka sciezki musimy uaktualnic kolory brakujace: dodac kolor alfa, usunac kolor beta
	// nie powinno sie to NIGDY pojawic, bo pierwsza krawedz sciezki powinna byc koloru ALFA 
						if(i==0 )
						{
							//System.out.println("beta i = 0 lub koniec sciezki");
							vertices.get(Path.get(i )).addMissingColor(beta);//uaktualniania missingColor
							vertices.get(Path.get(i)).removeMissingColor(alfa);//uaktualniania missingColor
							vertices.get(Path.get(i)).setActualMissingColor(degree);//nowe
						}
	// 61. Jezeli bierzemy ostatni wierzcholek, rowniez musimy uaktualnic kolory brakujace
						if (i==(Path.size()- 2) ) {
							vertices.get(Path.get(i + 1)).addMissingColor(beta);//uaktualniania missingColor
							vertices.get(Path.get(i + 1)).removeMissingColor(alfa);//uaktualniania missingColor
							//vertices.get(Path.get(i)).setActualMissingColor(degree);//nowe
							
						}
					}
				}
	

				
	// 62. Obrocilismy sciezke.
	// 63. Teraz mozemy pokolorowac v1->xs(tmpVertex). alfa jest kolorem brakujacym v1,
	//     a XS albo nie mial krawedzi koloru alfa, albo przekolorowalismy ja na beta obracajac sciezke
//t				//System.out.println("v1: "+v1.getId()+" tmpVertex: "+tmpVertex.getId() + " alfa: " + alfa);
	// 64. Kolorujac v1->tmpVertex kolorem alfa zmieniamy macierz sasiedztwa
				neighbourhoodMatrix.get(v1.getId()).set(tmpVertex.getId(), alfa);
				neighbourhoodMatrix.get(tmpVertex.getId()).set(v1.getId(), alfa);
	// 65. Kolorujemy v1->tmpVertex kolorem alfa
				v1.getEdge(tmpVertex.getId()).setColor(alfa);
	// 66. Aktualizujemy kolory brakujace v1, i XS (tmpVertex)
				v1.removeMissingColor(alfa);//uaktualniania missingColor
				tmpVertex.removeMissingColor(alfa);//uaktualniania missingColor
				v1.setActualMissingColor(degree);//uaktualniania missingColor
				tmpVertex.setActualMissingColor(degree);//uaktualniania missingColor

			}
	// 67. Jezeli sciezka Path PRZECINA v1 //sciezka nie moze przecinac v1, bo alfa jest kolorem brakujacym v1
			else
			{
				//System.out.println("Sciezka konczy sie w V1. V1 to "+v1.getId()+" a koniec sciezki to "+Path.get(Path.size()-1)); 
	// 68. Szukamy wierzcholka nalezacego do wachlarza, ktory ma kolor brakujacy beta.
	//     Musi taki istniec, poniewaz, w jakis sposob sciezka Path dotarla do V1 - tymczasem alfa jest kolorem brakujacym V1,
	//     wiec sciezka Path musiala dotrzec do V1 kolorem BETA, czyli wierzcholek bedacy w wachlarzu PRZED wierzcholkiem
	//     polaczonym z V1 krawedzia BETA, musi miec kolor brakujacy beta.
	// 	   Gdyby krawedz beta nie nalezala do wachlarza, XS ktorego kolorem brakujacym byla BETA, nie bylby ostatnim
	//     wierzcholkiem wachlarza - mo�naby bowiem doda� t� kraw�d� koloru beta.
				
	// 69. Niech xi bedzie wierzcholkiem wachlarza takim ze m(xi)=beta
				Vertex xi = new Vertex(-1);
	// 70. W petli szukamy wierzcholka nalezacego do wachlarza o kolorze brakujacym beta, nie bedacego wierzcholkiem XS (tmpVertex)	
				//jesli sciezka Path konczy sie w v1, to znaczy, ze ostatnia jej krawedz ma kolor beta
				//wypisywanie macierzy sasiedztwa dla testow
				xi = vertices.get(FanV1.get(FanV1.indexOf(Path.get(Path.size() - 2)) - 1));
								
				
				/*for(Vertex v : vertices)
				{
					//if(v.getId()!=v2.getId() && v.getMissingColor()==beta && FanV1.indexOf(v.getId())>0 )//&& v.getId()!=tmpVertex.getId())
					if(v.getMissingColor()==beta && FanV1.indexOf(v.getId())>=0 && v.getId()!=tmpVertex.getId())
						{
							xi = v;
							System.out.println("xi: "+v.getId());
							break;
						}
				}*/
				//System.out.println("xi: "+xi.getId());
	// 71. Konstruujemy sciezke Path2 zaczynajaca sie w xi (ktorego kolor brakujacy to beta) pokolorowana naprzemian kolorami alfa i beta
	//     Beta jest kolorem brakujacym xi, a zatem sciezka z xi musi zaczynac sie kolorem alfa		
				ArrayList<Integer> Path2 = new ArrayList<Integer>();
    // 72. Dodajemy xi do sciezki Path2		
				Path2.add(xi.getId());
//t             //System.out.println("Path2 add: "+xi.getId());
//t				if(v1.getId()==22 && v2.getId()==42) //System.out.println("Path2 add: "+xi.getId());
	// 73. Ustalamy xi wierzcholkiem tymczasowo ostatnim sciezki Path2 - tmptmpVertex		
				tmptmpVertex = xi;
				licznik=0;
				koniec=false;
				parzystosc=0;
				
				while(licznik<edges.size() && !koniec)
				{
					koniec=true;
	// 74. Zaczynamy od liczby parzysej, zatem najpierw szukamy krawedzi o kolorze alfa (bo beta jest brakujacym kolorem xi)
					if(parzystosc%2==0)
					{
						for(int i=0; i<vertices.size(); i++)
						{
							if(neighbourhoodMatrix.get(tmptmpVertex.getId()).get(i)==alfa && Path2.indexOf(i)<0)
							{
	// 75.  Jezeli znajdziemy odpowiednia krawedz dodajemy ja do sciezki i zmieniamy wierzcholek tymczasowy tmptmpVertex
								tmptmpVertex = vertices.get(i);
								Path2.add(i); //Path -> Path2
								////System.out.println("Path2 add: "+i);
								//if(v1.getId()==22 && v2.getId()==42) //System.out.println("Path2 add: "+i);
								koniec=false;
								break;
							}
						}
					}
	// 76. W krokach nieprazystych szukamy krawedzi kolorze beta
					else
					{
						for(int i=0; i<vertices.size(); i++)
						{
							if(neighbourhoodMatrix.get(tmptmpVertex.getId()).get(i)==beta && Path2.indexOf(i)<0)
							{
	// 77.  Jezeli znajdziemy odpowiednia krawedz dodajemy ja do sciezki i zmieniamy wierzcholek tymczasowy tmptmpVertex
								tmptmpVertex = vertices.get(i);
								Path2.add(i);  //Path -> Path2
								////System.out.println("Path2 add: "+i);
								//if(v1.getId()==22 && v2.getId()==42) //System.out.println("Path2 add: "+i);
								koniec=false;
								break;
							}
						}	
					}
					parzystosc++;
					licznik++;
				}
				
//tt				
/*				//System.out.println("Przed przesunieciem wachlarza:");
				//wypisywanie macierzy sasiedztwa dla testow
				if(v1.getId()==22 && v2.getId()==44)
				{for(int m=0; m<vertices.size();m++)
					for(int j=0; j<vertices.size();j++)
						{
							//System.out.print(neighbourhoodMatrix.get(m).get(j)+"\t");
							if(j==vertices.size()-1)//System.out.println();
						}
				}
*/
//tt
				
	// 78. Sciezka Path2 zostala skonstruowana.
	// 79. Przesuwamy wachlarz FanV1, teraz v1->v2 jest pokolorowana, a v1->xi nie
				for (int i=0; i<FanV1.indexOf(xi.getId()); i++)  //bylo: i<FanV1.indexOf(xi.getId()-1) ale to chyba bez sensu
				{
	// 80. Przekolorowujemy krawedzie w macierzy sasiedztwa, reagujac na przesuwanie wachlarza
					neighbourhoodMatrix.get(v1.getId()).set(FanV1.get(i), neighbourhoodMatrix.get(v1.getId()).get(FanV1.get(i+1)));
					neighbourhoodMatrix.get(FanV1.get(i)).set(v1.getId(), neighbourhoodMatrix.get(v1.getId()).get(FanV1.get(i+1)));
//t					//System.out.println(FanV1.get(i) + " " + neighbourhoodMatrix.get(v1.getId()).get(FanV1.get(i+1)));
//t					//System.out.println(v1.getId() + " " + neighbourhoodMatrix.get(v1.getId()).get(FanV1.get(i+1)));
	// 81. Zmieniamy kolory krawedzi przy przesuwaniu wachlarza, aktualizujemy kolory brakujace
					vertices.get(FanV1.get(i)).addMissingColor(v1.getEdge(FanV1.get(i)).getColor());							
					v1.getEdge(FanV1.get(i)).setColor(neighbourhoodMatrix.get(v1.getId()).get(FanV1.get(i+1)));
					vertices.get(FanV1.get(i)).removeMissingColor(v1.getEdge(FanV1.get(i)).getColor());
				}
				
	// 82. Usuwamy krawedz v1->xi	
				v1.getEdge(xi.getId()).setColor(0);
	// 83. Aktualizujemy kolor brakujacy xi, na podstawie macierzy sasiedztwa, ktorej jeszcze nie zmienilismy
				xi.addMissingColor(neighbourhoodMatrix.get(xi.getId()).get(v1.getId()));
	// 84. Aktualizujemy kolory brakujace wierzcholkow wachlarza wlacznie z Xi
				v1.setActualMissingColor(degree);
				for (int i=0; i<FanV1.indexOf(xi.getId()); i++)
				{
					vertices.get(FanV1.get(i)).setActualMissingColor(degree);
					v1.getEdge(FanV1.get(i)).setActualMissingColor(degree);
				}
				
	// 85. Aktualizujemy macierz sasiedztwa (usuwamy krawedz v1->xi)
				neighbourhoodMatrix.get(v1.getId()).set(xi.getId(), 0);
				neighbourhoodMatrix.get(xi.getId()).set(v1.getId(), 0);
			
//tt				
				//System.out.println("Po przesunieciu:");
//				if(v1.getId()==22 && v2.getId()==42)
//				{for(int m=0; m<vertices.size();m++)
//					for(int j=0; j<vertices.size();j++)
//						{
//						//System.out.print(neighbourhoodMatrix.get(m).get(j)+"\t");
//						if(j==vertices.size()-1)//System.out.println();
//						}
//				}
////tt				

	// 86. Odwracamy kolory sciezki Path2 

//t				//System.out.println("PATH2 size " + Path2.size());
//t				for (Integer i : Path2) 
//t					//System.out.println("Path2 " + i);
				
//t				//System.out.println("Kontrola koloru krawedzi 2");
//t				for (Edge e : edges) {
//t					if(e.getColor() > 0)
//t						//System.out.println("Krawedz " + e.getV1Id() + "," + e.getV2Id() + " " + e.getColor());
//t				} 
				
//t				for(int i=0; i<vertices.size();i++)
//t					for(int j=0; j<vertices.size();j++)
//t						{
//t							//System.out.print(neighbourhoodMatrix.get(i).get(j)+"\t");
//t							if(j==vertices.size()-1)//System.out.println();
//t						}
				
	// 87. Przystosc ustawiamy na zero i rozpoczynamy przetwarzanie
				
				parzystosc=0;
				for(int i=0;i<Path2.size()-1;i++) 
				{
	// 88. Jezeli jestesmy w kroku parzystym zmieniamy kolor alfa na beta. Pierwszy krok jest wlasnie taki, bo beta jest kolorem brakujacym xi
					if(parzystosc%2==0)
					{
					//System.out.println("Przekolorowujemy krawedz P2 " + Path2.get(i) + " " + Path2.get(i + 1) + " na kolor " + beta);
	// 89. Zmieniamy kolor sciezki w macierzy sasiedztwa
						neighbourhoodMatrix.get(Path2.get(i)).set(Path2.get(i+1), beta);
						neighbourhoodMatrix.get(Path2.get(i+1)).set(Path2.get(i), beta);
//t                     if(v1.getId()==22 && v2.getId()==42)
//t						//System.out.println("Przekolorowujemy krawedz " + Path2.get(i) + " " + Path2.get(i + 1) + " na kolor " + beta);
	// 90. Zmieniamy kolor sciezki
						vertices.get(Path2.get(i)).getEdge(Path2.get(i + 1)).setColor(beta);
	// 91. Dla pierwszego i ostatniego wierzcholka sciezki musimy aktualizowac kolory brakujace
						if(i==0 )
						{
							//System.out.println("alfa i = 0 " );
							vertices.get(Path2.get(i)).addMissingColor(alfa);//uaktualniania missingColor
							vertices.get(Path2.get(i)).removeMissingColor(beta);//uaktualniania missingColor
							vertices.get(Path2.get(i)).setActualMissingColor(degree);//nowe
						}
						if (i==(Path2.size()- 2) ) {
							vertices.get(Path2.get(i + 1)).addMissingColor(alfa);//uaktualniania missingColor
							//System.out.println("USUWAM");
							vertices.get(Path2.get(i + 1)).removeMissingColor(beta);//uaktualniania missingColor
							vertices.get(Path2.get(i + 1)).setActualMissingColor(degree);//nowe
						}
					}
	// 92. Jezeli jestesmy w kroku nieparzystym zmieniamy krawedz koloru beta na alfa
					else
					{
    // 93. Aktualizujemy macierz sasiedztw
						neighbourhoodMatrix.get(Path2.get(i)).set(Path2.get(i+1), alfa);
						neighbourhoodMatrix.get(Path2.get(i+1)).set(Path2.get(i), alfa);
//t 					if(v1.getId()==22 && v2.getId()==42)
						//System.out.println("Przekolorowujemy krawedz P2" + Path2.get(i) + " " + Path2.get(i + 1) + " na kolor " + alfa);
	// 94. Zmieniamy kolor sciezki
						vertices.get(Path2.get(i)).getEdge(Path2.get(i + 1)).setColor(alfa);
	// 95. Dla pierwszego i ostatniego wierzcholka sciezki musimy aktualizowac kolory brakujace
						if(i==0 )
						{
							//System.out.println("beta i = 0 lub koniec sciezki");
							vertices.get(Path2.get(i )).addMissingColor(beta);//uaktualniania missingColor
							vertices.get(Path2.get(i)).removeMissingColor(alfa);//uaktualniania missingColor
							vertices.get(Path2.get(i)).setActualMissingColor(degree);//nowe
						}
						if (i==(Path2.size()- 2) ) {
							vertices.get(Path2.get(i + 1)).addMissingColor(beta);//uaktualniania missingColor
							vertices.get(Path2.get(i + 1)).removeMissingColor(alfa);//uaktualniania missingColor
							vertices.get(Path2.get(i + 1)).setActualMissingColor(degree);//nowe
						}
					}
					parzystosc++;
				}
//tt				
				//System.out.println("Po odwroceniu:");
//				if(v1.getId()==22 && v2.getId()==42)
//				{for(int m=0; m<vertices.size();m++)
//					for(int j=0; j<vertices.size();j++)
//						{
//						//System.out.print(neighbourhoodMatrix.get(m).get(j)+"\t");
//						if(j==vertices.size()-1)//System.out.println();
//						}
//				}
//tt		
				
//t				//System.out.println("Kontrola koloru krawedzi 3");
//t				for (Edge e : edges) {
//t					if(e.getColor() > 0)
//t						//System.out.println("Krawedz " + e.getV1Id() + "," + e.getV2Id() + " " + e.getColor());
//t				} 
				
//t				for(int i=0; i<vertices.size();i++)
//t					for(int j=0; j<vertices.size();j++)
//t						{
//t							//System.out.print(neighbourhoodMatrix.get(i).get(j)+"\t");
//t							if(j==vertices.size()-1)//System.out.println();
//t						}

	// 96. Kolorujemy sciezke v1->xi kolorem alfa. alfa jest kolorem brakujacym v1, a xi je�eli mial krawedz alfa
	//     to zmienilismy jej kolor na beta odwracajac sciezke

//t				if(v1.getId()==22 && v2.getId()==42)
//t				//System.out.println("v1: "+v1.getId()+" xi: "+xi.getId() + " alfa: " + alfa);
				
	// 97. Aktualizujemy macierz sasiedztwa
				neighbourhoodMatrix.get(v1.getId()).set(xi.getId(), alfa);
				neighbourhoodMatrix.get(xi.getId()).set(v1.getId(), alfa);
	// 98. Dodajemy krawedz	(ktorej wczesniej nie bylo)	
				v1.getEdge(xi.getId()).setColor(alfa);
	// 99. Aktualizujemy kolory brakujace v1 i xi
				v1.removeMissingColor(alfa);//uaktualniania missingColor
				xi.removeMissingColor(alfa);//uaktualniania missingColor
				v1.setActualMissingColor(degree);//uaktualniania missingColor
				xi.setActualMissingColor(degree);//uaktualniania missingColor

			}
					
		}
	// 100. KONIEC
	}


boolean test() {
	for (Vertex v : vertices) {
		TreeSet <Integer> colors = new TreeSet<>();
		for (int i = 0; i < v.getAdjacencyList().size(); ++ i) {
			int vid = v.getAdjacencyList().get(i).getId();
			if(v.getEdge(vid).getColor() > 0)
				colors.add(v.getEdge(vid).getColor());		
			
		}
		if(colors.size() != v.getAdjacencyList().size()) {
			System.out.println("ERROR " + v.getId() + " " + v.getAdjacencyList().size() + " " + colors.size());
			/*for ( int i = 0; i < v.getAdjacencyList().size(); ++ i) {
				System.out.println(v.getAdjacencyList().get(i).getId() + " " + v.getEdge(v.getAdjacencyList().get(i).getId()).getColor());
			}*/
			return false;
		}
	}
	return true;
}

boolean test2() {
//	for (Vertex v : vertices) {
//		for(Vertex vx: vertices){
//		
//		if(v.getEdge(vx.getId())!=null){
//			if (v.missingColor(v.getEdge(vx.getId()).getColor()) || v.getEdge(vx.getId()).getColor()==0)
//			{
//				//System.out.println("ERROR!!!!!!!!");
//				return false;
//			}
//		}
//			
//		}
//	}
//	//System.out.println(":)");
	return true;
}



}