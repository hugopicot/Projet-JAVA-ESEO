package org.example.demo2.model;

import java.time.LocalDateTime;

public class Commentaire {
    private int id_commentaire;
    private String contenu;
    private LocalDateTime date_creation;
    private int score;
    private int id_utilisateur;
    private int id_post;
    private Integer id_parent; // Integer car peut être null (si réponse directe au post)

    // Constructeur COMPLET
    public Commentaire(int id_commentaire, String contenu, LocalDateTime date_creation, int score, 
                       int id_utilisateur, int id_post, Integer id_parent) {
        this.id_commentaire = id_commentaire;
        this.contenu = contenu;
        this.date_creation = date_creation;
        this.score = score;
        this.id_utilisateur = id_utilisateur;
        this.id_post = id_post;
        this.id_parent = id_parent;
    }

    // Constructeur RÉPONSE
    public Commentaire(String contenu, int id_utilisateur, int id_post, Integer id_parent) {
        this.contenu = contenu;
        this.date_creation = LocalDateTime.now();
        this.score = 0;
        this.id_utilisateur = id_utilisateur;
        this.id_post = id_post;
        this.id_parent = id_parent;
    }

    // Getters
    public int getId_commentaire() { return id_commentaire; }
    public String getContenu() { return contenu; }
    public LocalDateTime getDate_creation() { return date_creation; }
    public int getScore() { return score; }
    public int getId_utilisateur() { return id_utilisateur; }
    public int getId_post() { return id_post; }
    public Integer getId_parent() { return id_parent; }

    // Setters
    public void setId_commentaire(int id_commentaire) { this.id_commentaire = id_commentaire; }
    public void setContenu(String contenu) { this.contenu = contenu; }
    public void setScore(int score) { this.score = score; }
}
