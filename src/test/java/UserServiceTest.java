package org.example.demo2.service;

import org.example.demo2.model.Utilisateur;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class UtilisateurServiceTest {

    private UtilisateurService utilisateurService;

    @BeforeEach
    public void setUp() {
        // Instancie le service avant chaque test
        utilisateurService = new UtilisateurService();
    }

    @Test
    public void testCreerUtilisateur() {
        // Crée un utilisateur de test
        Utilisateur user = utilisateurService.creerUtilisateur("testuser", "password123");

        // Vérifie que l'utilisateur n'est pas null
        assertNotNull(user, "L'utilisateur créé ne doit pas être null");

        // Vérifie que le pseudo est correct
        assertEquals("testuser", user.getPseudo(), "Le pseudo doit correspondre à celui fourni");

        // Vérifie que l'id est positif
        assertTrue(user.getId() > 0, "L'id de l'utilisateur doit être supérieur à 0");
    }

    @Test
    public void testGetUtilisateurParId() {
        // Crée un utilisateur
        Utilisateur user = utilisateurService.creerUtilisateur("anotheruser", "password456");

        // Récupère l'utilisateur par ID
        Utilisateur fetchedUser = utilisateurService.getUtilisateurParId(user.getId());

        assertNotNull(fetchedUser, "L'utilisateur récupéré ne doit pas être null");
        assertEquals(user.getPseudo(), fetchedUser.getPseudo(), "Le pseudo doit correspondre");
    }

    @Test
    public void testAuthentification() {
        // Crée un utilisateur
        Utilisateur user = utilisateurService.creerUtilisateur("authuser", "secret");

        // Simule la connexion
        boolean loginResult = utilisateurService.connexion("authuser", "secret");
        assertTrue(loginResult, "L'utilisateur doit être authentifié avec le bon mot de passe");

        // Vérifie que l'utilisateur est bien marqué comme connecté
        assertTrue(utilisateurService.estAuthentifie(), "L'utilisateur doit être authentifié");

        // Déconnexion
        utilisateurService.deconnexion();
        assertFalse(utilisateurService.estAuthentifie(), "L'utilisateur doit être déconnecté");
    }
}