# README #

POC de blockchain en Java 9.

Ce projet a pour but de développer un système de stockage de données en utilisant la blockchain.
Il a été créé dans le but de réaliser un exemple de fonctionnement de blockchain, et non pour être réellement utilisé en production. A ce titre, il peut comporter des failles de sécurité, des problèmes d'optimisation, etc ... Il est livré en l'état sans aucune garantie.


# A faire

### Construction de la chaîne et minage
- [x] Création de la chaîne et du block Genesis
- [x] Gestion des données en attente (alimentation, piochage aléatoire)
- [x] Minage de bloc et hachage
- [x] Preuve de travail
- [x] Reconstruction de la chaîne existante pour un nouveau noeud
- [x] Envoi de la chaîne à un nouveau noeud
- [x] Gestion des blocks entrants (vérification d'intégrité, de continuité, d'unicité des données)

### Sécurité
- [x] Vérification de l'intégrité de la chaîne par les hachages
- [x] Calcul de la racine de Merkle d'un bloc
- [ ] Vérification de la présence d'une transaction grâce à l'arbre de Merkle

### Réseau
- [x] Mapping JSON des données
- [x] Manager réseau qui fait le lien avec la chaîne en cours
- [x] Agent UDP pour envoyer un broadcast de découverte de pairs
- [x] Agent UDP qui traite les demandes de découvertes reçues
- [x] Manager qui maintient les connexions avec les pairs connus
- [x] Agent TCP client qui envoie les blocs créés
- [x] Agent TCP serveur qui reçoit les données et blocs depuis le réseau
- [ ] Chiffrer les données transmises

### Stockage
- [ ] Couche de persistance LevelDB pour la chaîne.
- [ ] Stockage en temps réel
- [ ] Chargement au démarrage

### Utilisation
- [ ] Création de données
- [ ] API d'ajout de données
- [ ] Consultation des données
- [ ] API consultation des données
- [ ] Lancement du noeud avec création de données uniquement (pas de minage, pas de chaine stockée)
- [x] Lancement du noeud avec minage ou non en ligne de commande
- [ ] Lancement du noeud en mode génération de données de test seulement
- [x] Possibilité ou non de lancement du noeud sans aucun pair connu

### Technique
- [ ] Initialiser modules Java 9
- [ ] Optimiser la recherche de block et de données avec un index
- [ ] Optimiser BD par utilisation d'un pool de connexions.


### Refactor / Autres
- [ ] Remettre au carré les NetworkMessage vs NetworkDataBulk
- [x] Ajuster les niveaux de log
- [ ] Finaliser TU
- [ ] Javadoc
- [x] Synchroniser les collections avec risque de concurrence
- [x] Contrôle qualité du code (PMD)


### Limites
- Si la condition de minage est trop facile a atteindre et que les blocs sont créés trop rapidement, chaque noeud fera avancer sa version de la chaine plus rapidement que le mécanisme de diffusion/concensus. La chaine sera alors dans un état incohérent.
- Si plusieurs noeuds démarrent à vide en même temps, sans au moins un autre noeud déjà en route, ils débuteront chacun leur version de la chaine. La chaine sera alors dans un état incohérent.


# Usage
`java -jar poc-blockchain.jar -mining=1 -canStartAlone=0 -useNetwork=1 -demoDataGenerator=0`


### Regular use parameters : 

**-help** displays this help menu.

**-mining=1** for a mining node. (default) 
**-mining=0** for a not mining node.

**-canStartAlone=1** for a node that can start alone. (default) 
**-canStartAlone=0** for a node that cannot start without other peer.

**-useNetwork=1** for a node that listen to network to catch new data. (default) 
**-useNetwork=0** for a node that doesn't listen to network to catch new data. (mining is therefore disabled)

### Demo parameters : 

**-demoDataGenerator=1** for a demo data generator node. 
**-demoDataGenerator=0** for a no demo data generator node. (default)
