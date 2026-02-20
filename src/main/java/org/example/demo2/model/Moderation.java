package org.example.demo2.model;

public class Moderation {
   private int  id_moderation;
    private int  id_utilisateur;
    private static int id_subreddit ;
public  Moderation(int id_moderation,int id_utilisateur,int id_subreddit){
    this.id_moderation=id_moderation;
    this.id_utilisateur=id_utilisateur;
    this.id_subreddit=id_subreddit;
}

    public static int getId_subreddit() {
        return id_subreddit;
    }



    public int getId_moderation() {
    return id_moderation;
    }

    public int getId_utulisateur() {
        return id_subreddit;

    }
}
