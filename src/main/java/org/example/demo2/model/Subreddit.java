package org.example.demo2.model;

import java.time.LocalDateTime;

public class Subreddit {
    private int id_subreddit;
    private String nom;
    private String description;
    private LocalDateTime date_creation;

    public Subreddit(int id_subreddit, String nom, String description, LocalDateTime date_creation) {
        this.id_subreddit = id_subreddit;
        this.nom = nom;
        this.description = description;
        this.date_creation = date_creation;
    }

    public Subreddit(String nom, String description) {
        this.nom = nom;
        this.description = description;
        this.date_creation = LocalDateTime.now();
    }

    // Getters
    public int getId_subreddit() { return id_subreddit; }
    public String getNom() { return nom; }
    public String getDescription() { return description; }
    public LocalDateTime getDate_creation() { return date_creation; }

    // Setters
    public void setId_subreddit(int id_subreddit) { this.id_subreddit = id_subreddit; }
    public void setNom(String nom) { this.nom = nom; }
    public void setDescription(String description) { this.description = description; }

    @Override
    public String toString() {
        return nom; // Pour l'affichage dans le ComboBox
    }
}
