#include <stdio.h>
#include <stdlib.h>
#include<string.h>
#include "prim.h"


arete aretes[100]; // ensembles des arêtes du graphe dont on cherche l'arbre couvrant de poids minimal

arete arbreCouvrant[100]; // l'arbre couvrant de poids minimal obtenu

int nombreAretes = 0; // // initialiser le nombre d'arêtes du graphe à 0

int nombreAretesArbreMst =0; // initialiser le nombre d'arêtes de l'arbre couvrant à 0

sommet sommetMarque[100]; // tableau correspondant aux sommets marqués après chaque étape de l'algorithme de Prim

int nombreDeSommetMarque = 0;  // initialiser le nombre de sommet marqués à 0


/* NB: En plus de l'affichage qui accompagne chaque étape de l'algorithme, l'arbre couvrant de poids minimal est affiché à la fin sur le terminal et il est ecrit aussi dans un fichiers texte qui est dans le même dossier que le fichier graphe.txt*/

int main(){

	printf("\n\n\t\t************ Algorithme de Prim ************\n");
	
	getGraph(); // chargement de notre graphe connexe pondéré depuis un fichier texte 
	
	//marquer le sommet de référence
	strcpy(sommetMarque[nombreDeSommetMarque].nom, aretes[0].v1); 
	
	// incrémenter le nombre de sommet marqué
	nombreDeSommetMarque++; 
	
	//algorithme de Prim
	prim();
	
	printf("\n\n\t\t\t************** FIN **************\n\n");

	
}

