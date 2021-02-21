package up_info_ai_tp7;
import java.util.LinkedList;
import java.util.ArrayList;

/**
 * Cette classe permet de définir un graphe pondéré.
 * @author Sylvain Lobry
 *
 */


public class WeightedGraph {
	/**
	 * 
	 * classe interne pour définir  une arête.
	 */
	
	// 
 static class Edge {
    /**
     * sommet duquel part l'arête
     */
	 int source;
	 /**
	  * sommet de distination de l'arête
	  */
     int destination;
    
     /**
      * poids d'une arête
      */
     double weight;

    /**
     * constructeur pour définir une arête
     * @param source sommet duquel l'arête part 
     * @param destination le sommet distination de l'arête
     * @param weight
     */
     
     public Edge(int source, int destination, double weight) {
         this.source = source;
         this.destination = destination;
         this.weight = weight; //Tsource+Tdistination/2
     }
 }
 
 /**
  * classe interne pour définir un somet
  */
 static class Vertex {
 	/**
 	 *  c'est le temps nécessaire pour parcourir horizontalement ou verticalement le sommet (la case)
 	 */
	 double indivTime;  
 	/**
 	 * cela correspond au coût du chemin que construit Dijkstra une fois arrivé au sommet en question en partant du sommet de départ
 	 */
	 double timeFromSource; 
 	/**
 	 * Cela correspond à l’estimation du coût réel d'un chemin reliant ce sommet au sommet sommet final
 	 */
	double heuristic;  
 	/**
 	 * sommet parent de ce sommet dans le plus court chemin
 	 */
	Vertex prev; 
	/**
	 * liste des arêtes reliant ce sommet au sommets voisins
	 */
 	LinkedList<Edge> adjacencylist;
 	/**
 	 * numéro de ce sommet
 	 */
 	int num;
 	
 	/**
 	 * contructeur permettant de définir un sommet
 	 * @param num numéro du sommet qu'on construit
 	 */
 	public Vertex(int num) {
 		this.indivTime = Double.POSITIVE_INFINITY;
 		this.timeFromSource = Double.POSITIVE_INFINITY;
 		this.heuristic = -1;
 		this.prev = null;
 		this.adjacencylist = new LinkedList<Edge>();
 		this.num = num;
 	}
 	/**
 	 * cette méthode permet d'ajouter une arête à la liste des arêtes reliant ce sommet à ses voisins
 	 * @param e l'arête à ajouter
 	 */
 	
 	public void addNeighbor(Edge e) {
 		this.adjacencylist.addFirst(e);
 	}
 }

 /**
  * // classe interne pour définir un graphe.
  * 
  */
 static class Graph {
     /**
      * liste des sommets du graphe
      */
	 ArrayList<Vertex> vertexlist;
    /**
     * nombre de sommet constituant le graphe, cela correspondra aussi au numéro du sommet créé à chaque fois qu'un sommet est créé
     */
	 int num_v = 0;
	 /**
	  * Constructeur qui permet de  définir un graphe
	  */
     Graph() {
         vertexlist = new ArrayList<Vertex>();
     }

     /**
      * méthode qui permet d'ajouter un sommet au graphe
      * @param indivTime temps nécessaire pour parcourir horizontalement ou verticalement ce sommet (case) 
      */
     public void addVertex(double indivTime)
     {
     	Vertex v = new Vertex(num_v);
     	v.indivTime = indivTime;
     	vertexlist.add(v);
     	num_v = num_v + 1;
     }
     /**
      * méthode permettant d'ajouter une arête
      * @param source sommet source
      * @param destination sommet de distination
      * @param weight poids de l'arête
      */
     public void addEgde(int source, int destination, double weight) {
         Edge edge = new Edge(source, destination, weight);
         vertexlist.get(source).addNeighbor(edge);
     }

 }
 

}
