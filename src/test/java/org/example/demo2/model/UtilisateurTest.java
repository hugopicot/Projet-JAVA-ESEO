package org.example.demo2.model;

import org.junit.jupiter.api.Test;
import java.sql.Timestamp;
import static org.junit.jupiter.api.Assertions.*;

public class UtilisateurTest {

    @Test
    public void testUtilisateurCreationAndGetters() {
        int id = 10;
        String pseudo = "SuperUser";
        String email = "superuser@test.com";
        String motDePasse = "Hash123!@#";
        int karma = 100;

        Utilisateur user = new Utilisateur(id, pseudo, email, motDePasse, karma);

        assertEquals(id, user.getId());
        assertEquals(pseudo, user.getPseudo());
        assertEquals(email, user.getEmail());
        assertEquals(motDePasse, user.getMotDePasse());
        assertEquals(karma, user.getKarma());
    }
}
