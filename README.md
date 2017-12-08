# README #

POC de blockchain en Java 9.

Rien de bien avancé ou utilisable pour le moment.

# Fait 
- Création de la chaîne et du block Genesis
- Minage de block avec les données en attente et preuve de travail
- Alimentation des données en attente depuis le réseau


# A faire

### Minage
- Pool de données orphelines, reçues du réseau
- Vérification des données orphelines, passage en attente si ok (Quelles sont les règles de vérification)
- Pool de données en attente, vérifiées par le noeud et proposées au minage

### Sécurité
- Calcul de la racine de Merkle d'un bloc
- Vérification de l'intégrité de la chaîne par l'arbre de Merkle


### Réseau
- Agent UDP pour envoyer un broadcast de découverte de pairs
- Agent UDP qui traite les demandes de découvertes reçues
- Manager TCP qui maintient les connexions avec les pairs connus
- Agent TCP client qui envoie les blocs créés
- Agent TCP serveur qui reçoit les données et blocs depuis le réseau

### Construction de la chaîne
- Reconstruction de la chaîne existante pour un nouveau noeud
- Envoie de la chaîne à un nouveau noeud
- Gestion des forks

### Stockage
- Couche de persistance LevelDB pour la chaîne.
- Stockage en temps réel
- Chargement au démarrage


### Utilisation
- Création de données