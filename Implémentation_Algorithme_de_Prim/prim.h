#ifndef PRIM_H
#define PRIM_H

// déclaration de la structure arete

typedef struct arete arete;
struct arete{
	char v1[10]; //nom du premier sommet
	char v2[10]; //nom du deuxième sommet
	float poids; // poids de l'arête
};

// déclaration de la structure sommet
typedef struct sommet sommet;
struct sommet{
	
	char nom[10]; //nom du sommet
}; 


extern arete aretes[100]; 

extern arete arbreCouvrant[100]; 

extern int nombreAretes; 

extern int nombreAretesArbreMst; 

extern sommet sommetMarque[100]; 

extern int nombreDeSommetMarque;



void getGraph();
void EcrireArbreCouvrant();
void addAreteInArbreC(int indexArete);
void addSommet(int indexArete);
void afficher();
int indexAretePminMarquee();
void prim(); 

#endif
