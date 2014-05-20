import javax.swing.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.io.*;
import java.util.*;
import java.io.FileNotFoundException;

public class MainFrame extends JFrame{
	public MainFrame(String s) {
		super(s);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(400,300);
		setLocationRelativeTo(null);

		setMenu();
		setVisible(true);
	}

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
				
				int returnVal = chooser.showOpenDialog(chooser);
				if(returnVal == JFileChooser.APPROVE_OPTION){
					
					File file = chooser.getSelectedFile();
					
					try {
						Scanner scanner = new Scanner(file);
						Graph g = new Graph();
						if(scanner.hasNext()) {
							if(scanner.next().equals("VNUMBER")) { //najpierw liczba wierzchlokow
								int vnumber = scanner.nextInt();
								g.addVertices(vnumber); 
								while(scanner.hasNext()) {
									if(scanner.next().equals("EDGE")) { //wczytywanie krawedzi
										int id = scanner.nextInt();
										int idV1 = scanner.nextInt();
										int idV2 = scanner.nextInt();
										g.addEdge(idV1, idV2, id);
									}
									
								}
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
						g.writeGraph();
					}
					catch (FileNotFoundException e) {
						e.printStackTrace();
					}
				}	

			}
		});
		menu.add(selectFile);
		
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
	public static void main(String[] args) {
		MainFrame mf = new MainFrame("Edge Coloring");
	}
}
