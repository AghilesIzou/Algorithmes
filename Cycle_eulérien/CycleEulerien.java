package question_20;

import java.util.ArrayList;

import java.util.HashMap;
import java.util.HashSet;

/**
 * cette classe propose une méthode qui permet d'obtenir un cycle eulérien à partir d'un graphe orienté 
 * @author Aghiles IZOUAOUEN
 * @version version finale
 */

public class CycleEulerien {

	public static void main(String[] args) {
		
		//Définition du graphe orienté de la question 11  comportant plusieurs cycles eulériens
		
		HashMap<String, ArrayList<String>> grapheB = new HashMap<String, ArrayList<String>>();	
		
		ArrayList<String> voisinTA =  new ArrayList<String>();
		voisinTA.add("AC");
		grapheB.put("TA", voisinTA);
		
		ArrayList<String> voisinAC =  new ArrayList<String>();
		voisinAC.add("CC");
		voisinAC.add("CG");
		grapheB.put("AC", voisinAC);
		
		ArrayList<String> voisinCC =  new ArrayList<String>();
		voisinCC.add("CG");
		grapheB.put("CC", voisinCC);
		
		ArrayList<String> voisinCG =  new ArrayList<String>();
		voisinCG.add("GC");
		voisinCG.add("GT");
		grapheB.put("CG", voisinCG);
		
		ArrayList<String> voisinGC =  new ArrayList<String>();
		voisinGC.add("CA");
		grapheB.put("GC", voisinGC);
		
		ArrayList<String> voisinCA =  new ArrayList<String>();
		voisinCA.add("AC");
		grapheB.put("CA", voisinCA);
		
		ArrayList<String> voisinGT =  new ArrayList<String>();
		voisinGT.add("TA");
		grapheB.put("GT", voisinGT);
		
		ArrayList<String> cycleEulerien = new  ArrayList<>();
		try {
			
			cycleEulerien = construireUnCycleEulérien(grapheB,  "AC");
		
		}catch(IllegalArgumentException e ) {
			System.err.println(e.getMessage());
			System.err.println("Fin du programme.");
			System.exit(0);
		}
		
		// réponse de la question 21
		System.out.println("");
		System.out.println("|=====================================================================================================|");
		System.out.println("|                Affichage du cycle eulérien obtenu                                                   |");
		System.out.println("|=====================================================================================================|\n");
		
		for (String sommet : cycleEulerien) {
			System.out.print(sommet+"\t");
		}
		
		
	}

	/**
	 * Cette méthode permet d'obtenir un cycle eulérien à partir d'un graphe et un sommet de départ
	 * @param graphe le graphe à partir duquel on construit un cycle eulérien
	 * @param sommet l'origine du cycle eulérien
	 * @return un cycle eulérien sous forme d'arrayList
	 * @throws IllegalArgumentException si le sommet passé en paramètre n'appartient pas au graphe
	 */
	
	public static ArrayList<String> construireUnCycleEulérien(HashMap<String, ArrayList<String>> graphe, String sommet) throws IllegalArgumentException {
		if(!graphe.containsKey(sommet)) {
			throw new IllegalArgumentException("Erreur ! ce sommet n'appartient pas au graphe.");
		}
		
		HashSet<String> sommetsMarquees = new HashSet<>();
		ArrayList<String> cycleEulerien = new ArrayList<>();
		HashMap<String,Integer> degreEntrantSommet = new HashMap<String,Integer>();
		
		int degreEntrant=0;
		//stocker dans un objet de type HashMap tous les sommets du graphe ainsi que leur degrés entrant
		for (String sommetA : graphe.keySet()) {
			
			
			degreEntrant =0;
			
			for (ArrayList<String> voisins : graphe.values()) {
				
				if(voisins.contains(sommetA)) {
					degreEntrant++;
				}
			}
			
			degreEntrantSommet.put(sommetA, degreEntrant++);
		
		}
		
		cycleEulerien.add(sommet);
		
		String sommetCourant = sommet;	
		
		
		
		boolean continuer = true;
		
		while(continuer) {
			continuer = false;
			ArrayList<String> voisins = graphe.get(sommetCourant);
			
			for (String voisin : voisins) {
				
				if(!sommetsMarquees.contains(voisin)) {
					int degreEntVoisin = degreEntrantSommet.get(voisin);
					
					
					sommetCourant= voisin;
					cycleEulerien.add(voisin);
					degreEntrantSommet.put(voisin, degreEntVoisin-1);
					degreEntVoisin = degreEntrantSommet.get(voisin);
					if(degreEntVoisin==0) {
						sommetsMarquees.add(voisin);
					}
					continuer = true;
					break;
				}
			}
			
		}
		
		
		return cycleEulerien;	
	
	
	}


}
