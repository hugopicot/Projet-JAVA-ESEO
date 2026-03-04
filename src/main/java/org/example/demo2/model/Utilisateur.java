package org.example.demo2.model;

import java.time.LocalDateTime;
import java.sql.Timestamp;

public class Utilisateur {

    private int id;
    private String pseudo;
    private String email;
    private String motDePasse;
    private Timestamp dateInscription;
    private int karma;

    public Utilisateur(int id, String pseudo, String email, String motDePasse, Timestamp dateInscription, int karma) {
        this.id = id;
        this.pseudo = pseudo;
        this.email = email;
        this.motDePasse = motDePasse;
        this.dateInscription = dateInscription;
        this.karma = karma;
    }

    public Utilisateur(String pseudo, String email, String motDePasse, int karma) {
        this.pseudo = pseudo;
        this.email = email;
        this.motDePasse = motDePasse;
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