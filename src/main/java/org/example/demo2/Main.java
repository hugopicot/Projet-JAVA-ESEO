package org.example.demo2;

import javafx.application.Application;
import service.UtilisateurService;

public class Main {

    public static void main(String[] args) {

        UtilisateurService service = new UtilisateurService();

        // Inscription
        boolean inscrit = service.inscrire("John", "john@email.com", "123456");

        if (inscrit) {
            System.out.println("Inscription réussie !");
        } else {
            System.out.println("Email déjà utilisé !");
        }

        // Login
        boolean connecte = service.login("john@email.com", "123456");

        if (connecte) {
            System.out.println("Connexion réussie !");
            System.out.println("Bienvenue " + service.getUtilisateurConnecte().getPseudo());
        } else {
            System.out.println("Email ou mot de passe incorrect !");
        }

        // Vérifier session
        if (service.estAuthentifie()) {
            System.out.println("Utilisateur connecté.");
        }

        // Logout
        service.logout();
        System.out.println("Déconnecté.");
    }
}