package org.example.demo2.service;

import org.example.demo2.dao.UtilisateurDao;
import org.example.demo2.model.Utilisateur;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class UtilisateurServiceTest {

    private UtilisateurService utilisateurService;
    private UtilisateurDao utilisateurDao;
    private List<Integer> userIds = new ArrayList<>();

    @BeforeEach
    public void setUp() {
        utilisateurService = new UtilisateurService();
        utilisateurDao = new UtilisateurDao();
    }

    @AfterEach
    public void tearDown() {
        for (Integer id : userIds) {
            try { utilisateurDao.delete(id); } catch (Exception e) { }
        }
        userIds.clear();
    }

    @Test
    public void testInscrire() {
        String uniqueId = String.valueOf(System.currentTimeMillis());
        String uniqueEmail = "test" + uniqueId + "@example.com";
        
        boolean result = utilisateurService.inscrire("testuser" + uniqueId, uniqueEmail, "password123");
        assertTrue(result, "L'inscription doit réussir");

        Utilisateur user = utilisateurDao.getUtilisateurByEmail(uniqueEmail);
        assertNotNull(user, "L'utilisateur doit être créé");
        userIds.add(user.getId());

        boolean resultDuplicite = utilisateurService.inscrire("testuser" + uniqueId, uniqueEmail, "password123");
        assertFalse(resultDuplicite, "L'inscription avec email existant doit échouer");
    }

    @Test
    public void testLogin() {
        String uniqueId = String.valueOf(System.currentTimeMillis());
        String uniqueEmail = "login" + uniqueId + "@test.com";
        utilisateurService.inscrire("loginuser" + uniqueId, uniqueEmail, "secret");

        Utilisateur user = utilisateurDao.getUtilisateurByEmail(uniqueEmail);
        assertNotNull(user);
        userIds.add(user.getId());

        boolean loginResult = utilisateurService.login(uniqueEmail, "secret");
        assertTrue(loginResult, "Le login doit réussir avec le bon mot de passe");

        assertTrue(utilisateurService.estAuthentifie(), "L'utilisateur doit être authentifié");

        utilisateurService.logout();
        assertFalse(utilisateurService.estAuthentifie(), "L'utilisateur doit être déconnecté");
    }

    @Test
    public void testGetUtilisateurParId() {
        String uniqueId = String.valueOf(System.currentTimeMillis());
        String uniqueEmail = "id" + uniqueId + "@test.com";
        utilisateurService.inscrire("iduser" + uniqueId, uniqueEmail, "password");
        utilisateurService.login(uniqueEmail, "password");

        Utilisateur user = utilisateurService.getUtilisateurConnecte();
        assertNotNull(user, "L'utilisateur doit être connecté après login");
        
        userIds.add(user.getId());

        Utilisateur fetchedUser = utilisateurService.getUtilisateurParId(user.getId());

        assertNotNull(fetchedUser, "L'utilisateur récupéré ne doit pas être null");
        assertEquals(user.getPseudo(), fetchedUser.getPseudo(), "Le pseudo doit correspondre");
    }
}
