package org.example.demo2.model;

import java.time.LocalDateTime;

public class Post {
    private int id_post;
    private String titre;
    private String contenu;
    private LocalDateTime date_creation;
    private int score;
    private int id_utilisateur;
    private int id_subreddit;

    // Constructeur COMPLET (pour lire depuis la BDD)
    public Post(int id_post, String titre, String contenu, LocalDateTime date_creation,
                int score, int id_utilisateur, int id_subreddit) {
        this.id_post = id_post;
        this.titre = titre;
        this.contenu = contenu;
        this.date_creation = date_creation;
        this.score = score;
        this.id_utilisateur = id_utilisateur;
        this.id_subreddit = id_subreddit;
    }

    // Constructeur SANS ID (pour créer un nouveau post)
    public Post(String titre, String contenu, int id_utilisateur, int id_subreddit) {
        this.titre = titre;
        this.contenu = contenu;
        this.date_creation = LocalDateTime.now();
        this.score = 0;
        this.id_utilisateur = id_utilisateur;
        this.id_subreddit = id_subreddit;
    }

    // ===== GETTERS =====
    public int getId_post() {
        return id_post;
    }

    public String getTitre() {
        return titre;
    }

    public String getContenu() {
        return contenu;
    }

    public LocalDateTime getDate_creation() {
        return date_creation;
    }

    public int getScore() {
        return score;
    }

    public int getId_utilisateur() {
        return id_utilisateur;
    }

    public int getId_subreddit() {
        return id_subreddit;
    }

    // ===== SETTERS =====
    public void setId_post(int id_post) {
        this.id_post = id_post;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

    public void setContenu(String contenu) {
        this.contenu = contenu;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public void setId_utilisateur(int id_utilisateur) {
        this.id_utilisateur = id_utilisateur;
    }

    public void setId_subreddit(int id_subreddit) {
        this.id_subreddit = id_subreddit;
    }
}
