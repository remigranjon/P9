Green Code - Plan d'amélioration

Ce projet en microservices consomme des ressources (CPU, mémoire, réseau, stockage).
L'objectif est de réduire cette consommation sans dégrader la qualité fonctionnelle.

1) Conteneurs plus légers
Utiliser des images Java runtime minimales pour les services.
Vérifier que chaque image ne contient que le nécessaire pour exécuter l'application.
Supprimer les dépendances non utilisées dans chaque service.

2) Limiter les ressources par service
Définir des limites CPU et mémoire pour chaque conteneur.
Éviter le surdimensionnement par défaut.
Ajuster selon la charge réelle observée.

3) Réduire les appels réseau
Éviter les appels redondants entre services.
Mettre en cache les données stables (exemple: informations patient peu modifiées).
Regrouper les requêtes quand c'est possible.

4) Optimiser les accès base de données
Ajouter/valider les index sur les requêtes fréquentes.
Limiter les retours volumineux (pagination, filtres).
Éviter les lectures complètes quand seul un champ est utile.

5) Logs simplifiés
Passer en niveau INFO en production (DEBUG seulement en diagnostic).
Éviter les logs verbeux à chaque requête.

6) Front plus sobre
Compresser les ressources statiques.
Réduire le poids des pages.
Charger uniquement ce qui est visible/utilisé.

