package org.example.demo2.model;

import java.time.LocalDateTime;

public class Vote {
   private int id_vote;
   private int type_vote;
    private LocalDateTime date_vote;
   private int   id_utilisateur;
   private int   id_post;
   private int  id_commentaire;
   public Vote( int id_vote,int type_vote,LocalDateTime date_vote,int id_utilisateur,int id_post,int id_commentaire){
       this.id_vote=id_vote;
       this.type_vote=type_vote;
       this.date_vote=date_vote;
       this.id_utilisateur=id_utilisateur;
       this.id_post=id_post;
       this.id_commentaire=id_commentaire;
   }
  public int getId_vote(){
       return id_vote;
  }
  public int getType_vote(){
       return type_vote;

  }

    public LocalDateTime getDate_vote() {
        return date_vote;
    }
    public int getId_utilisateur(){
       return id_utilisateur;
    }

    public int getId_post() {
        return id_post;
    }

    public int getId_commentaire() {
        return id_commentaire;
    }
}
