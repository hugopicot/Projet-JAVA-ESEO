package org.example.demo2.model;


import java.time.LocalDateTime;
import java.sql.Timestamp;

public class Utilisateur {
    private int id_utilisateur;
    private String pseudo;
    private String email;
    private String mot_de_passe;
    private LocalDateTime date_inscription;
    private int karma;

    public Utilisateur(int id_utilisateur, String pseudo, String email, String mot_de_passe, int karma) {
        this.id_utilisateur = id_utilisateur;
        this.pseudo = pseudo;
        this.email = email;
        this.mot_de_passe = mot_de_passe;
        this.karma = karma;
    }
    public int getId() { return id; }
    public String getPseudo() { return pseudo; }
    public String getEmail() { return email; }
    public String getMotDePasse() { return motDePasse; }
    public Timestamp getDateInscription() { return dateInscription; }
    public int getKarma() { return karma; }

    public void setId(int id) { this.id = id; }

}