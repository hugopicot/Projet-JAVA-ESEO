package org.example.demo2.util;

import org.example.demo2.model.Utilisateur;

/**
 * Gestionnaire de session utilisateur connecté.
 * Utilise le pattern Singleton pour partager l'utilisateur connecté entre toutes les instances.
 */
public class SessionManager {
    
    private static SessionManager instance;
    private Utilisateur utilisateurConnecte;
    
    private SessionManager() {
        // Constructeur privé pour le singleton
    }
    
    public static SessionManager getInstance() {
        if (instance == null) {
            instance = new SessionManager();
        }
        return instance;
    }
    
    public void setUtilisateurConnecte(Utilisateur utilisateur) {
        this.utilisateurConnecte = utilisateur;
    }
    
    public Utilisateur getUtilisateurConnecte() {
        return this.utilisateurConnecte;
    }
    
    public boolean estAuthentifie() {
        return this.utilisateurConnecte != null;
    }
    
    public void logout() {
        this.utilisateurConnecte = null;
    }
    
    public int getUtilisateurConnecteId() {
        return this.utilisateurConnecte != null ? this.utilisateurConnecte.getId() : -1;
    }
}