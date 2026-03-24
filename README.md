# Projet-JAVA-ESEO

Projet Java destine aux etudiants ESEO. C'est une application de type forum (comme Reddit) qui permet aux utilisateurs de poster du contenu, de commenter et d'interagir avec d'autres membres.

## Fonctionnalites principales

- Creation de compte utilisateur et connexion
- Creation de subreddits (communautes)
- Creation de posts dans les subreddits
- Ajout de commentaires sur les posts
- Systeme de likes sur les posts

## Base de donnees

Le projet utilise une base de donnees MySQL pour stocker toutes les donnees de l'application.

### Configuration de la connexion

Les informations de connexion sont definies dans le fichier `src/main/java/org/example/demo2/util/DatabaseConnection.java` :

- **URL** : `jdbc:mysql://localhost:3306/bdd_forum`
- **Utilisateur** : `root`
- **Mot de passe** : (vide par defaut)

Si vous avez un mot de passe MySQL, modifiez le fichier correspondint.

### Creer la base de donnees

1. Assurez-vous que MySQL est en cours d'execution sur votre machine.
2. Ouvrez MySQL (via phpMyAdmin, MySQL Workbench ou la ligne de commande).
3. Importez le fichier `bdd_forum.sql` fourni dans le projet. Ce fichier contient toutes les tables necessaires.


### Tables et relations

Voici les principales tables de la base de donnees :

- **utilisateur** : Contient les informations des utilisateurs (id, pseudo, email, mot de passe, karma).
- **subreddit** : Les communautes creees par les utilisateurs (id, nom, description).
- **post** : Les publications dans les subreddits (id, titre, contenu, score, id_utilisateur, id_subreddit).
- **commentaire** : Les commentaires sur les posts (id, contenu, score, id_utilisateur, id_post, id_parent).
- **vote** : Les likes sur les posts et commentaires (id, type_vote, id_utilisateur, id_post, id_commentaire).
- **abonnement** : Les abonnements aux subreddits (id_utilisateur, id_subreddit).
- **message** : Les messages entre utilisateurs.
- **moderation** : Les permissions de moderation des subreddits.
- **signalement_post** et **signalement_commentaire** : Les signalements de contenu.

### Relations entre les tables

- Un **utilisateur** peut creer plusieurs **posts** et **commentaires**.
- Un **post** appartient a un **subreddit** et peut avoir plusieurs **commentaires**.
- Un **commentaire** peut avoir un **commentaire parent** (reponse).
- Un **utilisateur** peut voter sur des **posts** et **commentaires** (table vote).
- Un **utilisateur** peut s'abonner a plusieurs **subreddits**.

## Tests

Cette section decrit tous les tests unitaires du projet. Les tests permettent de verifier que chaque partie du code fonctionne correctement.

### UtilisateurServiceTest

Tests pour la gestion des utilisateurs.

- **testInscrire** : Verifie qu'un utilisateur peut s'inscrire avec un email unique. Controle egalement qu'on ne peut pas s'inscrire deux fois avec le meme email. Resultat attendu : inscription reussie, echec si email deja utilise.

- **testLogin** : Teste la connexion avec le bon mot de passe et la deconnexion. Verifie que l'utilisateur est authentifie apres connexion et deconnecte apres logout. Resultat attendu : connexion et deconnexion reussies.

- **testGetUtilisateurParId** : Recupere un utilisateur par son ID et verifie que les informations sont correctes. Resultat attendu : les donnees de l'utilisateur correspondent.

### PostServiceTest

Tests pour la gestion des posts.

- **testCreerPost** : Cree un post avec un titre et du contenu. Verifie que le post est bien cree. Resultat attendu : le post apparait dans la liste des posts.

- **testCreerPostTitreVide** : Verifie qu'on ne peut pas creer un post sans titre. Resultat attendu : une erreur est generate.

- **testCreerPostContenuVide** : Verifie qu'on ne peut pas creer un post sans contenu. Resultat attendu : une erreur est generate.

- **testGetPostsParSubreddit** : Recupere tous les posts d'un subreddit. Resultat attendu : tous les posts du subreddit sont retournes.

- **testGetPostsParUtilisateur** : Recupere tous les posts d'un utilisateur. Resultat attendu : tous les posts de l'utilisateur sont retournes.

- **testToggleLike** : Teste le systeme de likes. Ajoute un like puis le retire. Verifie que le score augmente puis revient a la valeur initiale. Resultat预期 : le score change correctement.

- **testADejALike** : Verifie si un utilisateur a deja liké un post. Resultat attendu : retourne vrai apres avoir liké, faux avant.

- **testSupprimerPost** : Supprime un post. Verifie que le post n'existe plus. Resultat attendu : le post est completement supprime.

- **testSupprimerPostNonProprietaire** : Verifie qu'un utilisateur ne peut pas supprimer le post d'un autre. Resultat attendu : le post reste existant.

- **testGetTopPostsParSubreddit** : Recupere les posts les plus populaires d'un subreddit. Resultat attendu : les posts sont tries par score.

### CommentaireServiceTest

Tests pour la gestion des commentaires.

- **testAjouterCommentaire** : Ajoute un commentaire sur un post. Verifie que le commentaire est cree avec le bon contenu. Resultat attendu : le commentaire apparait sous le post.

- **testAjouterCommentaireVide** : Verifie qu'on ne peut pas ajouter un commentaire vide. Resultat attendu : une erreur est generate.

- **testAjouterCommentaireNull** : Verifie qu'on ne peut pas ajouter un commentaire null. Resultat attendu : une erreur est generate.

- **testAjouterReponseCommentaire** : Ajoute une reponse a un commentaire existant. Verifie le lien entre le commentaire parent et la reponse. Resultat attendu : la reponse est liee au bon commentaire parent.

- **testGetCommentairesPost** : Recupere tous les commentaires d'un post. Resultat attendu : tous les commentaires du post sont retournes.

- **testGetNombreCommentaires** : Compte le nombre de commentaires sur un post. Ajoute des commentaires et verifie que le compteur augmente. Resultat attendu : le nombre de commentaires est correct.

- **testSupprimerCommentaire** : Supprime un commentaire. Verifie que le commentaire n'existe plus. Resultat attendu : le commentaire est completement supprime.

## Plan de Tests

### Cas Nominaux (Flux principal)

| Fonctionnalite | Test | Description |
|----------------|------|-------------|
| Inscription | testInscrireValide | Creation de compte avec email unique |
| Connexion | testLoginValide | Connexion avec identifiants corrects |
| Creation post | testCreerPost | Publication d'un post avec titre et contenu |
| Ajout commentaire | testAjouterCommentaire | Reponse a un post |
| Like post | testUpvotePost | Ajout d'un vote positif |
| Desabonnement | testDesabonnerSubreddit | Suppression d'un abonnement |

### Cas d'Erreurs (Validation)

| Fonctionnalite | Test | Description |
|----------------|------|-------------|
| Inscription email existant | testInscrireEmailExistant | Echec si email deja utilise |
| Creation post titre vide | testCreerPostTitreVide | Exception si titre vide |
| Creation post contenu vide | testCreerPostContenuVide | Exception si contenu vide |
| Commentaire vide | testAjouterCommentaireVide | Exception si texte vide |
| Suppression post tiers | testSupprimerPostNonProprietaire | Interdit si pas proprietaire |

### Cas Limites (Frontieres)

| Fonctionnalite | Test | Description |
|----------------|------|-------------|
| Like/Unlike | testToggleLike | Like puis unlike sur meme post |
| Multiple commentaires | testGetNombreCommentaires | Verification du compteur |
| Suppression vote | testSupprimerVoteApresUpvote | Suppression apres vote |
| Titre longueur max | testCreerPostTitreLong | Post avec titre de 255 caracteres |

## Comment executer les tests

### Avec Maven (ligne de commande)

Pour executer tous les tests, utilisez la commande suivante dans le dossier du projet :

```bash
mvn test
```

Cette commande va compiler le projet et lancer tous les tests automatiquement. Si tous les tests passent, vous verrez un message de succes. Si un test echoue, les details de l'erreur seront affiches.

Pour executer un seul fichier de test, vous pouvez specifier le nom de la classe :

```bash
mvn test -Dtest=UtilisateurServiceTest
```

ou pour un test specifique :

```bash
mvn test -Dtest=UtilisateurServiceTest#testLogin
```

### Avec IntelliJ IDEA

1. Ouvrez le projet dans IntelliJ IDEA.
2. Assurez-vous que Maven est configure (le projet doit avoir le logo Maven dans la vue Project).
3. Pour executer tous les tests :
   - Ouvrez la fenetre Maven (bouton "Maven" sur le bord droit ou raccourci : Ctrl+Shift+A puis "Maven").
   - Developpez "Lifecycle" et double-cliquez sur "test".
4. Pour executer un seul fichier de test :
   - Ouvrez le fichier de test (dans src/test/java).
   - Faites un clic droit sur la classe ou une methode specifique.
   - Selectionnez "Run 'NomDeLaClasse'" ou "Run 'NomDeLaMethode'".
5. Les resultats des tests apparaitront dans la fenetre "Run" en bas de l'ecran. Les tests reussis sont en vert, les echecs en rouge.

## Generer un JAR executable

Pour creer un JAR executable avec toutes les dependances incluses :

```bash
mvn clean package
```

Le fichier JAR sera genere dans le dossier `target/`. Pour lancer l'application :

```bash
java -jar target/demo2-1.0-SNAPSHOT.jar
```

## Architecture du projet

Le projet suit une architecture en couches avec les packages suivants :

- **controller** : Gestion de l'interface utilisateur (JavaFX)
- **service** : Logique metier
- **dao** : Acces aux donnees (JDBC)
- **model** : Objets metier
- **util** : Utilitaires (connexion BDD, session, mots de passe)

Les interfaces sont definies dans :
- `dao/interfaces/` pour les DAO
- `service/interfaces/` pour les services

## Documentation Javadoc

### Avec Maven

```bash
mvn javadoc:javadoc
```

La documentation sera générée dans `target/site/apidocs/`.

### Avec IntelliJ IDEA

1. Allez dans `Tools` → `Generate JavaDoc`
2. Configuration :
   - **Whole project** : Sélectionnez cette option
   - **Include test sources** : Décochez cette option
   - **Output directory** : Renseignez `docs`
3. Cliquez sur `OK`

La documentation sera générée dans le dossier `docs/`.

## Diagramme UML

Le diagramme de classes UML du projet est disponible dans le fichier :

- `src/uml.uml`

### Comment générer le diagramme UML avec IntelliJ IDEA

1. Dans la fenêtre du projet, faites un clic droit sur le dossier `src`
2. Sélectionnez `Java Classes` → `Diagram`
3. Une fenêtre affiche toutes les classes avec leurs propriétés, méthodes et relations
4. Pour exporter :
   - Clic droit dans le diagramme
   - Sélectionnez `Export to Image` ou `Save as`
5. Enregistrez le fichier dans le dossier souhaité

Le fichier `src/uml.uml` contient le diagramme généré automatiquement par IntelliJ IDEA.
