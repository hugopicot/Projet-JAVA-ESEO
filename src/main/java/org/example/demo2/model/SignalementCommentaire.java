package org.example.demo2.model;

public class SignalementCommentaire {
   private int id_signalement_com;
  private  int id_utilisateur;
    private int id_commentaire;
    public SignalementCommentaire(int id_signalement_com, int id_utilisateur, int id_commentaire){
        this.id_signalement_com=id_signalement_com;
        this.id_utilisateur=id_utilisateur;
        this.id_commentaire=id_commentaire;
    }
    public int getId_signalement_com(){
        return id_signalement_com;
    }
    public int getId_commentaire(){
        return id_commentaire;
    }
    public int getId_utilisateur(){
        return id_utilisateur;
    }
}
