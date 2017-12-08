# README #

POC de blockchain en Java 9.

Rien de bien avancé ou utilisable pour le moment.

# Fait 
- Création de la chaîne et du block Genesis
- Minage de block avec les données en attente et preuve de travail
- Alimentation des données en attente depuis le réseau


# A faire

### Construction de la chaîne et minage
- [x] Création de la chaîne et du block Genesis
- [x] Minage de bloc et hachageavec
- [x] Gestion des données en attente (alimentation, piochage aléatoire)
- [x] Preuve de travail
- [ ] Reconstruction de la chaîne existante pour un nouveau noeud
- [ ] Envoie de la chaîne à un nouveau noeud
- [ ] Gestion des forks

### Sécurité
- [x] Vérification de l'intégrité de la chaîne par le liage des blocs par hash
- [ ] Calcul de la racine de Merkle d'un bloc
- [ ] Vérification de l'intégrité de la chaîne par l'arbre de Merkle

### Réseau
- [x] Mapping JSON des données
- [ ] Agent UDP pour envoyer un broadcast de découverte de pairs
- [ ] Agent UDP qui traite les demandes de découvertes reçues
- [ ] Manager TCP qui maintient les connexions avec les pairs connus
- [ ] Agent TCP client qui envoie les blocs créés
- [ ] Agent TCP serveur qui reçoit les données et blocs depuis le réseau

### Stockage
- [ ] Couche de persistance LevelDB pour la chaîne.
- [ ] Stockage en temps réel
- [ ] Chargement au démarrage

### Utilisation
- [ ] Création de données