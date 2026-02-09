package org.example.demo2.model;

import java.time.LocalDateTime;

public class Commentaire {
    private String contenu;
    private int id_commentaire;
    private LocalDateTime date_creation;
    private int score;
    private int id_utilisateur;
    private int id_post;
    private int id_parent;

    public Commentaire(String contenu, int id_commentaire, LocalDateTime date_creation, int score, int id_utilisateur, int id_post, int id_parent) {
        this.contenu = contenu;
        this.id_commentaire = id_commentaire;
        this.date_creation = date_creation;
        this.score = score;
        this.id_utilisateur = id_utilisateur;
        this.id_post = id_post;
        this.id_parent = id_parent;
    }

}
