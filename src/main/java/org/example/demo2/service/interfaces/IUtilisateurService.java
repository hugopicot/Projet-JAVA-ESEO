package org.example.demo2.service.interfaces;

import org.example.demo2.model.Utilisateur;

import java.util.List;

public interface IUtilisateurService {
    boolean inscrire(String pseudo, String email, String motDePasse);
    boolean login(String email, String motDePasse);
    void logout();
    boolean estAuthentifie();
    Utilisateur getUtilisateurConnecte();
    Utilisateur getUtilisateurById(int id);
    List<Utilisateur> getAllUtilisateurs();
    void updateUtilisateur(Utilisateur utilisateur);
    void supprimerUtilisateur(int id);
}
