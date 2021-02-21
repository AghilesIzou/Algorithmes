package up_info_ai_tp7;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.InputMismatchException;
import java.util.LinkedList;
import java.util.Scanner;

import javax.swing.JComponent;
import javax.swing.JFrame;

import up_info_ai_tp7.WeightedGraph.Graph;
import up_info_ai_tp7.WeightedGraph.Vertex;

/**
 * 
 *classe qui permet de géré l'affichage
 */

class Board extends JComponent 
{
	private static final long serialVersionUID = 1L;
	Graph graph;
	int pixelSize;
	int ncols;
	int nlines;
	/**
	 * une collection contenant un ensemble de clé-valeur, la clé correspond au numéro d'un sommet(case) et la valeur correpond à sa couleur 
	 * telle qu'elle est définie dans le fichier texte
	 */
	HashMap<Integer, String> colors;
	int start;
	int end;
	double max_distance;
	int current;
	LinkedList<Integer> path;
	
 public Board(Graph graph, int pixelSize, int ncols, int nlines, HashMap<Integer, String> colors, int start, int end)
 {
     super();
     this.graph = graph;
     this.pixelSize = pixelSize;
     this.ncols = ncols;
     this.nlines = nlines;
     this.colors = colors;
     this.start = start;
     this.end = end;
     this.max_distance = ncols * nlines;
     this.current = -1;
     this.path = null;
 }
 	
 //Mise à jour de l'affichage
	
 	@Override
 	public void paint(Graphics g) 
	{
		Graphics2D g2 = (Graphics2D) g;
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				        	RenderingHints.VALUE_ANTIALIAS_ON);
		//Ugly clear of the frame
		g2.setColor(Color.cyan);
		g2.fill(new Rectangle2D.Double(0,0,this.ncols*this.pixelSize, this.nlines*this.pixelSize));
		
		
		int num_case = 0;
		for (WeightedGraph.Vertex v : this.graph.vertexlist)
		{
			double type = v.indivTime;
			int i = num_case / this.ncols;
			int j = num_case % this.ncols;

			if (colors.get((int)type).equals("green"))
				g2.setPaint(Color.green);
			if (colors.get((int)type).equals("gray"))
				g2.setPaint(Color.gray);
			if (colors.get((int)type).equals("blue"))
				g2.setPaint(Color.blue);
			if (colors.get((int)type).equals("yellow"))
				g2.setPaint(Color.yellow);
			g2.fill(new Rectangle2D.Double(j*this.pixelSize, i*this.pixelSize, this.pixelSize, this.pixelSize));
			
			if (num_case == this.current)
			{
				g2.setPaint(Color.red);
				g2.draw(new Ellipse2D.Double(j*this.pixelSize+this.pixelSize/2, i*this.pixelSize+this.pixelSize/2, 6, 6));
			}
			if (num_case == this.start)
			{
				g2.setPaint(Color.white);
				g2.fill(new Ellipse2D.Double(j*this.pixelSize+this.pixelSize/2, i*this.pixelSize+this.pixelSize/2, 4, 4));
				
			}
			if (num_case == this.end)
			{
				g2.setPaint(Color.black);
				g2.fill(new Ellipse2D.Double(j*this.pixelSize+this.pixelSize/2, i*this.pixelSize+this.pixelSize/2, 4, 4));
			}
			
			num_case += 1;
		}
		
		num_case = 0;
		for (WeightedGraph.Vertex v : this.graph.vertexlist)
		{
			int i = num_case / this.ncols;
			int j = num_case % this.ncols;
			if (v.timeFromSource < Double.POSITIVE_INFINITY)
			{
				float g_value = (float) (1 - v.timeFromSource / this.max_distance);
				if (g_value < 0)
					g_value = 0;
				g2.setPaint(new Color(g_value, g_value, g_value));
				g2.fill(new Ellipse2D.Double(j*this.pixelSize+this.pixelSize/2, i*this.pixelSize+this.pixelSize/2, 4, 4));
				WeightedGraph.Vertex previous = v.prev;
				if (previous != null)
				{
					int i2 = previous.num / this.ncols;
					int j2 = previous.num % this.ncols;
					g2.setPaint(Color.black);
					g2.draw(new Line2D.Double(j * this.pixelSize + this.pixelSize/2, i * this.pixelSize + this.pixelSize/2, j2 * this.pixelSize + this.pixelSize/2, i2 * this.pixelSize + this.pixelSize/2));
				}
			}
				
			num_case += 1;
		}
		
		int prev = -1;
		if (this.path != null)
		{
			g2.setStroke(new BasicStroke(3.0f));
			for (int cur : this.path)
			{
				if (prev != -1)
				{
					g2.setPaint(Color.red);
					int i = prev / this.ncols;
					int j = prev % this.ncols;
					int i2 = cur / this.ncols;
					int j2 = cur % this.ncols;
					g2.draw(new Line2D.Double(j * this.pixelSize + this.pixelSize/2, i * this.pixelSize + this.pixelSize/2, j2 * this.pixelSize + this.pixelSize/2, i2 * this.pixelSize + this.pixelSize/2));
				}
				prev = cur;
			}
		}
	}
	
	//Mise à jour du graphe (à appeler avant de mettre à jour l'affichage)
 	/**
 	 * cette méthode permet de mettre à jour le graphe avant de l'afficher
 	 * @param graph le graphe à mettre à jour
 	 * @param current le sommet courant qui est traité
 	 */
	public void update(Graph graph, int current)
	{
		this.graph = graph;
		this.current = current;
		repaint();
	}
	
	//Indiquer le chemin (pour affichage)
	/**
	 * permet d'indiquer le chemin obtenu une fois l'un des algorithmes est appliqué
	 * @param graph le graphe correspondant à la carte en 2D
	 * @param le chemin obtenu (liste de sommet)
	 */
	public void addPath(Graph graph, LinkedList<Integer> path)
	{
		this.graph = graph;
		this.path = path;
		this.current = -1;
		repaint();
	}
}

//Classe principale contenant la méthode main, le point d'entré du programme ainsi que les méthodes correspondantes aux algos
// de Dijkstra & A*
public class App {
	
	//Initialise l'affichage
	private static void drawBoard(Board board, int nlines, int ncols, int pixelSize)
	{
	    JFrame window = new JFrame("Plus court chemin");
	    window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    window.setBounds(0, 0, ncols*pixelSize+20, nlines*pixelSize+40);
	    window.getContentPane().add(board);
	    window.setVisible(true);
	}
	
		/**
		 * implémentation de l'algo A*
		 * @param graph le graphe représentant la carte
		 * @param start un entier représentant la case de départ
		 * @param end un entier représentant la case d'arrivée
		 * @param ncols le nombre de colonnes dans la carte
		 * @param numberV le nombre de cases dans la carte
		 * @param board l'affichage
		 * @return retourne une liste d'entiers correspondant au chemin.
		 */
	
		private static LinkedList<Integer> AStar(Graph graph, int start, int end, int ncols, int numberV, Board board)
		{
			graph.vertexlist.get(start).timeFromSource=0;
			int number_tries = 0;
			
			// mettre tous les noeuds du graphe dans la liste des noeuds à visiter:
			HashSet<Integer> to_visit = new HashSet<Integer>();
			for (Vertex vertex : graph.vertexlist) {
				to_visit.add(vertex.num);
			}
			
			//Remplir l'attribut graph.vertexlist.get(v).heuristic pour tous les noeuds v du graphe:
			
			
			for (Vertex vertex : graph.vertexlist) {
			
				vertex.heuristic = vertex.num*0.001;
			/*	vertex.heuristic =  DijkstraHeuristic(graph, vertex.num, end, numberV );
			 * 
			 * l'utilistation de la méthode DijkstraHeuristic donne une estimation  réelle du plus court chemin entre un sommet du graphe 
			 *  et le sommet de distination mais étant donnée que la méthode Dijkstra normal nécessite un peu moins d'une minute pour donner le plus court
			 *  chemin entre un sommet de départ et un sommet d'arrivé et si on fait cela avec tous les sommets du graphe (comme par exemple 
			 *  on a un graphe composé de  5000 sommets) donc le programme donnera le résultat qu'après environ 5000 minutes !!! ce qui est extrêmement lent
			 * cela marche seulement pour des petits graphes
			*/	
			}
			
			int min_v = 0;
			
			while (to_visit.contains(end))
			{
				
				for (Integer firstVertex : to_visit) {
					min_v = firstVertex;
					break;
				}
				
				for (Integer v : to_visit) {
					if( graph.vertexlist.get(min_v).timeFromSource + graph.vertexlist.get(min_v).heuristic >graph.vertexlist.get(v).timeFromSource + graph.vertexlist.get(v).heuristic) {
						min_v = graph.vertexlist.get(v).num;
					}
				}
				
				//trouver le noeud min_v parmis tous les noeuds v ayant la distance temporaire
				//   (graph.vertexlist.get(v).timeFromSource + heuristic) minimale.
				
				//On l'enlève des noeuds à visiter
				to_visit.remove(min_v);
				number_tries += 1;
				
				
				//pour tous ses voisins, on vérifie si on est plus rapide en passant par ce noeud.
				
				for (int i = 0; i < graph.vertexlist.get(min_v).adjacencylist.size(); i++)
				{
					int to_try = graph.vertexlist.get(min_v).adjacencylist.get(i).destination;
					
					if(graph.vertexlist.get(to_try).timeFromSource> (graph.vertexlist.get(min_v).timeFromSource+ graph.vertexlist.get(min_v).adjacencylist.get(i).weight)) {
						
						graph.vertexlist.get(to_try).timeFromSource = graph.vertexlist.get(min_v).timeFromSource+ graph.vertexlist.get(min_v).adjacencylist.get(i).weight;
						graph.vertexlist.get(to_try).prev = graph.vertexlist.get(min_v);
					}
					
				}
				
				//On met à jour l'affichage
				try {
		    	    board.update(graph, min_v);
		    	    Thread.sleep(10);
		    	} catch(InterruptedException e) {
		    	    System.out.println("stop");
		    	}
		            
			}
			
			System.out.println("Done! Using A*:");
			System.out.println("	Number of nodes explored: " + number_tries);
			System.out.println("	Total time of the path: " + graph.vertexlist.get(end).timeFromSource);
			LinkedList<Integer> path=new LinkedList<Integer>();
			path.addFirst(end);
			//remplir la liste path avec le chemin
			
			Vertex  v = graph.vertexlist.get(end).prev;
			do {
				path.addFirst(v.num);
				v = v.prev;
				
			}while(!v.equals(graph.vertexlist.get(start)));
			
			path.addFirst(graph.vertexlist.get(start).num);
			
			board.addPath(graph, path);
			return path;
		}
	
	
	
	/**
	 * cette méthode correspond à l'implémentation de l'algo de Dijkstra
	 * @param graph le graphe représentant la carte
	 * @param start un entier représentant la case de départ
	 * @param end un entier représentant la case d'arrivée
	 * @param numberV le nombre de cases dans la carte
	 * @param board l'affichage
	 * @return retourne une liste d'entiers correspondant au plus court chemin.
	 */
	
	private static LinkedList<Integer> Dijkstra(Graph graph, int start, int end, int numberV, Board board)
	{
		graph.vertexlist.get(start).timeFromSource=0;
		int number_tries = 0;
		
		// mettre tous les noeuds du graphe dans la liste des noeuds à visiter
		
		HashSet<Integer> to_visit = new HashSet<Integer>();
		
		for (Vertex vertex : graph.vertexlist) {
			to_visit.add(vertex.num);
		}
		int min_v = 0;
		
		while (to_visit.contains(end))
		{
			
			
			for (Integer firstVertex : to_visit) {
				min_v = firstVertex;
				break;
			}
			
			for (Integer v : to_visit) {
				if( graph.vertexlist.get(min_v).timeFromSource >graph.vertexlist.get(v).timeFromSource) {
					min_v = graph.vertexlist.get(v).num;
				}
			}
			
			/* trouver le noeud min_v parmis tous les noeuds v ayant la distance temporaire  
			 * graph.vertexlist.get(v).timeFromSource minimale.
			*/     
			
			//On l'enlève des noeuds à visiter
			to_visit.remove(min_v);
			number_tries += 1;
			
			// pour tous ses voisins, on vérifie si on est plus rapide en passant par ce noeud.
			for (int i = 0; i < graph.vertexlist.get(min_v).adjacencylist.size(); i++)
			{
				int to_try = graph.vertexlist.get(min_v).adjacencylist.get(i).destination;
				
				if(graph.vertexlist.get(to_try).timeFromSource> (graph.vertexlist.get(min_v).timeFromSource+ graph.vertexlist.get(min_v).adjacencylist.get(i).weight)) {
					graph.vertexlist.get(to_try).timeFromSource = graph.vertexlist.get(min_v).timeFromSource+ graph.vertexlist.get(min_v).adjacencylist.get(i).weight;
					graph.vertexlist.get(to_try).prev = graph.vertexlist.get(min_v);
				}
				
			}
			//On met à jour l'affichage
			try {
	    	    board.update(graph, min_v);
	    	    Thread.sleep(10);
	    	} catch(InterruptedException e) {
	    	    System.out.println("stop");
	    	}
	            
		}
		
		System.out.println("Done! Using Dijkstra:");
		System.out.println("	Number of nodes explored: " + number_tries);
		System.out.println("	Total time of the path: " + graph.vertexlist.get(end).timeFromSource);
		LinkedList<Integer> path=new LinkedList<Integer>();
		path.addFirst(end);
	
		//remplir la liste path avec le chemin
		
		Vertex  v = graph.vertexlist.get(end).prev;
		do {
			path.addFirst(v.num);
			v = v.prev;
			
		}while(!v.equals(graph.vertexlist.get(start)));
		
			path.addFirst(graph.vertexlist.get(start).num);
		
		board.addPath(graph, path);
		return path;
	}
	
	/**
	 * cette méthode correspond permet de donner une heuristique à chaque sommet du graphe en exécutant l'algo de Dijkstra
	 * @param graph le graphe représentant la carte
	 * @param start un entier représentant la case de départ
	 * @param end un entier représentant la case d'arrivée
	 * @param numberV le nombre de cases dans la carte
	 * 
	 * @return retourne un double qui correspond à une heuristique qui estime le temps nécessaire pour 
	 * aller du sommet pour lequel on cherche une heuristique et le sommet final
	 */
	
	@SuppressWarnings("unused")
	private static double DijkstraHeuristic(Graph graph, int start, int end, int numberV)
	{
		graph.vertexlist.get(start).timeFromSource=0;
		
		
		// mettre tous les noeuds du graphe dans la liste des noeuds à visiter
		
		HashSet<Integer> to_visit = new HashSet<Integer>();
		
		for (Vertex vertex : graph.vertexlist) {
			to_visit.add(vertex.num);
		}
		int min_v = 0;
		
		while (to_visit.contains(end))
		{
			
			
			for (Integer firstVertex : to_visit) {
				min_v = firstVertex;
				break;
			}
			
			for (Integer v : to_visit) {
				if( graph.vertexlist.get(min_v).timeFromSource >graph.vertexlist.get(v).timeFromSource) {
					min_v = graph.vertexlist.get(v).num;
				}
			}
			
			/* trouver le noeud min_v parmis tous les noeuds v ayant la distance temporaire  
			 * graph.vertexlist.get(v).timeFromSource minimale.
			*/     
			
			//On l'enlève des noeuds à visiter
			to_visit.remove(min_v);
			
			
			// pour tous ses voisins, on vérifie si on est plus rapide en passant par ce noeud.
			for (int i = 0; i < graph.vertexlist.get(min_v).adjacencylist.size(); i++)
			{
				int to_try = graph.vertexlist.get(min_v).adjacencylist.get(i).destination;
				
				if(graph.vertexlist.get(to_try).timeFromSource> (graph.vertexlist.get(min_v).timeFromSource+ graph.vertexlist.get(min_v).adjacencylist.get(i).weight)) {
					graph.vertexlist.get(to_try).timeFromSource = graph.vertexlist.get(min_v).timeFromSource+ graph.vertexlist.get(min_v).adjacencylist.get(i).weight;
					graph.vertexlist.get(to_try).prev = graph.vertexlist.get(min_v);
				}
				
			}
			
	            
		}
		
	
		return graph.vertexlist.get(end).timeFromSource;
	}
	
	
	
	
	
	
	
	/**
	 * méthode permettant de lire un entier au clavier
	 * @param sc l'objet scanner dans lequel on lit l'entier
	 * @param message le message à afficher àl'utilisateur
	 * @return l'entier lu
	 */
	private static int lireEntierAuClavier(Scanner sc, String message) {
		int res = 0;
		boolean lectureOK = false;

		while (!lectureOK) {
			try {
				System.out.print(message);
				res = sc.nextInt();
				lectureOK = true;
			} catch (InputMismatchException e) {
				System.out.println("Error : You must write an integer.");
				sc.nextLine();
			}
		}
		return res;
	}
		/**
		 * menu qui affiche à l'utilisateur les 2 algorithmes qui sont à sa disposition afin de trouver le plus court chemin
		 */
		public static void menuResolution() {
		
		System.out.println("");
		System.out.println("|==========================================================================================|");
		System.out.println("|=================================== Algorithms  ==========================================|");
		System.out.println("|==========================================================================================|");
		System.out.println("|1) Use Dijkstra's algorithm.                                                              |");
		System.out.println("|2) Use A-Star algorithm.                                                                  |");
		System.out.println("|==========================================================================================|\n");
		
	}
	
	//Méthode principale
	public static void main(String[] args) {
			Scanner keybord = new Scanner(System.in);
			int choixAlgo=0;
		
		
		//Lecture de la carte et création du graphe
		try {
			
		      File myObj = new File("/home/rilles/Bureau/graph.txt");
		      Scanner myReader = new Scanner(myObj);
		      String data = "";
		      //On ignore les deux premières lignes
		      for (int i=0; i < 3; i++)
		    	  data = myReader.nextLine();
		      
		      //Lecture du nombre de lignes
		      int nlines = Integer.parseInt(data.split("=")[1]);
		      //Et du nombre de colonnes
		      data = myReader.nextLine();
		      int ncols = Integer.parseInt(data.split("=")[1]);
		      
		      //Initialisation du graphe
		      Graph graph = new Graph();
		      
		      HashMap<String, Integer> groundTypes = new HashMap<String, Integer>();
		      HashMap<Integer, String> groundColor = new HashMap<Integer, String>();
		      data = myReader.nextLine();
		      data = myReader.nextLine();
		      //Lire les différents types de cases
		      while (!data.equals("==Graph=="))
		      {
		    	  String name = data.split("=")[0];
		    	  int time = Integer.parseInt(data.split("=")[1]);
		    	  data = myReader.nextLine();
		    	  String color = data;
		    	  groundTypes.put(name, time);
		    	  groundColor.put(time, color);
		    	  data = myReader.nextLine();
		      }
		      
		      //On ajoute les sommets dans le graphe (avec le bon type)
		      for (int line=0; line < nlines; line++)
		      {
		    	  data = myReader.nextLine();
		    	  for (int col=0; col < ncols; col++)
		    	  {
		    		  graph.addVertex(groundTypes.get(String.valueOf(data.charAt(col))));
		    	  }
		      }
		      
		     
		      for (int line=0; line < nlines; line++)
		      {
		    	  for (int col=0; col < ncols; col++)
		    	  {
		    		  int source = line*ncols+col;
		    		  int dest;
		    		  double weight;
		    		
		    		  
		    		  if(line==0) {
		    			  
		    		 
		    			  if(col==0) {
		    				  
		    				  dest = (line + 1)*ncols+col;
		    				  // pour trouver le poids de l'arête j'ai appliqué la formule tA+tB/2
		    				  weight = (graph.vertexlist.get(source).indivTime + graph.vertexlist.get(dest).indivTime)/2; 
		    				  graph.addEgde(source, dest, weight);
		    				  
		    				  
		    				  
		    				  dest = (line)*ncols+col+1;
		    				  // pour trouver le poids de l'arête j'ai appliqué la formule tA+tB/2
		    				  weight = (graph.vertexlist.get(source).indivTime + graph.vertexlist.get(dest).indivTime)/2; 
		    				  graph.addEgde(source, dest, weight);
		    				  
		    				 
		    				 
		    				  
		    				  
		    				  dest = (line + 1)*ncols+col + 1;
		    				  weight = (Math.sqrt( Math.pow(graph.vertexlist.get(source).indivTime, 2)*2) + Math.sqrt( Math.pow(graph.vertexlist.get(dest).indivTime, 2)*2))/2 ; 
		    				  graph.addEgde(source, dest, weight);
		    			  
		    			  
		    			  
		    			  }else if(col== ncols-1) {
		    				  
		    				  
		    				  dest = (line +1)*ncols+col -1;
		    				  /**
		    				   * application du théorème de pythagore pour trouver le tx nécessaire pour parcourir diagonalement une case source ou distination 
		    				  *	puis application de la formule tA+tB/2 pour trouver le poids diagonal
		    				  *
		    				  */
		    				  weight = (Math.sqrt( Math.pow(graph.vertexlist.get(source).indivTime, 2)*2) + Math.sqrt( Math.pow(graph.vertexlist.get(dest).indivTime, 2)*2))/2 ; 
		    				  graph.addEgde(source, dest, weight);
		    			  
		    			  
		    				   dest = (line + 1)*ncols+col;
		    				  // pour trouver le poids de l'arête j'ai appliqué la formule tA+tB/2
		    				  weight = (graph.vertexlist.get(source).indivTime + graph.vertexlist.get(dest).indivTime)/2; 
		    				  graph.addEgde(source, dest, weight);
		    				  
		    				  
		    				  dest = (line)*ncols+col - 1;
		    				  weight = (Math.sqrt( Math.pow(graph.vertexlist.get(source).indivTime, 2)*2) + Math.sqrt( Math.pow(graph.vertexlist.get(dest).indivTime, 2)*2))/2 ; 
		    				  graph.addEgde(source, dest, weight);
		    			  
		    				  
		    				  
		    				  
		    			  }else{
		    				 
		    				  dest = (line)*ncols+col+1;
		    				  // pour trouver le poids de l'arête j'ai appliqué la formule tA+tB/2
		    				  weight = (graph.vertexlist.get(source).indivTime + graph.vertexlist.get(dest).indivTime)/2; 
		    				  graph.addEgde(source, dest, weight);
		    				  
		    				  dest = (line)*ncols+col-1;
		    				  // pour trouver le poids de l'arête j'ai appliqué la formule tA+tB/2
		    				  weight = (graph.vertexlist.get(source).indivTime + graph.vertexlist.get(dest).indivTime)/2; 
		    				  graph.addEgde(source, dest, weight);
		    				  
		    				  dest = (line + 1)*ncols+col;
		    				  // pour trouver le poids de l'arête j'ai appliqué la formule tA+tB/2
		    				  weight = (graph.vertexlist.get(source).indivTime + graph.vertexlist.get(dest).indivTime)/2; 
		    				  graph.addEgde(source, dest, weight);
		    				  
		    				  
		    				  
		    				  dest = (line + 1)*ncols+col - 1;
		    				  weight = (Math.sqrt( Math.pow(graph.vertexlist.get(source).indivTime, 2)*2) + Math.sqrt( Math.pow(graph.vertexlist.get(dest).indivTime, 2)*2))/2 ; 
		    				  graph.addEgde(source, dest, weight);
		    			  
		    				  dest = (line + 1)*ncols+col + 1;
		    				  weight = (Math.sqrt( Math.pow(graph.vertexlist.get(source).indivTime, 2)*2) + Math.sqrt( Math.pow(graph.vertexlist.get(dest).indivTime, 2)*2))/2 ; 
		    				  graph.addEgde(source, dest, weight);
		    			  
		    			  
		    			  }
		    		  
		    		  }else if(line == nlines-1) {
		    		  
		    		  
		    		  	  if(col==0) {
		    				  
		    				  
		    				  dest = (line - 1)*ncols+col +1;
		    				  /**
		    				   * application du théorème de pythagore pour trouver le tx nécessaire pour parcourir diagonalement une case source ou distination 
		    				  *	puis application de la formule tA+tB/2 pour trouver le poids diagonal
		    				  *
		    				  */
		    				  weight = (Math.sqrt( Math.pow(graph.vertexlist.get(source).indivTime, 2)*2) + Math.sqrt( Math.pow(graph.vertexlist.get(dest).indivTime, 2)*2))/2 ; 
		    				  graph.addEgde(source, dest, weight);
		    			  
		    			  
		    				  dest = (line - 1)*ncols+col;
		    				  // pour trouver le poids de l'arête j'ai appliqué la formule tA+tB/2
		    				  weight = (graph.vertexlist.get(source).indivTime + graph.vertexlist.get(dest).indivTime)/2; 
		    				  graph.addEgde(source, dest, weight);
		    				  
		    				  
		    				  
		    				  dest = (line)*ncols+col+1;
		    				  // pour trouver le poids de l'arête j'ai appliqué la formule tA+tB/2
		    				  weight = (graph.vertexlist.get(source).indivTime + graph.vertexlist.get(dest).indivTime)/2; 
		    				  graph.addEgde(source, dest, weight);
		    				  
		    				 
		    				  
		    				 
		    			  
		    			  
		    			  }else if(col== ncols-1) {
		    				  
		    				  
		    				  dest = (line - 1)*ncols+col -1;
		    				  /**
		    				   * application du théorème de pythagore pour trouver le tx nécessaire pour parcourir diagonalement une case source ou distination 
		    				  *	puis application de la formule tA+tB/2 pour trouver le poids diagonal
		    				  *
		    				  */
		    				  weight = (Math.sqrt( Math.pow(graph.vertexlist.get(source).indivTime, 2)*2) + Math.sqrt( Math.pow(graph.vertexlist.get(dest).indivTime, 2)*2))/2 ; 
		    				  graph.addEgde(source, dest, weight);
		    			  
		    			  
		    				  dest = (line - 1)*ncols+col;
		    				  // pour trouver le poids de l'arête j'ai appliqué la formule tA+tB/2
		    				  weight = (graph.vertexlist.get(source).indivTime + graph.vertexlist.get(dest).indivTime)/2; 
		    				  graph.addEgde(source, dest, weight);
		    				  
		    				  
		    				  
		    				  dest = (line)*ncols+col-1;
		    				  // pour trouver le poids de l'arête j'ai appliqué la formule tA+tB/2
		    				  weight = (graph.vertexlist.get(source).indivTime + graph.vertexlist.get(dest).indivTime)/2; 
		    				  graph.addEgde(source, dest, weight);
		    				  
		    				 
		    				  
		    			}else {
		    				
		    				  dest = (line)*ncols+col+1;
		    				  // pour trouver le poids de l'arête j'ai appliqué la formule tA+tB/2
		    				  weight = (graph.vertexlist.get(source).indivTime + graph.vertexlist.get(dest).indivTime)/2; 
		    				  graph.addEgde(source, dest, weight);
		    				  
		    				  dest = (line)*ncols+col-1;
		    				  // pour trouver le poids de l'arête j'ai appliqué la formule tA+tB/2
		    				  weight = (graph.vertexlist.get(source).indivTime + graph.vertexlist.get(dest).indivTime)/2; 
		    				  graph.addEgde(source, dest, weight);
		    				  
		    				 
		    				  dest = (line - 1)*ncols+col;
		    				  // pour trouver le poids de l'arête j'ai appliqué la formule tA+tB/2
		    				  weight = (graph.vertexlist.get(source).indivTime + graph.vertexlist.get(dest).indivTime)/2; 
		    				  graph.addEgde(source, dest, weight);
		    				  
		    				  
		    				  dest = (line - 1)*ncols+col -1;
		    				 weight = (Math.sqrt( Math.pow(graph.vertexlist.get(source).indivTime, 2)*2) + Math.sqrt( Math.pow(graph.vertexlist.get(dest).indivTime, 2)*2))/2 ; 
		    				  graph.addEgde(source, dest, weight);
		    				  
		    				  
		    				  dest = (line - 1)*ncols+col +1;
		    				 
		    				  weight = (Math.sqrt( Math.pow(graph.vertexlist.get(source).indivTime, 2)*2) + Math.sqrt( Math.pow(graph.vertexlist.get(dest).indivTime, 2)*2))/2 ; 
		    				  graph.addEgde(source, dest, weight);
		    				  
		    				  
		    			}
		    			
		    			
		    			
		    	}else{
		    				  
		    		 if(col==0) {
	    				  
	    				  
	    				  dest = (line - 1)*ncols+col +1;
	    				  /**
	    				   * application du théorème de pythagore pour trouver le tx nécessaire pour parcourir diagonalement une case source ou distination 
	    				  *	puis application de la formule tA+tB/2 pour trouver le poids diagonal
	    				  *
	    				  */
	    				  weight = (Math.sqrt( Math.pow(graph.vertexlist.get(source).indivTime, 2)*2) + Math.sqrt( Math.pow(graph.vertexlist.get(dest).indivTime, 2)*2))/2 ; 
	    				  graph.addEgde(source, dest, weight);
	    				  
	    				  dest = (line + 1)*ncols+col +1;
	    				  /**
	    				   * application du théorème de pythagore pour trouver le tx nécessaire pour parcourir diagonalement une case source ou distination 
	    				  *	puis application de la formule tA+tB/2 pour trouver le poids diagonal
	    				  *
	    				  */
	    				  weight = (Math.sqrt( Math.pow(graph.vertexlist.get(source).indivTime, 2)*2) + Math.sqrt( Math.pow(graph.vertexlist.get(dest).indivTime, 2)*2))/2 ; 
	    				  graph.addEgde(source, dest, weight);
	    			  
	    			  
	    				  dest = (line - 1)*ncols+col;
	    				  // pour trouver le poids de l'arête j'ai appliqué la formule tA+tB/2
	    				  weight = (graph.vertexlist.get(source).indivTime + graph.vertexlist.get(dest).indivTime)/2; 
	    				  graph.addEgde(source, dest, weight);
	    				  
	    				  
	    				  
	    				  dest = (line)*ncols+col+1;
	    				  // pour trouver le poids de l'arête j'ai appliqué la formule tA+tB/2
	    				  weight = (graph.vertexlist.get(source).indivTime + graph.vertexlist.get(dest).indivTime)/2; 
	    				  graph.addEgde(source, dest, weight);
	    				  
	    				  dest = (line+1)*ncols+col;
	    				  // pour trouver le poids de l'arête j'ai appliqué la formule tA+tB/2
	    				  weight = (graph.vertexlist.get(source).indivTime + graph.vertexlist.get(dest).indivTime)/2; 
	    				  graph.addEgde(source, dest, weight);
	    				  
	    			}else if(col== ncols-1) {
	    				  
	    				  
	    				  dest = (line - 1)*ncols+col -1;
	    				  /**
	    				   * application du théorème de pythagore pour trouver le tx nécessaire pour parcourir diagonalement une case source ou distination 
	    				  *	puis application de la formule tA+tB/2 pour trouver le poids diagonal
	    				  *
	    				  */
	    				  weight = (Math.sqrt( Math.pow(graph.vertexlist.get(source).indivTime, 2)*2) + Math.sqrt( Math.pow(graph.vertexlist.get(dest).indivTime, 2)*2))/2 ; 
	    				  graph.addEgde(source, dest, weight);
	    			  
	    			  
	    				  dest = (line - 1)*ncols+col;
	    				  // pour trouver le poids de l'arête j'ai appliqué la formule tA+tB/2
	    				  weight = (graph.vertexlist.get(source).indivTime + graph.vertexlist.get(dest).indivTime)/2; 
	    				  graph.addEgde(source, dest, weight);
	    				  
	    				  
	    				  
	    				  dest = (line)*ncols+col-1;
	    				  // pour trouver le poids de l'arête j'ai appliqué la formule tA+tB/2
	    				  weight = (graph.vertexlist.get(source).indivTime + graph.vertexlist.get(dest).indivTime)/2; 
	    				  graph.addEgde(source, dest, weight);
	    				  
		    		
	    				  dest = (line + 1)*ncols+col -1;
	    				 
	    				  weight = (Math.sqrt( Math.pow(graph.vertexlist.get(source).indivTime, 2)*2) + Math.sqrt( Math.pow(graph.vertexlist.get(dest).indivTime, 2)*2))/2 ; 
	    				  graph.addEgde(source, dest, weight);
	    			  
	    			  
	    				  dest = (line + 1)*ncols+col;
	    				  // pour trouver le poids de l'arête j'ai appliqué la formule tA+tB/2
	    				  weight = (graph.vertexlist.get(source).indivTime + graph.vertexlist.get(dest).indivTime)/2; 
	    				  graph.addEgde(source, dest, weight);
		    		
		    		
		    		
		    		
		    		
	    			  }else {
		    		
	    				  dest = (line - 1)*ncols+col - 1;
		    				  /**
		    				   * application du théorème de pythagore pour trouver le tx nécessaire pour parcourir diagonalement une case source ou distination 
		    				  *	puis application de la formule tA+tB/2 pour trouver le poids diagonal
		    				  *
		    				  */
		    				  weight = (Math.sqrt( Math.pow(graph.vertexlist.get(source).indivTime, 2)*2) + Math.sqrt( Math.pow(graph.vertexlist.get(dest).indivTime, 2)*2))/2 ; 
		    				  graph.addEgde(source, dest, weight);
		    			  
		    			  
		    				  dest = (line - 1)*ncols+col;
		    				  // pour trouver le poids de l'arête j'ai appliqué la formule tA+tB/2
		    				  weight = (graph.vertexlist.get(source).indivTime + graph.vertexlist.get(dest).indivTime)/2; 
		    				  graph.addEgde(source, dest, weight);
		    				  
		    				  dest = (line - 1)*ncols+col+1;
		    				 
		    				  weight = (Math.sqrt( Math.pow(graph.vertexlist.get(source).indivTime, 2)*2) + Math.sqrt( Math.pow(graph.vertexlist.get(dest).indivTime, 2)*2))/2 ; 
		    				  graph.addEgde(source, dest, weight);
		    				  
		    				  dest = (line)*ncols+col+1;
		    				  // pour trouver le poids de l'arête j'ai appliqué la formule tA+tB/2
		    				  weight = (graph.vertexlist.get(source).indivTime + graph.vertexlist.get(dest).indivTime)/2; 
		    				  graph.addEgde(source, dest, weight);
		    				  
		    				  dest = (line)*ncols+col-1;
		    				  // pour trouver le poids de l'arête j'ai appliqué la formule tA+tB/2
		    				  weight = (graph.vertexlist.get(source).indivTime + graph.vertexlist.get(dest).indivTime)/2; 
		    				  graph.addEgde(source, dest, weight);
		    				  
		    				  dest = (line + 1)*ncols+col;
		    				  // pour trouver le poids de l'arête j'ai appliqué la formule tA+tB/2
		    				  weight = (graph.vertexlist.get(source).indivTime + graph.vertexlist.get(dest).indivTime)/2; 
		    				  graph.addEgde(source, dest, weight);
		    				  
		    				  
		    				  
		    				  dest = (line + 1)*ncols+col - 1;
		    				  weight = (Math.sqrt( Math.pow(graph.vertexlist.get(source).indivTime, 2)*2) + Math.sqrt( Math.pow(graph.vertexlist.get(dest).indivTime, 2)*2))/2 ; 
		    				  graph.addEgde(source, dest, weight);
		    			  
		    				  dest = (line + 1)*ncols+col + 1;
		    				  weight = (Math.sqrt( Math.pow(graph.vertexlist.get(source).indivTime, 2)*2) + Math.sqrt( Math.pow(graph.vertexlist.get(dest).indivTime, 2)*2))/2 ; 
		    				  graph.addEgde(source, dest, weight);
		    			}  
		    			  
		    		}
		    			  
		    				  
		    	}
		    		 
		    }
		      
		      
		      //On obtient les noeuds de départ et d'arrivé
		      data = myReader.nextLine();
		      data = myReader.nextLine();
		      int startV = Integer.parseInt(data.split("=")[1].split(",")[0]) * ncols + Integer.parseInt(data.split("=")[1].split(",")[1]);
		      data = myReader.nextLine();
		      int endV = Integer.parseInt(data.split("=")[1].split(",")[0]) * ncols + Integer.parseInt(data.split("=")[1].split(",")[1]);

		      myReader.close();
		      
		      
		      int pixelSize = 10;
		      Board board = new Board(graph, pixelSize, ncols, nlines, groundColor, startV, endV);
		      drawBoard(board, nlines, ncols, pixelSize);
		      board.repaint();
		      
		      try {
		    	    Thread.sleep(100);
		      }catch(InterruptedException e) {
		    	    System.out.println("stop");
		      }
		      
		      LinkedList<Integer> path = new LinkedList<>();
		      
		      do {
		    	  
		    	 menuResolution();
		    	 choixAlgo = lireEntierAuClavier(keybord, "choice = ");
		    	 
		    	 switch(choixAlgo) {
		    	 
		    	 	case 1 : 
		    	 		System.out.println("\nLoading...........\n");
		    	 		path = Dijkstra(graph, startV, endV, nlines*ncols, board);
		    	 		break;
		    	 	case 2 : 
		    	 		System.out.println("\nLoading...........\n");
		    	 		path= AStar(graph, startV, endV, ncols, nlines*ncols,  board);
		    	 		break;
		    	 	default:
		    	 		System.out.println("Error ! you must choose an integer between 1 & 2.");
		    	 		keybord.nextLine();
		    	 }
		    	  
		      
		      }while(choixAlgo!=1 & choixAlgo!=2);
		    
		      
		      //Écriture du chemin dans un fichier de sortie
		      try {
			// NB : il faut changer le chemin vers le fichier 
			      File file = new File("/home/rilles/Bureau/out.txt");
			      if (!file.exists()) {
			    	  file.createNewFile();
			      } 
			      FileWriter fw = new FileWriter(file.getAbsoluteFile());
			      BufferedWriter bw = new BufferedWriter(fw);
			      
			      for (int i: path)
			      {
			    	  bw.write(String.valueOf(i));
			    	  bw.write('\n');
			      }
			      bw.close();	
		      
			} catch (IOException e) {
				e.printStackTrace();
			  } 
		    } catch (FileNotFoundException e) {
		      System.out.println("An error occurred.");
		      e.printStackTrace();
		    }
		keybord.close();
	}

}
