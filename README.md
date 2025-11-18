# Java_Bonnes_Pratiques

##//// Présentation du projet ////

Ce projet consiste à développer une application de chat TCP client/serveur en Java tout en respectant les bonnes pratiques d’architecture logicielle :

- Séparation stricte des responsabilités (S.R.P)

- Architecture par packages cohérente

- Utilisation de classes dédiées (network, controller, model, utils…)

- Externalisation complète de la configuration

- Code clair, maintenable et évolutif

- Utilisation de Maven/Gradle pour la gestion des dépendances


##//// Mise à jour maîtrisée d’une dépendance ////

Nous avons effectué une mise à jour maîtrisée de la dépendance Apache Commons IO pour démontrer notre capacité à gérer les dépendances de manière professionnelle.

Procédure suivie :

- Recherche de la version la plus récente sur Maven Central

- Lecture des release notes

- Mise à jour du fichier build.gradle.kts

- Rafraîchissement du projet avec la commande : ./gradlew clean build

- Exécution des tests unitaires

- Validation fonctionnelle (client ↔ serveur)


##//// Comment lancer le serveur et le client ////

Depuis le dossier serveur (ou client) :

./gradlew run
