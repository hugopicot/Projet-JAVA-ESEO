package org.example.demo2.service;


import org.example.demo2.dao.UtilisateurDao;
import org.example.demo2.model.Utilisateur;
import org.example.demo2.util.PasswordUtils;
import org.example.demo2.util.SessionManager;


public class UtilisateurService {

    private UtilisateurDao utilisateurDAO = new UtilisateurDao();

    // INSCRIPTION
    public boolean inscrire(String pseudo, String email, String motDePasse) {

        if (utilisateurDAO.getUtilisateurByEmail(email) != null) {
            return false;
        }

        String hashedPassword = PasswordUtils.hashPassword(motDePasse);

        Utilisateur utilisateur = new Utilisateur(
                0,
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
            SessionManager.getInstance().setUtilisateurConnecte(utilisateur);
            return true;
        }

        return false;
    }

    // LOGOUT
    public void logout() {
        SessionManager.getInstance().logout();
    }

    // Vérifie si utilisateur connecté
    public boolean estAuthentifie() {
        return SessionManager.getInstance().estAuthentifie();
    }

    // Récupérer utilisateur connecté
    public Utilisateur getUtilisateurConnecte() {
        return SessionManager.getInstance().getUtilisateurConnecte();
    }

    // Récupérer utilisateur par ID
    public Utilisateur getUtilisateurParId(int id) {
        return utilisateurDAO.findById(id);
    }
}