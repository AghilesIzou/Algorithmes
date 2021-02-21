#ifndef KRUSKAL_H
#define KRUSKAL_H

// déclaration de la structure arete

typedef struct arete arete;
struct arete{ 
	char v1[10]; //nom du premier sommet
	char v2[10]; //nom du deuxième sommet
	float poids; // poids de l'arête
	arete *next; // pointeur sur la prochaine arête
};

// déclaration de la structure sommet
typedef struct sommet sommet;
struct sommet{
	
	char nom[10]; //nom du sommet
	sommet *next; // pointeur sur le prochain sommet
}; 


extern arete *premireArete; /* pointeur sur la première arête de la liste chaînée d'arête du graphe */
extern arete *premiereAreteMST; /* pointeur sur la première arête de la liste chaînée d'arête de l'arbre couvrant de poids Min.*/ 
extern int nombreAretes; // nombre d'arêtes 
extern sommet *tabSommets[70]; // un tableau de liste chaînée de sommet
extern int nombreSommets; //nombre de sommet
 


void getGraph();
void ajoutAreteInitiale(char v1[10],char v2[10], float poids);
void tabListesDeSommets();
arete * aretePoidsMin();
void afficher();
void kruskal();
void EcrireArbreCouvrant();
void indListesContenantDeuxSommets(char v1[10], char v2[10], int *i1, int *i2); 
void ajouterDeuxAuPremi(int i1,int i2);
void ajouterAreteDePoidSMinMST(char v1[10] ,char v2[10], float poids);
void supprimerAretePMin(arete *e);

#endif
