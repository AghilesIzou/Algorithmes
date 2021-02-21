#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include "kruskal.h"


arete *premireArete = NULL; /* pointeur sur la première arête de la liste chainé d'arête du graphe */
arete *premiereAreteMST = NULL; /* pointeur sur la première arête de la liste chainé d'arête de l'arbre couvrant de poids Min.*/
int nombreAretes=0; // nombre d'arêtes
sommet *tabSommets[70]; // un tableau de listes chaînées de sommet // treesarray
int nombreSommets=0; // initialiser le nombre de sommet à 0

/* NB: En plus de l'affichage qui accompagne chaque étape de l'algorithme, l'arbre couvrant de poids minimal obtenu est affiché à la fin sur le terminal et il est ecrit aussi dans un fichiers texte qui est dans le même dossier que le fichier graphe.txt*/

int main(){

	printf("\n\n\t\t************ Algorithme de Kruskal ************\n");
	
	getGraph(); // chargement du graphe connexe pondéré à partir d'un fichier texte 
	
	tabListesDeSommets(); /* créer un tableau de listes chaînée de sommets (chaque case du tableau contiendra une liste chaînée de sommets, et à la fin une des listes contiendra l'ensemble des sommets qui représente l'arbre couvrant de poids Min) */
	
	
	//algorithme de kruskal
	kruskal();
	
	printf("\n\n\t\t\t************** FIN **************\n\n");

	
}

