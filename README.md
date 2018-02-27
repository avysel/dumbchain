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
- [x] Couche de persistance LevelDB pour la chaîne.
- [x] Stockage en temps réel
- [x] Chargement au démarrage

### Utilisation
- [ ] Création de données
- [ ] API d'ajout de données
- [ ] Consultation des données
- [ ] API consultation des données
- [ ] Lancement du noeud avec création de données uniquement (pas de minage, pas de stockage de chaine ?)
- [x] Lancement du noeud avec minage ou non en ligne de commande
- [x] Lancement du noeud en mode génération de données de test seulement
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



# Usage
```
java -jar poc-blockchain.jar 
	-properties=/usr/foo/param.properties 
	-mining=1 
	-demoDataGenerator=0
```


### Regular use parameters : 

**-help** displays this help menu.


**-properties=fileName** to specify a properties file (embeded *config.properties* by default). Use *config.properties* as pattern.


**-mining=1** for a mining node. (default)

**-mining=0** for a not mining node.


### Demo parameters : 

**-demoDataGenerator=1** for a demo data generator node.

**-demoDataGenerator=0** for a no demo data generator node. (default)


# Limites
- Si la condition de minage est trop facile a atteindre et que les blocs sont créés trop rapidement, chaque noeud fera avancer sa version de la chaine plus rapidement que le mécanisme de diffusion/concensus. La chaine sera alors dans un état incohérent.
- Si plusieurs noeuds démarrent à vide en même temps, sans au moins un autre noeud déjà en route, ils débuteront chacun leur version de la chaine. La chaine sera alors dans un état incohérent.

# Questions en cours (à répondre, et appliquer dans le code)
- Quelles données d'un bloc utiliser pour calculer le hash ? (actuellement : timestamp, nombre d'iteration de hash et données)
- Comment utiliser l'arbre de merkle pour vérifier la présence d'une donnée.
- Faut-il garantir l'ordre des données dans un bloc ? (Réponse : oui)
- Faut-il faire un catch-up du pool de données ? (Réponse : Non. Une routine doit tourner sur le noeud régulièrement, pour envoyer à tous les noeuds les transactions créées localement et non encore inclues dans un block)
- Comment piocher des données dans le pool pour miner un block ? (actuellement, prises dans l'ordre d'arrivée, minimum 50, maximum 500)

# Liste des classes

**Main** : classe principale, traite les paramètres de lancement et lance la blockchain.

**Blockchain** : classe principale métier, qui orchestre tous les autres composants.

**BlockchainParameters** : fourni les paramètres de contexte (paramètres fixes, valeur des arguments de lancement, contenu du fichier .properties fourni en entrée).

**BlockchainManager** : fourni des opérations de recherche de blocs dnas la blockchain selon différents critères.

**PropertiesManager** : intégré au BlockchainParameters, il gère le fichier .properties.

**Block** : représentation d'un bloc.

**BlockHeader** et **BlockData** : les entêtes et le contenu d'un bloc.

**Genesis** : un *Block* particulier, le bloc de départ de la chain, il est fixe pour toutes les instances de la blockchain.

**ChainPart** : une liste de blocs (une chaine), avec des opérations d'ajout et retrait de bloc, des informations sur la hauteur de la chaine, l'effort de minage ... Cet objet représente un morceau de chaine.

**Chain** : basé sur *ChainPart*, gère la présence d'un bloc *Genesis*. Cet objet est *LA* chaine de la blokcchain.

**ChainBuilder** : stocke la liste des derniers blocs reçus et est capable de les remettre dans l'ordre pour reconstruire un morceau de chaine.

**ChainRequestor** : au démarrage de l'instance, requête les autres noeuds pour rattraper le travail effectué par le reste de la chaine. Passe ensuite le relais au *ChainCatchUpBuilder*.

**ChainCatchUpBuilder** : lors du démarrage de l'instance, après la demande du *ChainRequestor*, rattrape le travail effectué par les autres noeuds du réseau.

**ChainConsensusBuilder** : gère la réception des blocs minés par les autres noeuds, vérifie l'intégrité de ces blocs et de la nouvelle chaine.

**ChainSender** : envoie la partie de la chaine manquante à une nouvelle instance lors de son arrivée sur le réseau.

**ISingleData** : interface qui définit ce que doit être une donnée à inclure dans un bloc.

**SingleData** : une proposition d'implémentation d'une donnée de bloc.

**HashTools** : utilisé pour calculer les hashs.

**MerkleTree** : fourni les services de vérification d'intégrité basés sur l'arbre de Merkle.

**DBManager** : gère le stockage local via LevelDB.

**RandomDataGenerator** : en mode test, génère à intervalle régulier des données de test.

**BlockIntegrityException** : quand un bloc doit être rejecté car il est corrompu.

**ChainIntegrityException** : quand la chaine ne peut être construite ou complétée car elle est corrompue.

**DataPool** : contient les données en attente d'être inclues dans un bloc.

**DataSender** : réenvoie au réseau toutes les données du *DataPool* plus vieilles que le dernier block.

**Miner** : effectue la création des blocs.

**IProof** : interface qui défini comment fournir la règle de minage d'un bloc.

**ProofOfWork** : proposition d'implémentation de la règle de minage par preuve de travail.

**ProofOfStake** : proposition d'implémentation de la règle de minage par preuve d'implication.

**NodeClient** : gère la partie envoi de données au réseau

**NodeServer** : gère la partie récpetion de données depuis le réseau

**ClientProcessor** : gère unitairement chaque connexion entrante.

**PeerManager** : gère les connexions aux autres noeuds

**PeerExplorer** : au démarrage de l'instance, cherche à établir des connexions avec les autres noeuds.

**PeerListener** : est à l'écoute des demandes de connexion de nouveaux noeuds sur le réseau.

**NetworkDataBulk** : encapsule un message envoyé sur le réseau.

**NetworkMessage** : un message envoyé sur le réseau.

**CatchUpRequestMessage** : une demande de rattrapage émise par un nouveau noeud.

**CatchUpDataMessage** : contient les données de rattrapage envoiyé à un nouveau noeud.

**JsonMapper** : serialisation/déserialisation des objets de/vers le format JSon.

**NetworkTools** : utilitaire de fonctions réseau

**Util** : utilitaires divers

# Architecture

# Scénarios

## Démarrage
1. La classe principale *Main* s'exécute, analyse les paramètres de lancement pour former le *BlockchainParameter*, puis crée une *Blockchain* avec ces paramètres

2. La *Blockchain* démarre, en plusieurs étapes
    1. Chargement depuis la base de données locale (*DBManager*)
    2. Démarrage des services réseau (*NetworkManager*).
        1. Démarrage de la partie serveur (*NodeServer*). 
        2. Démarrage du gestionnaire de pairs (*PeerManager*).
            1. Exploration des autres noeuds (*PeerExplorer*).
            2. Démarrage de l'écoute des demandes de connexion des autres pairs (*PeerListener*).
    3. Rattrapage de chaine existante (*ChainRequestor* et *ChainCatchUpBuilder*).
        1. Envoi d'une demande de rattrapage au noeud connu avec la plus longue chaine (*ChainSender*).
        2. Réception des données de rattrapage (*NodeServer*).
        3. Construction de la chaine (*ChainCatchUpBuilder*).
    4. Démarrage du minage (*Miner*)

    
## Réception d'une demande de connexion d'un pair
1. Lecture du message et collecte des données (*PeerListener*).
2. Ajout du pair à la liste de pairs connus (*PeerManager*).
3. Envoi d'une réponse au pair, avec notre ip, notre port d'écoute et la taille de notre chaine (*PeerListener*).

## Réception d'une réponse d'un pair à notre demande de connexion
1. Lecture du message et collecte des données (*ClientProcessor*).
2. Ajout du pair à la liste des pairs connus (*PeerManager*).

## Réception d'une demande de rattrapage d'un pair
1. Lecture du message et collecte des données (*ClientProcessor*).
2. Envoie des bloc manquants ou d'un message indiquant qu'il n'y a rien à rattraper (*ChainSender*).

## Minage d'un bloc
1. Sélection de données en attente (*DataPool*).
2. Création d'un block et calcul d'un hash respectant la condition (*Miner*, *HashTool*, *MerkleTree* et *IProof*).
3. Ajout du bloc créé à la chaine (*Blockchain*).
4. Envoi du bloc sur le réseau (*NodeClient*);

## Réception d'un bloc depuis le réseau   
1. Lecture du message et collecte des données (*ClientProcessor*).
2. Reconstruction du block reçu et envoi à la blockchain (*NetworkManager*, *Blockchain*).
3. Vérification de l'intégrité du bloc et de la nouvelle chaine, et ajout du nouveau bloc (*ChainConsensusBuilder*).
4. Sauvegarde de la chaine en local (*DBManager*).

