package org.example.demo2.model;

public class Abonnement {
    private int idUtilisateur;
    private int idSubreddit;

    public Abonnement(int idUtilisateur, int idSubreddit) {
        this.idUtilisateur = idUtilisateur;
        this.idSubreddit = idSubreddit;
    }

    public int getIdUtilisateur() {
        return idUtilisateur;
    }

    public void setIdUtilisateur(int idUtilisateur) {
        this.idUtilisateur = idUtilisateur;
    }

    public int getIdSubreddit() {
        return idSubreddit;
    }

    public void setIdSubreddit(int idSubreddit) {
        this.idSubreddit = idSubreddit;
    }
}
