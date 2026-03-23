package org.example.demo2.service;


import org.example.demo2.dao.UtilisateurDao;
import org.example.demo2.model.Utilisateur;
import org.example.demo2.util.PasswordUtils;


public class UtilisateurService {

    private UtilisateurDao utilisateurDAO = new UtilisateurDao();
    private Utilisateur utilisateurConnecte = null;

    // INSCRIPTION
    public boolean inscrire(String pseudo, String email, String motDePasse) {

        // Vérifier si email déjà utilisé
        if (utilisateurDAO.getUtilisateurByEmail(email) != null) {
            return false;
        }

        String hashedPassword = PasswordUtils.hashPassword(motDePasse);

        Utilisateur utilisateur = new Utilisateur(
                1,
                pseudo,
                email,
                hashedPassword,
                0
        );

        utilisateurDAO.add(utilisateur);
        return true;
    }

    // LOGIN
    public boolean login(String email, String motDePasse) {

        Utilisateur utilisateur = utilisateurDAO.getUtilisateurByEmail(email);

        if (utilisateur == null) {
            return false;
        }

        if (PasswordUtils.checkPassword(motDePasse, utilisateur.getMotDePasse())) {
            utilisateurConnecte = utilisateur;
            return true;
        }

        return false;
    }

    // LOGOUT
    public void logout() {
        utilisateurConnecte = null;
    }

    // Vérifie si utilisateur connecté
    public boolean estAuthentifie() {
        return utilisateurConnecte != null;
    }

    // Récupérer utilisateur connecté
    public Utilisateur getUtilisateurConnecte() {
        return utilisateurConnecte;
    }
}