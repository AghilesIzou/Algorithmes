#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include "kruskal.h"

extern arete *premireArete; /* pointeur sur la première arête de la liste chaînée d'arête du graphe */ 
extern arete *premiereAreteMST; /* pointeur sur la première arête de la liste chaînée d'arête de l'arbre couvrant de poids Min.*/
extern int nombreAretes; // nombre d'arêtes 
extern sommet *tabSommets[70]; // un tableau de liste chaînée de sommet // 
extern int nombreSommets; //nombre de sommets


// lecture du graphe dont on cherche l'arbre couvrant de poids minimal  à partir d'un fichier texte
void getGraph(){

	FILE *input;
	input = fopen("graphe.txt","r");
	char v1[10], v2[10];
	float poids;
	while(!feof(input)){

		fscanf(input,"%s %s %f", v1,v2,&poids);
		nombreAretes++;
		ajoutAreteInitiale(v1,v2,poids); /* ajouter les arêtes du graphe  dans une liste chaînée distinée à contenir toutes les arêtes du graphe  */
	}
	fclose(input);
} 


// ajouter les arêtes du graphe  dans une liste chaînée distinée à contenir toutes les arêtes du graphe
void ajoutAreteInitiale(char v1[10],char v2[10], float poids){

	arete *e1, *e2;
	e1 = (arete *) malloc (sizeof(arete));
	e1 -> next =NULL;
	strcpy(e1->v1,v1);
	strcpy(e1->v2,v2);
	e1->poids = poids;
	//ajout de la première arête à la liste chaînée d'arêtes
	if(premireArete==NULL){
		
		premireArete=e1;
	}else{

		for(e2 = premireArete; e2->next !=NULL; e2 = e2->next); /*ajout de la première arête à la liste chaînée d'arêtes*/
		e2->next =e1;
	}

} 



/* création d'un tableau qui va contenir des listes chaînées de sommet, on ajoute d'abord les sommets dont l'arête est de plus petit poids, NB : on ajoute seulement des sommets qui n'appartiennent pas à la liste en question */

void tabListesDeSommets(){

	sommet *s;
	int b; // un boolean
	arete *n;
	s = (sommet *) malloc(sizeof(sommet));
	s->next=NULL;
	strcpy(s->nom, premireArete->v1);

	tabSommets[nombreSommets]= s; //ajouter une liste chaînée de sommet à la case d'indice nombreSommets
	nombreSommets++; //incrémenter le nombre de listes chaînées de sommet

	for(n= premireArete; n!=NULL; n=n->next){

		b=1;

		//ajouter les sommets au tableau 	
		for(int i=0;i<nombreSommets;i++){
			//vérifie si le sommet n'existe pas déjà dans le tableau
			if(strcmp(n->v1,tabSommets[i]->nom)==0){
				b=0;
			}
		}
		if(b==1){

			s= (sommet *) malloc(sizeof(sommet));
			s->next=NULL;
			strcpy(s->nom,n->v1);
			tabSommets[nombreSommets]=s;
			nombreSommets++; // incrémenter le nombre de sommets à chaque fois qu'une arête est ajouté dans le tableau
		}
		b=1;
		for(int i=0; i<nombreSommets;i++){

			if(strcmp(n->v2,tabSommets[i]->nom)==0){
				b=0;
			}
		}
		if(b==1){

			s= (sommet *) malloc(sizeof(sommet));
			s->next=NULL;
			strcpy(s->nom,n->v2);
			tabSommets[nombreSommets]=s;
			nombreSommets++; // incrémenter le nombre de sommets à chaque fois qu'une arête est ajouté dans le tableau
		}
	


	}


}

// chercher l'arête de poids min dans la liste  chainée contenant ces arêtes et retourner un pointeur sur celle-ci
arete * aretePoidsMin(){

	arete *n, *n1;
	float min;
	//si la liste chaînée d'arêtes de départ est vide
	if(premireArete==NULL){
	
		return NULL;
	}
	else{ 

		n1=premireArete;
		min = premireArete-> poids;
		for(n=premireArete; n!=NULL; n=n->next){ /*chercher l'arete de poids min dans la liste chaînée contenant ces arêtes*/
			
			if(n->poids <min){
				n1=n;
				min = n->poids;
			}
		}
		return n1; // retourner l'arête de poids min 


	}

}






/* affichage des structures intermédiaire à chaque étape lors de la construction de l'arbre couvrant de poids minimal ainsi que l'arbre obtenu à chaque étape jusqu'à l'obtention d'un arbre couvrant de poids min */
void afficher(){

	printf("\n-------------------------------------------------------------------\n");
	
	printf("État actuel : \n");
	// affichage des arêtes du graphe de départ
	printf("\ntoutes les arêtes du graphe connexe pondéré\n");

	arete *e;
	for(e= premireArete; e!=NULL; e=e->next){
		printf("\n%s %s %0.2f",e->v1, e->v2, e->poids);

	}

	// affichage des listes chaînées de sommet 
	printf("\n\ntoutes les listes chaînées de sommet\n");
	sommet *s;

	for(int i=0; i<nombreSommets;i++){
		
		printf("\n[%d]", i);
		
		for(s=tabSommets[i]; s!=NULL; s=s->next){
			
			printf("(%s) ",s->nom);
		}

	}

	// affichage des arêtes de l'arbre obtenu  à chaque étape et l'arbre couvrant de poids Min à la fin 

	
	printf("\n\nLes arêtes de l'arbre couvrant et leurs poids respectifs à chaque étape");

	for(e = premiereAreteMST; e!=NULL; e=e->next ){
		printf("\n%s %s %0.2f",e->v1, e->v2, e->poids);

	}
	printf("\n");
}


// écrire l'arbre couvrant de poids minimal obtenu dans un fichier texte 
void EcrireArbreCouvrant(){

	FILE *output;
	output = fopen("arbreCouvrant.txt","w");
	arete *e;

	for(e=premiereAreteMST; e!=NULL; e=e->next){
		fprintf(output,"%s %s %0.2f\n", e->v1, e->v2, e->poids); //écrire chaque arête dans le fichier texte	
	}
	
	fclose(output); // fermeture du fichier
}

// ajouter le deuxième sommet à la liste chaînée du premier sommet

void ajouterDeuxAuPremi(int i1,int i2){

	sommet *s;
	for(s=tabSommets[i1]; s->next !=NULL; s=s->next);
	s-> next = tabSommets[i2]; // ajouter le sommeet 2 à la liste contenant le sommet 1
	tabSommets[i2] = NULL; // supprimer la liste contenant le sommet 2 du tableau de listes



}

/*chercher l'indice des listes contenant les 2 sommets d'une arête de poids min afin qu'on puisse par la suite ajouter le deuxième sommet à la liste du premier sommet et supprimer la liste du deuxième sommet */
void indListesContenantDeuxSommets(char v1[10], char v2[10], int *i1, int *i2){

	// i1 indice de la liste contenant v1
	// i2 indice de la liste contenant v2

	
	sommet *s;

	for(int i=0; i<nombreSommets;i++){

		for(s=tabSommets[i]; s!=NULL; s=s->next){

			if(strcmp(s->nom,v1)==0){
				*i1=i;        //indice de la liste du premier sommet
			}
			if(strcmp(s->nom,v2)==0){
				*i2=i;    //indice de la liste du deuxième sommet
			}
		}
	}

}

/*cette fonction permet d'ajouter l'arête de poids min à l'arbre couvrant de poids Min  */

void ajouterAreteDePoidSMinMST(char v1[10] ,char v2[10], float poids){


	arete *e= (arete *) malloc (sizeof(arete));
	e-> next = NULL;
	strcpy(e->v1,v1);
	strcpy(e->v2,v2);
	e->poids = poids;
	arete *e1;


	if(premiereAreteMST==NULL){ //si arbre vide 

		premiereAreteMST=e; //il contiendra cette premiere arête

	}else{
		//e1 devient la première arête et e devient l'arête suivante
		for(e1=premiereAreteMST; e1->next != NULL ; e1=e1->next);
		e1->next=e;

	}
	

}

//supprimer l'arête de poids min de la liste chaînée d'arêtes initiale

void supprimerAretePMin(arete *e){

	//supprmier l'arete si elle est à la tête de la liste
	if(e==premireArete){
		
		premireArete= premireArete->next;
		free(e);
	}else{
		arete *e1;
		for(e1=premireArete; e1->next !=e; e1 = e1->next);
		e1->next = e->next;	
		free(e);
	}

}


//cette fonction correspond à l'algorithme de kruskal avec l'ensemble des étapes pour construire l'arbre couvrant de poids minimal
void kruskal(){
	
	afficher(); //affichage du contenu de chaque structure à chaque appel à la fontion kruskal()
	
	//arrêter le programme et écrire l'arbre couvrant obtenu dans un fichier texte 
	if( premireArete== NULL){ //si toutes les arêtes on été supprimé de la liste de départ
		EcrireArbreCouvrant();
		return;

	}

	int boolean;
	arete * areteDePoidsMin = aretePoidsMin(); // chercher l'arête de poids min dans le graphe de départ
	printf("------------------------------------------------------------------------------");
	printf("\n(%s %s %0.2f) c'est la nouvelle arête de poids Min\n",areteDePoidsMin->v1,areteDePoidsMin->v2,areteDePoidsMin->poids);
	int i1, i2; //indice de deux sommets reliés par une arête

	indListesContenantDeuxSommets(areteDePoidsMin->v1,areteDePoidsMin->v2,&i1,&i2);
	//si les deux sommets ne sont pas dans la même liste chaîné donc leur indice de chaîne sont différent
	if(i2!= i1){

		ajouterDeuxAuPremi(i1,i2); /* ajouter le deuxième sommet à la liste chaînée du premier sommet */
		ajouterAreteDePoidSMinMST(areteDePoidsMin->v1,areteDePoidsMin->v2,areteDePoidsMin->poids); /*Ajouter l'arête de poids min à l'arbre couvrant de poids Min  */
		//supprimer l'arête de poids min de la liste chaînée d'arêtes initiale
		supprimerAretePMin(areteDePoidsMin);




	}else{
		/*si les deux sommets de l'arête de poids min sont da la même liste cela veut dire qu'ils appartiennent déja à l'arbre en cours de construction */

		//supprimer l'arête de poids min de la liste chaînée d'arêtes initiales
		supprimerAretePMin(areteDePoidsMin);
	}

	// kruskal() est une fonction récursive, tant que  premireArete != NULL on l'appelle à nouveau.
	kruskal();

}
