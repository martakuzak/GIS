package nc_ntl;

import javax.swing.*;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.io.*;
import java.util.*;
import java.io.FileNotFoundException;
import java.nio.file.Path;
import java.nio.file.Paths;

public class MainFrame extends JFrame{
	JMenu colMenu;
	Graph graph;
	/**
	 * 
	 * @param s tytul okna
	 */
	public MainFrame(String s) {
		super(s);
		graph = null;
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(400,300);
		setLocationRelativeTo(null);

		setMenu();
		setVisible(true);
	}
/**
 * 
 */
	public void setMenu() {
		JMenuBar menuBar= new JMenuBar();

		JMenu menu= new JMenu("File");
		menuBar.add(menu);

		JMenuItem selectFile= new JMenuItem("Select input file");

		selectFile.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) { //przy wyborze tej opcji wyswietl wybor pliku
				JFileChooser chooser= new JFileChooser();
				chooser.setDialogTitle("Select input file");
				chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);

				Path currentRelativePath = Paths.get("");
				String s = currentRelativePath.toAbsolutePath().toString();

				chooser.setCurrentDirectory(new File(s));
				int returnVal = chooser.showOpenDialog(chooser);
				if(returnVal == JFileChooser.APPROVE_OPTION){
					File file = chooser.getSelectedFile();
					readFile(file);
					
				}	

			}
		});
		menu.add(selectFile);
		
		
		//menu algorytmow
		colMenu = new JMenu("Color edges using...");
		JMenuItem nc = new JMenuItem("NC algorithm");
		nc.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) { 
				long time = graph.colorNC(); //dodalam czas wykonywania algorytmu 
				writeOutputFile("nc_out.txt", time); //zmiana nazwy pliku wyjsciowego
				colMenu.setEnabled(false);
			}
		});

		JMenuItem ntl = new JMenuItem("NTL agorithm");
		ntl.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) { 
				long time = graph.colorNTL();//dodalam czas wykonywania algorytmu
				writeOutputFile("ntl_out.txt", time);//zmiana nazwy pliku wyjsciowego
				colMenu.setEnabled(false);
			}
		});

		colMenu.add(nc);
		colMenu.add(ntl);
		colMenu.setEnabled(false);
		menu.add(colMenu);
		
		JMenuItem testAll= new JMenuItem("Test all files in directory");

		testAll.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) { //przy wyborze tej opcji wyswietl wybor pliku
				JFileChooser chooser= new JFileChooser();
				chooser.setDialogTitle("Select test directory");
				chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

				Path currentRelativePath = Paths.get("");
				String s = currentRelativePath.toAbsolutePath().toString();

				chooser.setCurrentDirectory(new File(s));
				int returnVal = chooser.showOpenDialog(chooser);
				if(returnVal == JFileChooser.APPROVE_OPTION){
					File folder = chooser.getSelectedFile();
					System.out.println("filename      stopien\tNC-time\tNC-colors\tNTL-time\tNTL-colors");
					for (final File fileEntry : folder.listFiles()) {
				        if (!fileEntry.isDirectory()) {
				        	readFile(fileEntry);
				        	long time = graph.colorNC();
				        	int colorNum = graph.getColorNum();
	//			        	long colorNum = graph.colorNC();
				        	writeOutputFile("nc_out_" + fileEntry.getName() + ".txt", time);//zmiana nazwy pliku wyjsciowego
				        	
				        	//System.out.println(fileEntry.getName());
				        	readFile(fileEntry);
				        
				        	long time2 = graph.colorNTL();
					        int colorNum2 = graph.getColorNum();
		//			        long colorNum2 = graph.colorNTL()[1];
					        writeOutputFile("ntl_out_" + fileEntry.getName() + ".txt", time2);//zmiana nazwy pliku wyjsciowego
					        System.out.println(fileEntry.getName() + "\t" + graph.getDegree()+ "\t" +time + "\t\t" +colorNum + "\t" + time2 + "\t\t"+colorNum2);
		//			        System.out.println(fileEntry.getName() + "\t" + colorNum + "\t" + colorNum2 + "\t");
		
				        	
				        }
					}
				}	

			}
		});
		menu.add(testAll);

		JMenuItem close= new JMenuItem("Close");
		close.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				System.exit(1);
			}
		});
		menu.add(close);
		menuBar.add(menu);

		setJMenuBar(menuBar);
	}

	public void writeOutputFile (String name, long time) {
		Path currentRelativePath = Paths.get("");
		String s = currentRelativePath.toAbsolutePath().toString() + "\\" + name;
		//System.out.println(s);
		File file = new File(s);
		if (!file.exists()) {
			try {
				file.createNewFile();
			}
			catch(IOException e) {
				e.printStackTrace();
			}
		}
		 try {
			 FileWriter fw = new FileWriter(file.getAbsoluteFile());
			 BufferedWriter bw = new BufferedWriter(fw);
			 bw.write("VNUMBER\t" + graph.getVerticesSize()+"\n" );
			 for (Edge e : graph.getEdges()) 
				 bw.write("EDGE\t" + e.getId() + "\t" + e.getV1Id() + "\t" + e.getV2Id() + "\t" + e.getColor() + "\n");
			 bw.write("COL_NUM" + "\t" + graph.getColorNum() + "\n");
			 bw.write("DEGREE\t" + graph.getDegree() + "\n"); //fajnie byloby sobie porownac liczbe kolorow do stopnia grafu
			 bw.write("TIME\t" + time + "\n");
			 bw.close();
		 }
		 catch(IOException e) {
			 e.printStackTrace();
		 }

	}

	void readFile(File file) {

		try { //analizowanie pliku wejsciowego
			Scanner scanner = new Scanner(file);
			graph = new Graph();
			if(scanner.hasNext()) {
				if(scanner.next().equals("VNUMBER")) { //najpierw liczba wierzchlokow
					int vnumber = scanner.nextInt();
					graph.addVertices(vnumber);
					
					// wypelniam macierz sasiedztwa zerami
					// rozmiar macierzy vnumber x vnumber
					
					for(int i=0; i<vnumber; i++)
					{
						ArrayList<Integer> temp = new ArrayList<Integer>();
						for(int j=0; j<vnumber; j++)
						{
							temp.add(0);
						}
						graph.getNeighbourhoodMatrix().add(temp);
					}

					while(scanner.hasNext()) {
						if(scanner.next().equals("EDGE")) { //wczytywanie krawedzi
							int id = scanner.nextInt();
							int idV1 = scanner.nextInt();
							int idV2 = scanner.nextInt();
							boolean tmp = false;
							for (Edge e : graph.getEdges()) {
								if((e.getV1().getId() == idV1 && e.getV2().getId() == idV2) || (e.getV1().getId() == idV2 && e.getV2().getId() == idV1)) {
									tmp = true;
								}
							}
							if(idV1 != idV2 || tmp)
								graph.addEdge(idV1, idV2, id);
							else 
								System.out.println("To ma byc graf prosty");
							
						}

					}
					colMenu.setEnabled(true);
				}
				else  {
					System.err.println("Error while parsing file");
				}

			}
			scanner.close();

		}
		catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	public static void main(String[] args) {

		MainFrame mf = new MainFrame("Edge Coloring");
	}
}