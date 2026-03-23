package org.example.demo2.model;

public class SignalementPost {
    private int id_signalement;
    private int id_utilisateur;
    private int id_post;

    public SignalementPost(int id_signalement, int id_utilisateur, int id_post) {
        this.id_signalement = id_signalement;
        this.id_utilisateur = id_utilisateur;
        this.id_post = id_post;
    }

    public SignalementPost(int id_utilisateur, int id_post) {
        this.id_utilisateur = id_utilisateur;
        this.id_post = id_post;
    }

    // Getters
    public int getId_signalement() { return id_signalement; }
    public int getId_utilisateur() { return id_utilisateur; }
    public int getId_post() { return id_post; }

    // Setters
    public void setId_signalement(int id_signalement) { this.id_signalement = id_signalement; }
}
