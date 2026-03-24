package org.example.demo2.model;

import java.time.LocalDateTime;
import java.sql.Timestamp;

public class Utilisateur {
    private int id_utilisateur;
    private String pseudo;
    private String email;
    private String mot_de_passe;
    private Timestamp date_inscription;
    private int karma;

    public Utilisateur(int id_utilisateur, String pseudo, String email, String mot_de_passe, int karma) {
        this.id_utilisateur = id_utilisateur;
        this.pseudo = pseudo;
        this.email = email;
        this.mot_de_passe = mot_de_passe;
        this.karma = karma;
    }

    public Utilisateur(int id_utilisateur, String pseudo, String email, String mot_de_passe, Timestamp date_inscription, int karma) {
        this.id_utilisateur = id_utilisateur;
        this.pseudo = pseudo;
        this.email = email;
        this.mot_de_passe = mot_de_passe;
        this.date_inscription = date_inscription;
        this.karma = karma;
    }

    public int getId() { return id_utilisateur; }
    public String getPseudo() { return pseudo; }
    public String getEmail() { return email; }
    public String getMotDePasse() { return mot_de_passe; }
    public Timestamp getDateInscription() { return date_inscription; }
    public int getKarma() { return karma; }

    public void setId(int id) { this.id_utilisateur = id; }
    public void setDateInscription(Timestamp date_inscription) { this.date_inscription = date_inscription; }
}
