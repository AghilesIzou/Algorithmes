#include<stdio.h>
#include<stdlib.h>
#include<string.h>
#include "prim.h"


extern arete aretes[100]; 

extern arete arbreCouvrant[100]; 

extern int nombreAretes; 

extern int nombreAretesArbreMst; 

extern sommet sommetMarque[100]; 

extern int nombreDeSommetMarque; 



// lecture du graphe dont on cherche l'arbre couvrant de poids minimal  à partir d'un fichier texte
void getGraph(){

	FILE *input;
	input = fopen("graphe.txt","r");
	while(!feof(input)){

		fscanf(input,"%s %s %f", aretes[nombreAretes].v1, aretes[nombreAretes].v2, &aretes[nombreAretes].poids);
		nombreAretes++;
	}
	fclose(input);
} 



int indexAretePminMarquee(){

	/*récupérer toutes les arêtes marquées (arêtes candidates) en récupérant seulement leurs indexes et les mettres dans un 	tableau. */
	int b1, b2;

	int indexesAretesMarques[100], nombreAretesMarque=0;
	// chercher les arêtes candidates de chaque sommet marquée (qui fait partie de l'arbre  non final obtenu)
	for(int i =0; i<nombreAretes;i++){

		b1=1, b2=1;

		for(int j=0; j<nombreDeSommetMarque;j++){

			if(strcmp(sommetMarque[j].nom, aretes[i].v1)==0){
			
				b1=0;
			}


			if(strcmp(sommetMarque[j].nom, aretes[i].v2)==0){
			
				b2=0;
			}
		}
		// si l'arête a une seule extrémité dans l'arbre on la marque.
		if((b1==0 && b2==1) || (b1==1 && b2==0)){
			indexesAretesMarques[nombreAretesMarque]=i;
			nombreAretesMarque++;
		}
		
	}
	// arreter l'algorithme si aucune arêtes n'a été marqué
	if(nombreAretesMarque==0){

		return -1;
	}
	
	// récupérer le poids  de la première arête marquée
	float min = aretes[indexesAretesMarques[0]].poids;
	
	// récupérer l'indice de la première arête marquée
	int lowestMarkedArete = indexesAretesMarques[0];
	
	//chercher l'indice de l'arête de plus petit poids marquée
	for(int i=0 ; i<nombreAretesMarque; i++){


		if(aretes[indexesAretesMarques[i]].poids < min){
			min = aretes[indexesAretesMarques[i]].poids;

			lowestMarkedArete = indexesAretesMarques[i];

		}

	}
	//retourner l'indice de l'arête de plus petit poids marquée
	return lowestMarkedArete;


}

// affichage des arêtes du graphe de départ ainsi que les sommets marqués et l'arbre en cours de construction
void afficher(){

	printf("\n-------------------------------------------------------------------\n");
	// affichage des arêtes du graphe de départ
	printf("\ntoutes les arêtes du graphe connexe pondéré\n");

	for(int i=0; i<nombreAretes-1;i++){
		printf("\n[%d] %s %s %0.2f",i,aretes[i].v1, aretes[i].v2, aretes[i].poids);

	}

	// affichage des sommets marqués à chaque étape
	printf("\n\nLes sommets marqués\n");

	for(int i=0; i<nombreDeSommetMarque;i++){
		printf("\n%s", sommetMarque[i].nom);

	}

	// affichage des arêtes de l'arbre en cours de construction
	printf("\n\nLes arêtes de l'arbre couvrant et leurs poids respectifs\n");

	for(int i=0; i<nombreAretesArbreMst;i++){
		printf("\n%s %s %0.2f",arbreCouvrant[i].v1, arbreCouvrant[i].v2, arbreCouvrant[i].poids);

	}
	printf("\n");
}

//fonction qui permet d'ajouter une arête dont l'indice est passé en paramètre  à notre arbre en cours de construction

void addAreteInArbreC(int indexArete){

	strcpy(arbreCouvrant[nombreAretesArbreMst].v1, aretes[indexArete].v1);
	strcpy(arbreCouvrant[nombreAretesArbreMst].v2, aretes[indexArete].v2);
	arbreCouvrant[nombreAretesArbreMst].poids =  aretes[indexArete].poids;
	nombreAretesArbreMst++;
}

/*fonction qui permet de marquer un nouveau sommet pour l'ajouter à la liste des sommets qui font partie de notre  arbre en cours de construction */
void addSommet(int indexArete){

	int b1=1, b2=1;

	for(int i=0; i<nombreDeSommetMarque;i++){

		if(strcmp(sommetMarque[i].nom, aretes[indexArete].v1)==0){
			
				b1=0;
		}


		if(strcmp(sommetMarque[i].nom, aretes[indexArete].v2)==0){
			
				b2=0;
		}


	}

	if(b1==1){

		strcpy(sommetMarque[nombreDeSommetMarque].nom, aretes[indexArete].v1);
		nombreDeSommetMarque++;
	}
	
	if(b2==1){

		strcpy(sommetMarque[nombreDeSommetMarque].nom, aretes[indexArete].v2);
		nombreDeSommetMarque++;

	}


}

// écrire l'arbre couvrant de poids minimal obtenu dans un fichier texte 
void EcrireArbreCouvrant(){

	FILE *output;
	output = fopen("arbreCouvrant.txt","w");

	for(int i =0; i<nombreAretesArbreMst; i++){
		fprintf(output,"%s %s %0.2f\n", arbreCouvrant[i].v1, arbreCouvrant[i].v2, arbreCouvrant[i].poids);	
	}
	
	fclose(output);
}

//cette fonction correspond à l'algorithme de prim avec l'ensemble des étapes pour construire l'arbre couvrant de poids minimal
void prim(){
	
	// obtenir l'indexe de l'arete candidate de poids minimal marquée
	int indexAreteDePoidsMin = indexAretePminMarquee(); 
	
	//arrêter le programme et écrire l'arbre couvrant obtenu dans un fichier texte 
	if( indexAreteDePoidsMin== -1){
		EcrireArbreCouvrant();
		return;

	}

	addAreteInArbreC(indexAreteDePoidsMin);
	addSommet(indexAreteDePoidsMin);

	/* à chaque étape de l'algorithme cela affiche les arêtes du graphe de départ ainsi que les sommets marqués et l'arbre en cours de construction*/
	afficher();
	// prim est une fonction récursive, tant que  indexAreteDePoidsMin n'est pas égal à -1 on l'appelle à nouveau.
	prim();

}
