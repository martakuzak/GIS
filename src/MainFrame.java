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
					
					try { //analizowanie pliku wejsciowego
						Scanner scanner = new Scanner(file);
						graph = new Graph();
						if(scanner.hasNext()) {
							if(scanner.next().equals("VNUMBER")) { //najpierw liczba wierzchlokow
								int vnumber = scanner.nextInt();
								graph.addVertices(vnumber); 
								while(scanner.hasNext()) {
									if(scanner.next().equals("EDGE")) { //wczytywanie krawedzi
										int id = scanner.nextInt();
										int idV1 = scanner.nextInt();
										int idV2 = scanner.nextInt();
										graph.addEdge(idV1, idV2, id);
									}
									
								}
								colMenu.setEnabled(true);
							}
							else  {
								System.err.println("Error while parsing file");
								JOptionPane.showMessageDialog(chooser,
								    "Graph cannot be constructed properly.",
								    "Error while parsing file",
								    JOptionPane.ERROR_MESSAGE);
							}
								
						}
						scanner.close();
						
					}
					catch (FileNotFoundException e) {
						e.printStackTrace();
					}
				}	

			}
		});
		menu.add(selectFile);
		//menu algorytmow
		colMenu = new JMenu("Color edges using...");
		JMenuItem nc = new JMenuItem("NC algorithm");
		nc.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) { 
				graph.colorNC();
				writeOutputFile("out.txt");
			}
		});
		
		JMenuItem ntl = new JMenuItem("NTL agorithm");
		ntl.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) { 
				graph.colorNTL();
				writeOutputFile("out.txt");
			}
		});
		
		colMenu.add(nc);
		colMenu.add(ntl);
		colMenu.setEnabled(false);
		menu.add(colMenu);
		
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
	
	public void writeOutputFile (String name) {
		Path currentRelativePath = Paths.get("");
		String s = currentRelativePath.toAbsolutePath().toString() + "\\test\\output\\" + name;
		System.out.println(s);
		String content = "a";
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
			 bw.write("TIME\n");
			 bw.write("OP_NUM\n");
			 bw.close();
		 }
		 catch(IOException e) {
			 e.printStackTrace();
		 }
		
	}
	
	public static void main(String[] args) {
		MainFrame mf = new MainFrame("Edge Coloring");
	}
}
