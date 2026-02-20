package org.example.demo2.model;

import java.time.LocalDateTime;

public class Post {
    private int id_post ;
  private String titre ;
 private   String contenu ;
 private LocalDateTime date_creation ;
   private int  score;
    private int  id_utilisateur ;
    private int  id_subreddit ;
    public Post (int id_post,String titre,String contenu,LocalDateTime date_cration,int score,int id_utilisateur,int id_subreddit){
        this.id_post=id_post;
        this.titre=titre;
        this.contenu=contenu;
        this.date_creation=date_cration;
        this.score=score;
        this.id_utilisateur=id_utilisateur;
        this.id_subreddit=id_subreddit;
    }
    public int getId_post(){
        return id_post;
    }
    public String getTitre(){
        return titre;
    }
    public String getContenu(){
        return contenu;
    }
    public LocalDateTime getDate_creation(){
        return date_creation;
    }
    public int getScore(){
        return score;
    }
    public int getId_utilisateur(){
        return String.valueOf(id_utilisateur);
    }
    public int getId_subreddit(){
        return id_subreddit;
    }
}
